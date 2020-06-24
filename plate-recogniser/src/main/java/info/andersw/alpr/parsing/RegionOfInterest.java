package info.andersw.alpr.parsing;

import lombok.Data;

@Data
public class RegionOfInterest {
    private float x;
    private float y;
    private float width;
    private float height;
}
