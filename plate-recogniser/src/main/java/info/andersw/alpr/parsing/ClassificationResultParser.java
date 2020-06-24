package info.andersw.alpr.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Pattern;

@CommonsLog
public class ClassificationResultParser {

    /* EXAMPLE
    Total Time to process image: 18619.6ms.
    {"version":2,"data_type":"alpr_results","epoch_time":1592745193749,"img_width":3456,"img_height":4608,"processing_time_ms":18600.687500,"regions_of_interest":[{"x":0,"y":0,"width":3456,"height":4608}],"results":[{"plate":"HPLS71","confidence":92.678444,"matches_template":0,"plate_index":7,"region":"","region_confidence":0,"processing_time_ms":28.238762,"requested_topn":3,"coordinates":[{"x":1572,"y":1764},{"x":2050,"y":1764},{"x":2050,"y":1890},{"x":1572,"y":1890}],"candidates":[{"plate":"HPLS71","confidence":92.678444,"matches_template":0},{"plate":"HPL571","confidence":79.370941,"matches_template":0}]}]}
     */

    @Getter
    private AlprResult parsedResult;

    public ClassificationResultParser(ArrayList<String> unprocessedResult) throws JsonProcessingException {

        assert unprocessedResult.size() == 2;
        log.info(unprocessedResult.get(0));
        parsedResult = new ObjectMapper().readValue(unprocessedResult.get(1), AlprResult.class);
    }

    public ClassificationResultParser(String alprJSON) throws JsonProcessingException {
        assert alprJSON != null;
        assert alprJSON.length() > 0;
        parsedResult = new ObjectMapper().readValue(alprJSON, AlprResult.class);
    }

    public int nrPlatesFound() {
        return parsedResult.results.size();
    }

    /*
    * Returns an array of the top plate candidate for each result.
    * ["ABC123", "MGO1212",...]
     */
    public ArrayList<String> getTopPlates() {
        ArrayList<String> result = new ArrayList<>();
        for (RecognizerResult recognizerResult : parsedResult.results)
            result.add(recognizerResult.getCandidates().get(0).getPlate());
        return result;
    }
}
