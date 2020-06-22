package info.andersw.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.apachecommons.CommonsLog;
import java.io.Serializable;
import java.util.ArrayList;

@CommonsLog
@ToString
public final class PlateMessage implements Serializable {

    @Getter
    private final ArrayList<String> plateNumbers;

    @Getter
    private final String clientId;

    public PlateMessage(@JsonProperty("clientId") String clientId,
                        @JsonProperty("plateNumbers") ArrayList<String> plateNumbers) {
        this.plateNumbers = plateNumbers;
        this.clientId = clientId;
    }

}