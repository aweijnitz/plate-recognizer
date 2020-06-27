package info.andersw.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mq")
@ToString
@CommonsLog
public class MQConfig {


    @Getter
    @Setter
    private String parsedPlatesExchange;

    @Getter
    @Setter
    private String parsedPlatesQueueName;

    @Getter
    @Setter
    private String bookKeeperQueueName;

    @Getter
    @Setter
    private String rabbitmqHost;

    @Getter
    @Setter
    private String rabbitmqPort;

    @Getter
    @Setter
    private String rappbitmqUsername;

    @Getter
    @Setter
    private String rappbitmqPassword;

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public Queue platesQ() {
        boolean durable = true;
        // Messages from the Recognizer arrive here
        // PlateMessages arrive for parsing
        Queue platesQ = new Queue("plates", durable);
        return platesQ;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    /*
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(getRabbitmqHost());
        connectionFactory.setUsername(getRappbitmqUsername());
        connectionFactory.setPassword(getRappbitmqPassword());
        log.debug("ConnectionFactory created: " + connectionFactory.getHost() + " " + connectionFactory.getUsername());
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }


    @Bean
    @DependsOn({"rabbitTemplate"})
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
*/

    @Bean
    public Queue parsedPlatesQ() {
        return new Queue(getParsedPlatesQueueName());
    }

    @Bean
    public Queue bookKeeperQ() {
        return new Queue(getBookKeeperQueueName());
    }

}
