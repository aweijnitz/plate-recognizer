package info.andersw;

import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.openalpr.jni.Alpr;

import java.io.File;
import java.io.IOException;


@SpringBootApplication
@CommonsLog
public class App {
    @Autowired
    static OpenAlprConfig conf;

    public static void main(String... args) {
        String runtimeDataDir = "openalpr-runtime-data"; // conf.getRuntimeDataDir();
        String confFile = "openalpr-lib/openalpr.conf"; // conf.getConfigFile();
/*
        File resource = null;
        try {

            ClassPathResource cpr = new ClassPathResource(
                    "openalpr.conf");
            log.info("Classpath Resrouce Location: " + cpr.getPath());
            resource = new ClassPathResource(
                    "openalpr.conf").getFile();
            String configfile = resource.getAbsoluteFile().getAbsolutePath();


        } catch (IOException e) {
            e.printStackTrace();
        }
         */
        File cf = new File("openalpr-lib/openalpr.conf");
        String cfName = cf.getAbsoluteFile().getAbsolutePath();
        log.info("Loading openalpr config from " + cfName);
        Alpr alpr = new Alpr("eu", cfName, runtimeDataDir);
        log.info("Using OpenALPR Version: " + alpr.getVersion());
        log.info("OpenALPR Successfully loaded: " + alpr.isLoaded());

        SpringApplication.run(App.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/message").allowedOrigins("http://localhost");
            }
        };
    }
}
