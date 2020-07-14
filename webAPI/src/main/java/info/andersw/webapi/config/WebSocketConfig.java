package info.andersw.webapi.config;


import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@CommonsLog
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("registerStompEndpoints ADDING /websocket");
        // Register stomp endpoint(s) and allow any client to connect
        // TODO: Add header check for access control, or narrow down from *
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:8000");
                //.withSockJS();
    }

    /*
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(3000000); // 3MB, default : 64 * 1024
        registration.setSendTimeLimit(20 * 10000); // default : 10 * 10000
        registration.setSendBufferSizeLimit(30000000); // 3MB, default : 512 * 1024
    }
*/

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        log.debug("configureMessageBroker adding app destination /app");
        registry.setApplicationDestinationPrefixes("/app");

        // Set up message broker to carry the messages back to the client
        // on destinations prefixed with “/topic” and “/queue”.
        registry.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }
}