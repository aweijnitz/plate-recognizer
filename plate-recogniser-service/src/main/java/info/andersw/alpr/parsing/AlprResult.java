package info.andersw.alpr.parsing;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AlprResult {
    private float version;
    private String data_type;
    private float epoch_time;
    private float img_width;
    private float img_height;
    private float processing_time_ms;
    ArrayList< RegionOfInterest > regions_of_interest = new ArrayList <> ();
    ArrayList < RecognizerResult > results = new ArrayList <> ();

}