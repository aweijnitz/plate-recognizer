package info.andersw.plateinfo;

import info.andersw.model.ParsedPlateMessage;
import info.andersw.model.PlateMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GermanRegionPrefixesTest {

    @Autowired
    GermanRegionPrefixes gp;

    @Test
    void init() throws IOException {
        assertEquals("Ulm", gp.getRegion("UL"));
    }

    @Test
    void getRegionForExistingCode() throws IOException {
        assertEquals("Ulm", gp.getRegion("UL"));
    }

    @Test
    void getRegionForNonExistingCode() throws IOException {
        assertEquals(null, gp.getRegion("NONEXISING"));
    }

    @Test
    void getRegionForManyPlates() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Ahrweiler");
        expected.add("Traunstein");
        expected.add("Ulm");
        ArrayList<String> input = new ArrayList<>();
        input.add("AW1233");
        input.add("LF1234");
        input.add("ULX123");
        assertEquals(expected, gp.getRegions(input));
    }

    @Test
    void getRegionForSingleEntryArrayPlates() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Ahrweiler");
        ArrayList<String> input = new ArrayList<>();
        input.add("AW1233");
        assertEquals(expected, gp.getRegions(input));
    }

    @Test
    void getRegionForEmptyArrayPlates() {
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> input = new ArrayList<>();
        assertEquals(expected, gp.getRegions(input));
    }

    @Test
    void getRegionsForPlateMessage() {
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Ahrweiler");
        expected.add("Traunstein");
        expected.add("Ulm");
        ArrayList<String> input = new ArrayList<>();
        input.add("AW1233");
        input.add("LF1234");
        input.add("ULX123");
        PlateMessage inputMsg = new PlateMessage("c0", input, "abc123");
        ParsedPlateMessage result = gp.parseMessage(inputMsg);
        assertEquals(inputMsg.getClientId(), result.getClientId());
        assertEquals(inputMsg.getMessageId(), result.getMessageId());
        assertEquals(expected, result.getRegions());
    }
}