package info.andersw.mq;

import info.andersw.model.ParsedPlateMessage;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@CommonsLog
public class ParsedPlateMessageSender {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MQConfig config;

    @Getter
    FanoutExchange exchange;

    public ParsedPlateMessageSender() {
    }

    @PostConstruct
    public void init() {
        exchange = fanoutExchange();
    }

    public void sendMessage(ParsedPlateMessage message) {
        log.debug("Sending ParsedPlateMessage to " + config.getParsedPlatesExchange());
        rabbitTemplate.convertAndSend(config.getParsedPlatesExchange(), "", message);
    }

    private FanoutExchange fanoutExchange() {
        boolean durable = true;
        boolean autoDelete = false;
        String exchName = config.getParsedPlatesExchange();
        log.debug("Creating FanoutExchange with name: " + exchName);
        // FanoutExchange exch = ExchangeBuilder.fanoutExchange(exchName).build();
        FanoutExchange exch = new FanoutExchange(exchName, true, autoDelete);
        Binding bindingPp = BindingBuilder.bind(config.parsedPlatesQ()).to(exch);
        Binding bindingBk = BindingBuilder.bind(config.bookKeeperQ()).to(exch);
        amqpAdmin.declareExchange(exch);
        amqpAdmin.declareQueue(config.parsedPlatesQ());
        amqpAdmin.declareQueue(config.bookKeeperQ());
        amqpAdmin.declareBinding(bindingPp);
        amqpAdmin.declareBinding(bindingBk);
        return exch;
    }

}
