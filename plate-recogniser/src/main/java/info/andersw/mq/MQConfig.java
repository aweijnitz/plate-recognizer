package info.andersw.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mq")
@EnableScheduling
@ToString
public class MQConfig {
    
    @Getter
    @Setter
    private boolean produceTestMessages;

    @Bean
    public Queue imagesQ() {
        boolean durable = true;
        Queue imagesQ = new Queue("images", durable);
        return imagesQ;
    }

    @Bean
    public Queue platesQ() {
        boolean durable = true;
        Queue imagesQ = new Queue("plates", durable);
        return imagesQ;
    }

}
