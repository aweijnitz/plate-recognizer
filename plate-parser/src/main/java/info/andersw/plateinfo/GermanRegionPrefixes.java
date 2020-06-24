package info.andersw.plateinfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.andersw.model.ParsedPlateMessage;
import info.andersw.model.PlateMessage;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds plate prefixes "database" and can match license plates to regions.
 * This represents the core business logic of this service.
 *
 * Original data source: https://daten.gdz.bkg.bund.de/produkte/sonstige/kfz250/aktuell/kfz250.gk3.csv.zip
 * The data can be downloaded and built as JSON by closing and building this repo: https://github.com/derhuerst/german-license-plate-prefixes
 */
@Service
@CommonsLog
public class GermanRegionPrefixes {

    private HashMap<String, String> prefixes;

    @Autowired
    ResourceLoader resourceLoader;

    public GermanRegionPrefixes() {
    }

    @PostConstruct
    public void init() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:german-license-plate-prefixes.json");
        File jsonFile = resource.getFile();
        prefixes = (HashMap<String, String>) new ObjectMapper()
                .readValue(jsonFile, new TypeReference<Map<String, String>>() {
                });
        log.debug("Loaded prefix data. Total region prefixes loaded: " + prefixes.keySet().size());
    }

    public String getRegion(String plateRegionCode) {
        return prefixes.get(plateRegionCode);
    }

    public ArrayList<String> getRegions(ArrayList<String> plates) {
        ArrayList<String> result = new ArrayList<>();

        // TODO
        // For each plate in message
        plates.forEach(plate -> {
            log.debug("Processing plate " + plate + " PREFIX: " + plate.substring(0, 2));
            String regionCandidate = plate.substring(0, 3);
            while (regionCandidate.length() > 0) {
                String hit = getRegion(regionCandidate);
                if(hit != null) {
                    result.add(hit);
                    break;
                }
                regionCandidate = regionCandidate.substring(0, regionCandidate.length() - 1);
            }
        });
        return result;
    }

    public ParsedPlateMessage parseMessage(PlateMessage msg) {
        log.debug("Parsing msg " + msg.toString());
        ArrayList<String> regions = getRegions(msg.getPlateNumbers());
        return new ParsedPlateMessage(msg.getClientId(), regions, msg.getMessageId());
    }
}
