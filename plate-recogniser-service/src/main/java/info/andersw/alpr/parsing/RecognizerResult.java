package info.andersw.alpr.parsing;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RecognizerResult {
    private String plate;
    private float confidence;
    private float matches_template;
    private float plate_index;
    private String region;
    private float region_confidence;
    private float processing_time_ms;
    private float requested_topn;
    ArrayList< PlateCoordinate > coordinates = new ArrayList <> ();
    ArrayList < PlateCandidate > candidates = new ArrayList <> ();
}