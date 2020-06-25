package info.andersw.model;

import lombok.Data;

@Data
public class ClientAckMessage {

    String message;

    public ClientAckMessage() {}

    public ClientAckMessage(String message) {
        this.message = message;
    }
}
