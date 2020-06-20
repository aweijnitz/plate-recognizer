package info.andersw.alpr;

import com.openalpr.jni.Alpr;
import info.andersw.alpr.OpenAlprConfig;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Service
@CommonsLog
public class OpenAlprLibraryWrapper {

    @Autowired
    private OpenAlprConfig conf;

    @Autowired
    private ExternalAlprExecution execAlpr;

    public OpenAlprLibraryWrapper() {
    }

    @PostConstruct
    public boolean selfCheck() {
        log.warn("SELFCHECK START");
        try {
            ArrayList<String> result = execAlpr.runAlpr("h786poj.jpg");
            if(result.size() == 0)
                return false;
        } catch (InterruptedException | IOException e) {
            log.warn("SELFCHECK FAIL");
            e.printStackTrace();
            return false;
        }
        log.info("SELFCHECK PASS");
        return true;
    }
}
