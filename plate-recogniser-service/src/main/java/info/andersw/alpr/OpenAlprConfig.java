package info.andersw.alpr;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "openalpr")
@ToString
public class OpenAlprConfig {

    @Getter
    @Setter
    private String nativeMode;

    @Getter
    @Setter
    private String runtimeDataDir;

    @Getter
    @Setter
    private String configFile;

    @Getter
    @Setter
    private String defaultLocation;

    @Getter
    @Setter
    private String pathToalpr;

    @Getter
    @Setter
    private String imageDir;

}
