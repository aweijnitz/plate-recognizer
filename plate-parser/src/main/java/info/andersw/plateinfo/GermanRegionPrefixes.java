package info.andersw.plateinfo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds plate prefixes.
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
                .readValue(jsonFile, new TypeReference<Map<String, String>>() {});
        log.debug("Loaded prefix data. Nr. prefixes: " + prefixes.keySet().size());
    }

    public String getRegionFor(String plateRegionCode) {
        return prefixes.get(plateRegionCode);
    }
}
