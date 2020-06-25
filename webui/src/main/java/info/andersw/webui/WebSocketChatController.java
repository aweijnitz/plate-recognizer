package info.andersw.webui;

import info.andersw.model.ClientAckMessage;
import info.andersw.model.ImageMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/javainuse")
    public ClientAckMessage sendMessage(@Payload ImageMessage inMessage) {
        return new ClientAckMessage("Hello from WebSocketChatController:sendMessage");
    }

    @MessageMapping("/chat.newUser")
    @SendTo("/topic/javainuse")
    public ClientAckMessage newUser(@Payload ImageMessage inMessage,
                                    SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("clientId", inMessage.getClientId());
        return new ClientAckMessage("Hello from WebSocketChatController:newUser");
    }
}