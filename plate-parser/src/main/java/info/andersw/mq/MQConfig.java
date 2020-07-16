package info.andersw.mq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
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

    @Getter
    @Setter
    private FanoutExchange exchange;

    final AmqpAdmin amqpAdmin;
    private final Queue parsedPlatesQ;
    private final Queue bookKeeperQ;

    public MQConfig(AmqpAdmin amqpAdmin,
                    @Qualifier("parsedPlatesQ") Queue parsedPlatesQ,
                    @Qualifier("bookKeeperQ") Queue bookKeeperQ) {
        this.amqpAdmin = amqpAdmin;
        this.parsedPlatesQ = parsedPlatesQ;
        this.bookKeeperQ = bookKeeperQ;
    }


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




    @Bean
    @Profile("!test")
    public FanoutExchange fanoutExchange() {
        boolean durable = true;
        boolean autoDelete = false;
        String exchName = getParsedPlatesExchange();
        log.debug("Creating FanoutExchange with name: " + exchName);
        // FanoutExchange exch = ExchangeBuilder.fanoutExchange(exchName).build();
        FanoutExchange exch = new FanoutExchange(exchName, true, autoDelete);
        Binding bindingPp = BindingBuilder.bind(parsedPlatesQ).to(exch);
        Binding bindingBk = BindingBuilder.bind(bookKeeperQ).to(exch);
        amqpAdmin.declareExchange(exch);
        amqpAdmin.declareQueue(parsedPlatesQ);
        amqpAdmin.declareQueue(bookKeeperQ);
        amqpAdmin.declareBinding(bindingPp);
        amqpAdmin.declareBinding(bindingBk);
        return exch;
    }

    @Bean
    @Profile("test")
    public FanoutExchange fanoutExchangeTest() {
        boolean durable = false;
        boolean autoDelete = true;
        String exchName = getParsedPlatesExchange();
        log.debug("Creating TEST FanoutExchange with name: " + exchName);
        // FanoutExchange exch = ExchangeBuilder.fanoutExchange(exchName).build();
        FanoutExchange exch = new FanoutExchange(exchName, durable, autoDelete);
        return exch;
    }

}
