package info.andersw.alpr;

import com.fasterxml.jackson.core.JsonProcessingException;
import info.andersw.alpr.parsing.ClassificationResultParser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
@CommonsLog
public class ExternalAlprExecution {

    @Autowired
    private OpenAlprConfig config;

    private String baseCmd;

    public ExternalAlprExecution() {
    }

    @PostConstruct
    public void init() {
        baseCmd = new File(config.getPathToalpr() + "alpr").getAbsolutePath();
        baseCmd += " -c " + config.getDefaultLocation();
        baseCmd += " --config " + new File(config.getConfigFile()).getAbsolutePath();
        baseCmd += " -n 3 ";    // We only care about the top results
        baseCmd += " --clock "; // Capture the execution time
        baseCmd += " -j ";      // JSON result format
        log.info("Base command: " + baseCmd);
    }

    public ArrayList<String> runAlpr(String imgFileName) {
        String absoluteFilePath = Paths.get(config.getImageDir() + imgFileName).toAbsolutePath().toString();
        ArrayList<String> result = new ArrayList<>();
        ProcessBuilder builder = new ProcessBuilder();
        String cmd = baseCmd + absoluteFilePath;
        log.debug("About to execute " + cmd);
        builder.command("sh", "-c", cmd);
        Process process = null;
        int exitCode = 0;

        try {
            ArrayList<String> cmdOutput = new ArrayList<>();
            process = builder.start();
            Consumer<String> commandResult = cmdOutput::add;
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), commandResult);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            exitCode = process.waitFor();
            if (exitCode != 0) {
                log.warn("Sub process exited with exit code " + exitCode + " for " + cmd);
                return result;
            }
            ClassificationResultParser crp = new ClassificationResultParser(cmdOutput);
            log.debug("Result " + crp.getTopPlates().toString());
            return crp.getTopPlates();

        } catch (IOException | InterruptedException e) {
            log.error("Exception for " + cmd + ", " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }


    /*
     * Lifted straight from the example https://www.baeldung.com/run-shell-command-in-java
     */
    private static class StreamGobbler implements Runnable {
        private final InputStream inputStream;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

}

