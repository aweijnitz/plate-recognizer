package info.andersw.mq;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@CommonsLog
public class ParsedPlateMessageSender {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    MQConfig config;

    FanoutExchange exch;

    public ParsedPlateMessageSender() {}

    @PostConstruct
    public void init() {
        exch = fanoutExchange();
    }

    private FanoutExchange fanoutExchange() {
        String exchName = config.getParsedPlatesExchange();
        log.debug("Creating FanoutExchange with name: " + exchName);
        FanoutExchange exch = ExchangeBuilder.fanoutExchange(exchName).build();
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
