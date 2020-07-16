package info.andersw.mq;

import info.andersw.model.ParsedPlateMessage;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@CommonsLog
@Profile("!test")
public class ParsedPlateMessageSender {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MQConfig config;

    @Autowired
    FanoutExchange exchange;

    public ParsedPlateMessageSender() {
    }


    public void sendMessage(ParsedPlateMessage message) {
        log.debug("Sending ParsedPlateMessage to " + config.getParsedPlatesExchange());
        rabbitTemplate.convertAndSend(config.getParsedPlatesExchange(), "", message);
    }


}
