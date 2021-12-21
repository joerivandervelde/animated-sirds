package nl.joerivandervelde.depthmap;

import nl.joerivandervelde.asirds.GifSequenceWriter;
import nl.joerivandervelde.asirds.ImageFrame;
import nl.joerivandervelde.asirds.Pixel;
import nl.joerivandervelde.asirds.Thimbleby;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static nl.joerivandervelde.asirds.AnimatedSIRDS.PixelsToIntMatrix;

public class MakeDepthMap {

    File outputGIF;

    public MakeDepthMap(File outputGIF){
        this.outputGIF = outputGIF;
    }

    /**
     * Viewport: h 200 x w 400
     * sphere: radius 75 (150 diam)
     * Z 0 is right in front of 'camera', X/Y 0 is top-left corner
     * moving on circular trajectory on X and Z axis with 50 pixel gap
     * trajectory radius is 100, leaving borders of 25 pixels
     * coordinates in this scenario:
     * x/y/z: 300/100/-100 (sphere starts on right-hand side at half distance)
     * x/y/z: 200/100/0 (sphere towards middle, close to camera)
     * x/y/z: 100/100/-100 (sphere towards left-hand side at half distance)
     * x/y/z: 200/100/-200 (sphere towards middle, far away)
     * return to starting position and repeat
     */
    public void go() throws IOException {
        GifSequenceWriter writer = null;

        int xOffset = 200;
        int yOffset = 100;
        int zOffset = -100;
        double radius = 100d;
        int resolution = 4;
        Coordinate[] coords = new Coordinate[resolution];
        for(int i = 0; i < resolution; i ++){
            final double angle = Math.toRadians(((double) i / resolution) * 360d);
            coords[i] = new Coordinate((Math.cos(angle) * radius) + xOffset, 0 + yOffset, (Math.sin(angle) * radius) + zOffset);
        }

        System.out.println(Arrays.toString(coords));

        for(Coordinate c : coords)
        {
            Pixel[][] sis = makeSphere(400, 200, c, 75);
            int[] sisToInt = PixelsToIntMatrix(sis);

            // todo: code dup
            DataBufferInt buffer = new DataBufferInt(sisToInt, sisToInt.length);
            int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
            WritableRaster rasternew = Raster.createPackedRaster(buffer, sis.length, sis[0].length, sis.length, bandMasks, null);
            System.out.println("rasternew: " + rasternew);
            ColorModel cm = ColorModel.getRGBdefault();
            BufferedImage bi = new BufferedImage(cm, rasternew, cm.isAlphaPremultiplied(), null);
            ImageFrame imgfrnew = new ImageFrame(bi);

            if(writer == null)
            {
                System.out.println("creating writer...");
                ImageOutputStream fos = new FileImageOutputStream(outputGIF);
                writer = new GifSequenceWriter(fos, imgfrnew.getImage().getType(), imgfrnew.getDelay(), true);
            }
            writer.writeToSequence(imgfrnew.getImage());
        }
        writer.close();
    }

    // todo: size and color based on distance because depth map
    public Pixel[][] makeSphere(int frameWidth, int frameHeight, Coordinate sphereCoords, int radius)
    {
        Pixel[][] out = new Pixel[frameWidth][frameHeight];

        // fill frame with black pixels
        for(int i=0;i<frameWidth;i++){
            for(int j=0;j<frameHeight;j++) {
                out[i][j] = new Pixel((short)0);
            }
        }
        //draw 3D sphere in 2D
        for(double x = -radius; x < radius; x++ ){
            for(double y = -radius; y < radius; y++ ){
                for(double z = -radius; z < radius; z++ ){
                    if(Math.sqrt((x * x) + (y * y) + (z * z)) <= radius){
                        out[(int) x+radius][(int)y+radius] = new Pixel((short)z);
                    }
                }
            }
        }

        return out;
    }

}
