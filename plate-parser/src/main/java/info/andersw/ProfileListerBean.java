package info.andersw;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

/**
 * Simple helper class that log which Spring profiles are currently active.
 *
 */
@Component
@CommonsLog
class ProfileListerBean {

    private Environment environment;

    ProfileListerBean(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    void postConstruct(){
        String[] activeProfiles = environment.getActiveProfiles();
        String profiles = Arrays.toString(activeProfiles);
        log.info("Active profiles: "+profiles);
    }

}