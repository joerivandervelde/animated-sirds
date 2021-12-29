package nl.joerivandervelde.asirds.depthmap;

import nl.joerivandervelde.asirds.gif.GifSequenceWriter;
import nl.joerivandervelde.asirds.util.ImageFrame;
import nl.joerivandervelde.asirds.util.Pixel;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import static nl.joerivandervelde.asirds.AnimatedSIRDS.PixelsToIntMatrixRGBA;

public class MakeDepthMap {

    File outputGIF;
    double radiusDistanceScale; // 0-1, sphere radius decreases by this factor from closest to farthest
    double depthDistanceScale; // 0-1, depth value decreases by this factor from closest to farthest

    public MakeDepthMap(File outputGIF, double radiusDistanceScale, double depthDistanceScale){
        this.outputGIF = outputGIF;
        this.radiusDistanceScale = radiusDistanceScale;
        this.depthDistanceScale = depthDistanceScale;
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
        int nrOfFrames = 64;
        int frameDelayMS = 5;
        int xOffset = 200;
        int yOffset = 100;
        int zOffset = -100;
        double orbitRadius = 100d;
        double sphereRadius = 75;

        Coordinate[] coords = new Coordinate[nrOfFrames];
        for(int i = 0; i < nrOfFrames; i ++){
            final double angle = Math.toRadians(((double) i / nrOfFrames) * 360d);
            coords[i] = new Coordinate((Math.cos(angle) * orbitRadius) + xOffset, 0 + yOffset, (Math.sin(angle) * orbitRadius) + zOffset);
        }

        for(Coordinate c : coords)
        {
            // scale sphereRadius based on the Z value, orbitRadius and radiusDistanceScale
            //System.out.println(75 * c.z * orbitRadius * radiusDistanceScale);
            // sphereRadius = 75 at close
            // radiusDistanceScale 0.4 so 30 at far
            // so, x 0.4 + 0 (far) to x 0.4 + 0.6 (close)
            double sphereRadiusScaledByDistance = (sphereRadius * (radiusDistanceScale + (((1.0-radiusDistanceScale)*(c.z/(-orbitRadius*2))))));
            double depthScale = (depthDistanceScale + (((1.0-depthDistanceScale)*(c.z/(-orbitRadius*2)))));
            Pixel[][] sis = drawSphereInFrame(400, 200, c, sphereRadiusScaledByDistance, depthScale);
            int[] sisToInt = PixelsToIntMatrixRGBA(sis);

            // todo: code dup
            DataBufferInt buffer = new DataBufferInt(sisToInt, sisToInt.length);
            int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
            WritableRaster rasternew = Raster.createPackedRaster(buffer, sis.length, sis[0].length, sis.length, bandMasks, null);
            ColorModel cm = ColorModel.getRGBdefault();
            BufferedImage bi = new BufferedImage(cm, rasternew, cm.isAlphaPremultiplied(), null);
            ImageFrame imgfrnew = new ImageFrame(bi);

            if(writer == null)
            {
                System.out.println("creating writer...");
                ImageOutputStream fos = new FileImageOutputStream(outputGIF);
                writer = new GifSequenceWriter(fos, imgfrnew.getImage().getType(), frameDelayMS, true);
            }
            writer.writeToSequence(imgfrnew.getImage());
        }
        writer.close();
    }

    // todo: size and color based on distance because depth map
    public Pixel[][] drawSphereInFrame(int frameWidth, int frameHeight, Coordinate sphereCoords, double radius, double depthScale)
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
                // start Z at 0 since no point in rendering the 'back half' pf the sphere
                for(double z = 0; z < radius; z++ ){
                    if(Math.sqrt((x * x) + (y * y) + (z * z)) <= radius){
                        short zScaled = (short) (z * (255.0/radius));
                        out[(int)x+sphereCoords.x][(int)y+sphereCoords.y] = new Pixel((short)(zScaled * depthScale));
                    }
                }
            }
        }
        return out;
    }

}
