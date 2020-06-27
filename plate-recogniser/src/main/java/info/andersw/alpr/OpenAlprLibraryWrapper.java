package info.andersw.alpr;

import info.andersw.model.ImageMessage;
import info.andersw.model.PlateMessage;
import javaxt.io.Image;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;

@Service
@CommonsLog
public class OpenAlprLibraryWrapper {

    @Autowired
    private OpenAlprConfig conf;

    @Autowired
    private ExternalAlprExecution execAlpr;

    public OpenAlprLibraryWrapper() {
    }

    public ArrayList<String> analyzeImageFile(@NonNull String fileName) {
        ArrayList<String> result;
        try {
            result = execAlpr.runAlpr(fileName);
        } catch (Throwable e) {
            e.printStackTrace();
            result = new ArrayList<>();
        }
        return result;
    }

    protected byte[] decodeImage(String imageDataBase64Str) {
        int dataStart = imageDataBase64Str.indexOf(",") + 1; // Skip data URI header
        String data = imageDataBase64Str.substring(dataStart);
        return Base64.getDecoder().decode(data);
    }

    protected String fileSuffix(String imageDataBase64Str) {
        int suffixStart = "data:image/".length();
        int suffixEnd = imageDataBase64Str.indexOf(";"); // Ex. "data:image/jpeg;base64,/ ..."
        return imageDataBase64Str.substring(suffixStart, suffixEnd);
    }

    public PlateMessage analyzeImage(@NonNull ImageMessage message) throws IOException {
        if (message.getImageData() == null)
            return new PlateMessage(message.getClientId(), new ArrayList<String>(), message.getMessageId());
        if (message.getImageData().length() < 128)
            return new PlateMessage(message.getClientId(), new ArrayList<String>(), message.getMessageId());

        Path workFile = createWorkfileName(message);
        log.debug("Writing workfile " + workFile.toString());

        byte[] imageBytes = decodeImage(message.getImageData());

        log.debug("Unpacked image from message. Bytes: " + imageBytes.length);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        log.debug("BufferedImage image from message. Width: " + bufferedImage.getWidth() + " height: " + bufferedImage.getHeight());
        Image image = new Image(bufferedImage);
        image.saveAs(new File(workFile.toString()));
        ArrayList<String> result = analyzeImageFile(workFile.getFileName().toString());
        //Files.deleteIfExists(workFile);
        return new PlateMessage(message.getClientId(), result, message.getMessageId());
    }

    private Path createWorkfileName(@NonNull ImageMessage message) {
        String suffix = fileSuffix(message.getImageData());
        String fileName = "tmp."
                + message.getClientId().hashCode()
                + "." + message.getImageData().substring(0, 15).hashCode()
                + "." + suffix;
        return Paths.get(conf.getImageDir() + File.separator + fileName).toAbsolutePath();
    }

    @PostConstruct
    public boolean selfCheck() {
        if (!conf.isRunSelfCheckOnStart())
            return true;
        log.info("Running self-check.");
        ArrayList<String> result = analyzeImageFile("MUP2911_HPLS71.jpg");
        if (result.size() == 0) {
            log.warn("SELF CHECK FAIL");
            return false;
        }
        log.info("Self-check passed.");
        return true;
    }
}
