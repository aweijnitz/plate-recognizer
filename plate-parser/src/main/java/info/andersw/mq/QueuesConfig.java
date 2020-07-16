package info.andersw.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mq")
@ToString
@CommonsLog
public class QueuesConfig {

    @Getter
    @Setter
    private String parsedPlatesQueueName;

    @Getter
    @Setter
    private String bookKeeperQueueName;

    @Bean(name = "parsedPlatesQ")
    public Queue parsedPlatesQ() {
        return new Queue(getParsedPlatesQueueName());
    }

    @Bean(name = "bookKeeperQ")
    public Queue bookKeeperQ() {
        return new Queue(getBookKeeperQueueName());
    }
}
