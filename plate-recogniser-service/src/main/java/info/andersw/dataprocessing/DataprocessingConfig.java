package info.andersw.dataprocessing;

import info.andersw.mq.RecogntionCompletedAction;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dataprocessing")
@ToString
public class DataprocessingConfig {

    @Getter
    @Setter
    private String workdir;

    @Getter
    @Setter
    private int threadPoolSize;

}
