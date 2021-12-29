package nl.joerivandervelde.asirds.util;

public class Pixel {
    public short R;
    public short G;
    public short B;

    public Pixel(short grayVal)
    {
        this.R = grayVal;
        this.G = grayVal;
        this.B = grayVal;
    }

    public Pixel (int[] rgb)
    {
        this.R = (short)rgb[0];
        this.G = (short)rgb[1];
        this.B = (short)rgb[2];
    }

    public Pixel(short R, short G, short B)
    {
        this.R = R;
        this.G = G;
        this.B = B;
    }
}
