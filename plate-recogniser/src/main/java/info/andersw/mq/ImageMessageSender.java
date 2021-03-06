package info.andersw.mq;

import info.andersw.model.ImageMessage;
import javaxt.io.Image;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Random;

@Service
@CommonsLog
public class ImageMessageSender {

    @Autowired
    private MQConfig config;

    private RabbitTemplate rabbitTemplate;

    public ImageMessageSender() {
    }

    @Autowired
    public ImageMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 2000L)
    public void sendTestMessage() {
        if (!config.isProduceTestMessages())
            return;

        String[] testImages = {
                "testImages/HPLS71-nearer.jpg",
                "testImages/MUP2911_HPLS71.jpg",
                "testImages/h786poj.jpg",
                "testImages/BKL25.jpg",
                "testImages/MFS2187.jpg"};
        Image image = new Image(testImages[new Random().nextInt(testImages.length)]);
        image.rotate(); // Rotate the image based on the image metadata (EXIF Orientation tag).
        byte[] imgBytes = image.getByteArray(); // as jpg bytes
        String base64String = Base64.getEncoder().encodeToString(imgBytes);
        final var message = new ImageMessage("client-abc123", base64String, "msgId-234");
        sendMessage(message);
    }

    public void sendMessage(ImageMessage message) {
        log.info("Sending ImageMessage to " + config.imagesQ().getName());
        rabbitTemplate.convertAndSend(config.imagesQ().getName(), message);
    }
}