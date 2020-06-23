package info.andersw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;

import java.io.Serializable;
import java.util.ArrayList;

@CommonsLog
@ToString
public final class ParsedPlateMessage implements Serializable {

    @Getter
    private final ArrayList<String> regions;

    @Getter
    private final String messageId;

    @Getter
    private final String clientId;

    public ParsedPlateMessage(@JsonProperty("clientId") String clientId,
                              @JsonProperty("plateNumbers") ArrayList<String> plateNumbers,
                              @JsonProperty("messageId") String messageId) {
        this.regions = plateNumbers;
        this.clientId = clientId;
        this.messageId = messageId;
    }

}