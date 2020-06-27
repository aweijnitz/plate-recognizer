package info.andersw.webui;

import info.andersw.model.ClientAckMessage;
import info.andersw.model.ImageMessage;
import info.andersw.mq.ImageMessageSender;
import info.andersw.mq.MQConfig;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@CommonsLog
public class ImageUploadController {

    @Autowired
    ImageMessageSender sender;

    @PostMapping("/api/v1/submit")
    ClientAckMessage uploadImage(@RequestBody ImageMessage imgMsg) {
//        log.debug("POST received message: "
//                + imgMsg.getClientId() + " msgId: "
//                + imgMsg.getMessageId()
//                + " dataSize:" + imgMsg);
        log.debug("POST RECEIVED " + imgMsg);
        if (imgMsg.getImageData() != null)
            sender.sendMessage(imgMsg);
        return new ClientAckMessage("Submitted for analysis.");
    }

}
