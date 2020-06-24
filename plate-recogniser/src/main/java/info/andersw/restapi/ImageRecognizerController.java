package info.andersw.restapi;

import info.andersw.alpr.OpenAlprConfig;
import info.andersw.alpr.OpenAlprLibraryWrapper;
import info.andersw.model.ImageMessage;
import info.andersw.model.PlateMessage;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@CommonsLog
public class ImageRecognizerController {

    private AtomicInteger fileNr = new AtomicInteger();

    @Autowired
    private OpenAlprConfig config;

    @Autowired
    private OpenAlprLibraryWrapper analyzer;

    @PostMapping("/api/v1/recognize")
    public ArrayList<String>  handleFileUpload(@RequestParam("image") MultipartFile image) throws IOException {

        Path tmpFileName = createWorkfileName(""+image.hashCode()+"-"+image.getSize()+"-"+image.getOriginalFilename());
        log.debug("POST Received and storing image under name " + tmpFileName.toString());
        Files.write(tmpFileName, image.getBytes());
        ArrayList<String> plates = analyzer.analyzeImageFile(tmpFileName.getFileName().toString());
        Files.deleteIfExists(tmpFileName);
        log.debug("POST Returning " + plates);
        return plates;
    }
    
    private Path createWorkfileName(@NonNull String name) {
        String fileName = "tmp." + fileNr.incrementAndGet() + name;
        return Paths.get(config.getImageDir() + File.separator + fileName).toAbsolutePath();
    }
}
