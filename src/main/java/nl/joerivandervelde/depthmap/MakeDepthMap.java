package nl.joerivandervelde.depthmap;

import nl.joerivandervelde.asirds.GifSequenceWriter;
import nl.joerivandervelde.asirds.ImageFrame;
import nl.joerivandervelde.asirds.Pixel;
import nl.joerivandervelde.asirds.Thimbleby;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import static nl.joerivandervelde.asirds.AnimatedSIRDS.PixelsToIntMatrix;

public class MakeDepthMap {

    File outputGIF;

    public MakeDepthMap(File outputGIF){
        this.outputGIF = outputGIF;
    }

    public void go() throws IOException {

        Pixel[][] sis = makeSphere(150);
        int[] sisToInt = PixelsToIntMatrix(sis);

        // todo: code dup
        DataBufferInt buffer = new DataBufferInt(sisToInt, sisToInt.length);
        int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
        WritableRaster rasternew = Raster.createPackedRaster(buffer, sis.length, sis[0].length, sis.length, bandMasks, null);
        System.out.println("rasternew: " + rasternew);

        ColorModel cm = ColorModel.getRGBdefault();
        BufferedImage bi = new BufferedImage(cm, rasternew, cm.isAlphaPremultiplied(), null);
        ImageFrame imgfrnew = new ImageFrame(bi);



        ImageFrame[] outf = new ImageFrame[1];
        outf[0] = imgfrnew;
        ImageOutputStream fos = new FileImageOutputStream(outputGIF);
        GifSequenceWriter writer =
                new GifSequenceWriter(fos, outf[0].getImage().getType(), outf[0].getDelay(), true);
        writer.writeToSequence(imgfrnew.getImage());
        writer.close();
    }

    // todo: size and color based on distance because depth map
    public Pixel[][] makeSphere(int radius){
        Pixel[][] out = new Pixel[radius*2][radius*2];
        // start all black
        for(int i=0;i<out.length;i++){
            for(int j=0;j<out[i].length;j++) {
                out[i][j] = new Pixel((short)0);
            }
        }
        //draw sphere
        for(double X = -radius; X < radius; X++ )
            for(double Y = -radius; Y < radius; Y++ )
                for(double Z = -radius; Z < radius; Z++ )
                    if(Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius)
                        out[(int) X+radius][(int)Y+radius] = new Pixel((short)Z);
        return out;
    }

}
