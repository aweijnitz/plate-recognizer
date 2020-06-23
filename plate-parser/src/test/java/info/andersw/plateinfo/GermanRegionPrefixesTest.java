package info.andersw.plateinfo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GermanRegionPrefixesTest {

    @Autowired
    GermanRegionPrefixes gp;

    @Test
    void init() throws IOException {
        assertEquals("Ulm", gp.getRegionFor("UL"));
    }

    @Test
    void getRegionForExistingCode() throws IOException {
        assertEquals("Ulm", gp.getRegionFor("UL"));
    }

    @Test
    void getRegionForNonExistingCode() throws IOException {
        assertEquals(null, gp.getRegionFor("NONEXISING"));
    }

}