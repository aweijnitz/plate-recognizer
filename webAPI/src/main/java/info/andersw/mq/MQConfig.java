package info.andersw.mq;

import lombok.ToString;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mq")
@ToString
public class MQConfig {

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue imagesQ() {
        boolean durable = true;
        Queue imagesQ = new Queue("images", durable);
        return imagesQ;
    }
    
}
