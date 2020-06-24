package info.andersw.mq;

import info.andersw.alpr.OpenAlprLibraryWrapper;
import info.andersw.model.ImageMessage;
import info.andersw.model.PlateMessage;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@CommonsLog
public class ImageMessageReceiver {

    @Autowired
    private OpenAlprLibraryWrapper imageAnalyzer;

    @Autowired
    private PlateMessageSender plateMessageSender;

    @RabbitListener(queues = "images")
    public void receiveMessage(final ImageMessage message) {
        log.info("Received message from `images`");
        try {
            PlateMessage pMsg = imageAnalyzer.analyzeImage(message);
            if (pMsg.getPlateNumbers().size() > 0)
                plateMessageSender.sendMessage(pMsg);
        } catch (IOException e) {
            log.warn("Failed to recognize image!" + e.getMessage());
            e.printStackTrace();
        }
    }

}