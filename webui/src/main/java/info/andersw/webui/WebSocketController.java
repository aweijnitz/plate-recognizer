package info.andersw.webui;

import info.andersw.model.ClientAckMessage;
import info.andersw.model.ImageMessage;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@CommonsLog
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    // @SendToUser indicates that the return value of a
    // message-handling method should be sent as a Message to the specified
    // destination(s) prepended with “/user/{username}“.
    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public ClientAckMessage processMessageFromClient(
            @Payload ImageMessage message) throws Exception {
        log.info("INCOMING " + message.toString());
        ClientAckMessage ack = new ClientAckMessage("Hello from WebSocketController:processMessageFromClient");
        messagingTemplate.convertAndSend("/queue/reply", ack);
        //messagingTemplate.convertAndSend("/app/queue/reply", ack);
        return ack;
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        log.info("ERROR HANDLED " + exception.getMessage());
        return exception.getMessage();
    }
}