package info.andersw.alpr.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClassificationResultParserTest {


    /* EXAMPLE
    Total Time to process image: 18619.6ms.
    {"version":2,"data_type":"alpr_results","epoch_time":1592745193749,"img_width":3456,"img_height":4608,"processing_time_ms":18600.687500,"regions_of_interest":[{"x":0,"y":0,"width":3456,"height":4608}],"results":[{"plate":"HPLS71","confidence":92.678444,"matches_template":0,"plate_index":7,"region":"","region_confidence":0,"processing_time_ms":28.238762,"requested_topn":3,"coordinates":[{"x":1572,"y":1764},{"x":2050,"y":1764},{"x":2050,"y":1890},{"x":1572,"y":1890}],"candidates":[{"plate":"HPLS71","confidence":92.678444,"matches_template":0},{"plate":"HPL571","confidence":79.370941,"matches_template":0}]}]}
     */

    @Test
    public void constructorWorks() throws JsonProcessingException {
        ArrayList<String> mock = new ArrayList<>();
        mock.add("Total Time to process image: 18619.6ms.");
        mock.add("{\"version\":2,\"data_type\":\"alpr_results\",\"epoch_time\":1592745193749,\"img_width\":3456,\"img_height\":4608,\"processing_time_ms\":18600.687500,\"regions_of_interest\":[{\"x\":0,\"y\":0,\"width\":3456,\"height\":4608}],\"results\":[{\"plate\":\"HPLS71\",\"confidence\":92.678444,\"matches_template\":0,\"plate_index\":7,\"region\":\"\",\"region_confidence\":0,\"processing_time_ms\":28.238762,\"requested_topn\":3,\"coordinates\":[{\"x\":1572,\"y\":1764},{\"x\":2050,\"y\":1764},{\"x\":2050,\"y\":1890},{\"x\":1572,\"y\":1890}],\"candidates\":[{\"plate\":\"HPLS71\",\"confidence\":92.678444,\"matches_template\":0},{\"plate\":\"HPL571\",\"confidence\":79.370941,\"matches_template\":0}]}]}");
        ClassificationResultParser crp = new ClassificationResultParser(mock);
    }

    @Test
    public void noResults() throws JsonProcessingException {
        String testJSON = "{\"version\":2,\"data_type\":\"alpr_results\",\"epoch_time\":1592749316417,\"img_width\":640,\"img_height\":640,\"processing_time_ms\":173.163239,\"regions_of_interest\":[{\"x\":0,\"y\":0,\"width\":640,\"height\":640}],\"results\":[]}";
        ClassificationResultParser crp = new ClassificationResultParser(testJSON);
        assertEquals(0, crp.nrPlatesFound());
    }

    @Test
    public void topPlates() throws JsonProcessingException {
        String testJSON = "{\"version\":2,\"data_type\":\"alpr_results\",\"epoch_time\":1592749149038,\"img_width\":3456,\"img_height\":4608,\"processing_time_ms\":10690.906250,\"regions_of_interest\":[{\"x\":0,\"y\":0,\"width\":3456,\"height\":4608}],\"results\":[{\"plate\":\"IMFS2187\",\"confidence\":90.879524,\"matches_template\":0,\"plate_index\":0,\"region\":\"\",\"region_confidence\":0,\"processing_time_ms\":86.146370,\"requested_topn\":3,\"coordinates\":[{\"x\":2463,\"y\":1634},{\"x\":2741,\"y\":1609},{\"x\":2750,\"y\":1686},{\"x\":2472,\"y\":1712}],\"candidates\":[{\"plate\":\"IMFS2187\",\"confidence\":90.879524,\"matches_template\":0},{\"plate\":\"MFS2187\",\"confidence\":87.219269,\"matches_template\":0},{\"plate\":\"0MFS2187\",\"confidence\":86.249229,\"matches_template\":0}]}]}";
        ClassificationResultParser crp = new ClassificationResultParser(testJSON);
        assertEquals(1, crp.nrPlatesFound());
        assertEquals("IMFS2187", crp.getTopPlates().get(0));
    }


}