package info.andersw.mq;

import info.andersw.model.PlateMessage;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@CommonsLog
public class PlateMessageSender {

    @Autowired
    private MQConfig config;

    private RabbitTemplate rabbitTemplate;

    public PlateMessageSender() {
    }

    @Autowired
    public PlateMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(PlateMessage message) {
        log.info("Sending message: " + message.toString() + " to queue " + config.platesQ().getName());
        rabbitTemplate.convertAndSend(config.platesQ().getName(), message);
    }
}