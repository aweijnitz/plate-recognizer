package info.andersw;

import com.openalpr.jni.Alpr;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.Objects;

@Service
@CommonsLog
public class OpenAlprLibraryWrapper {

    @Autowired
    private OpenAlprConfig conf;
    private final Alpr alpr = null;

    public OpenAlprLibraryWrapper() {
    }

    @PostConstruct
    public void initialize() {
        log.info("Alpr Native Mode : " + conf.getNativeMode());
        File cf = new File(conf.getConfigFile());
        String cfgFileName = cf.getAbsolutePath();
        log.info("Loading openalpr config from " + cfgFileName);
        Alpr alpr = new Alpr(
                conf.getDefaultLocation(),
                cfgFileName,
                new File(conf.getRuntimeDataDir()).getAbsolutePath(),
                new File(conf.getNativeLibFile()).getAbsolutePath(),
                new File(conf.getJniFile()).getAbsolutePath());
        log.info("Using Forked OpenALPR Version (fork https://github.com/aweijnitz/openalpr-forked-macosx): " + alpr.getVersion());
        log.info("Runtime data dir: " + new File(conf.getRuntimeDataDir()).getAbsolutePath());
        log.info("OpenALPR Successfully loaded: " + alpr.isLoaded());
    }

    @PreDestroy
    public void destroy() {
        log.info("Unloading native libraries.");
        if (alpr != null) {
            alpr.unload();
        }
    }
}
