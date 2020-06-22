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
    private final String clientId;
    @Getter
    private final String imageData;

    public ImageMessage(@JsonProperty("clientId") String clientId,
                        @JsonProperty("imageData") String imageData) {
        this.clientId = clientId;
        this.imageData = imageData;
    }
}