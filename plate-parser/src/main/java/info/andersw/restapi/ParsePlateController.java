package info.andersw.restapi;

import info.andersw.model.ParsedPlateMessage;
import info.andersw.model.PlateMessage;
import info.andersw.plateinfo.GermanRegionPrefixes;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@CommonsLog
public class ParsePlateController {

    @Autowired
    GermanRegionPrefixes parser;

    @PostMapping("/api/v1/parse")
    public ParsedPlateMessage parse(@RequestBody PlateMessage msg) {
        log.debug("POST: Received " + msg.toString());
        ParsedPlateMessage result = parser.parseMessage(msg);
        log.debug("Returning " + result.toString());
        return result;
    }

}
