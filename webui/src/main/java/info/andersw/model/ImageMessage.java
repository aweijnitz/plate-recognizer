package info.andersw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

import java.io.Serializable;

@CommonsLog
@ToString
public final class ImageMessage implements Serializable {

    @Getter
    @JsonProperty("clientId")
    private final String clientId;

    @Getter
    @JsonProperty("imageData")
    private final String imageData;

    @Getter
    @JsonProperty("messageId")
    private final String messageId;

    public ImageMessage(@JsonProperty("clientId") String clientId,
                        @JsonProperty("imageData") String imageData,
                        @JsonProperty("messageId") String messageId) {
        this.clientId = clientId;
        this.imageData = imageData;
        this.messageId = messageId;
    }
}