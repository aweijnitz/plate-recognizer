package info.andersw.mq;

import info.andersw.model.ImageMessage;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class ImageMessageSender {

    @Autowired
    private MQConfig config;

    private RabbitTemplate rabbitTemplate;

    public ImageMessageSender() { }

    @Autowired
    public ImageMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(ImageMessage message) {
        log.info("Sending ImageMessage to " + config.imagesQ().getName());
        rabbitTemplate.convertAndSend(config.imagesQ().getName(), message);
    }
}