package info.andersw.webui;

import info.andersw.model.ClientAckMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;


    // @SendToUser indicates that the return value of a
    // message-handling method should be sent as a Message to the specified
    // destination(s) prepended with “/user/{username}“.
    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public ClientAckMessage processMessageFromClient(
            @Payload String message) throws Exception {
        return new ClientAckMessage("Hello from WebSocketController:processMessageFromClient");
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}