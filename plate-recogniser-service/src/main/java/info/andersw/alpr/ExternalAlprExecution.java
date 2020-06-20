package info.andersw.alpr;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
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
        baseCmd += " -n 10 ";
        log.info("Base command: " + baseCmd);
    }

    public ArrayList<String> runAlpr(String imgFileName) throws InterruptedException, IOException {
        log.info("ANALYZING IMAGE " + imgFileName);
        ArrayList<String> result = new ArrayList<>();
        // alpr -c eu --config ./openalpr-lib/openalpr.conf -n 3 ./testImage/h786poj.jpg
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", baseCmd + config.getImageDir() + imgFileName);
        //builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        Consumer<String> commandResult = result::add;
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(),  commandResult);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        log.info("RESULT " + result.toString());
        return result;
    }

    /*
     * Lifted straight frmo the example https://www.baeldung.com/run-shell-command-in-java
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

