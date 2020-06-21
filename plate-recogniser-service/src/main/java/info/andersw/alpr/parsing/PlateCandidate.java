package info.andersw.alpr.parsing;

import lombok.Data;

@Data
public class PlateCandidate {

    private String plate;
    private float confidence;
    private float matches_template;

}