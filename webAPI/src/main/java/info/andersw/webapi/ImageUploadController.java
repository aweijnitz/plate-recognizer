package info.andersw.webapi;

import info.andersw.model.ClientAckMessage;
import info.andersw.model.ImageMessage;
import info.andersw.mq.ImageMessageSender;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (imgMsg.getClientId() == null
                || imgMsg.getMessageId() == null
                || imgMsg.getImageData() == null)
            return new ClientAckMessage("Message not valid.");

        log.debug("POST RECEIVED. Data size (Kb)" + (imgMsg.getImageData().length() / 1024));

        if (imgMsg.getImageData() != null)
            sender.sendMessage(imgMsg);

        return new ClientAckMessage("Submitted for analysis.");
    }
}
