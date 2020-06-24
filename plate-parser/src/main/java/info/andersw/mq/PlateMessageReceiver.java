package info.andersw.mq;

import info.andersw.model.ParsedPlateMessage;
import info.andersw.model.PlateMessage;
import info.andersw.plateinfo.GermanRegionPrefixes;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class PlateMessageReceiver {

    @Autowired
    private ParsedPlateMessageSender sender;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    GermanRegionPrefixes prefixes;

    @RabbitListener(queues = "plates")
    public void receiveMessage(final PlateMessage message) {
        log.debug("Received message from `plates` " + message.toString());
        ParsedPlateMessage result = prefixes.parseMessage(message);
        log.debug("Publishing result to " + sender.getExchange().getName() + " " + result.toString());
        rabbitTemplate.convertAndSend(sender.getExchange().getName(), "", result);
    }

}