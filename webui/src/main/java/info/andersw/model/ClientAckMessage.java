package info.andersw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClientAckMessage {

    @JsonProperty("message")
    String message;

    public ClientAckMessage() {}

    public ClientAckMessage(@JsonProperty("message") String message) {
        this.message = message;
    }
}
