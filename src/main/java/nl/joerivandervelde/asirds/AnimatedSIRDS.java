package nl.joerivandervelde.asirds;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

public class AnimatedSIRDS {

    File input;
    File output;


    public AnimatedSIRDS(File input, File output)
    {
        this.input = input;
        this.output = output;
        if(output.exists())
        {
            output.delete();
        }
    }

    public void convert() throws Exception {
        System.out.println("load input gif...");
        FileInputStream fis = new FileInputStream(input);
        ImageFrame[] imgf = ReadGIF.readGif(fis);
        System.out.println("INFO: read gif with " + imgf.length + " frames, type: " + imgf[0].getImage().getType() + ", delay: " + imgf[0].getDelay() + ", height: " + imgf[0].getHeight() + ", width: " + imgf[0].getWidth() + ", disposal: " + imgf[0].getDisposal());

        System.out.println("random img for embedding...");
        BufferedImage ff = imgf[0].getImage();
        BufferedImage embedIn = rndImg(ff.getWidth(), ff.getHeight());


        System.out.println("set up gif sequence output writer...");
        ImageOutputStream fos = new FileImageOutputStream(output);
        GifSequenceWriter writer =
                new GifSequenceWriter(fos, imgf[0].getImage().getType(), imgf[0].getDelay(), true);


        System.out.println("transform to sirds...");
        for(int f=0; f<imgf.length; f++)
        {
            BufferedImage nextImage = imgf[f].getImage();
            // load depth map PNG
            WritableRaster raster = nextImage.getRaster();
            int depthMin = Integer.MAX_VALUE;
            int depthMax = Integer.MIN_VALUE;
            int[][][] img = new int[raster.getWidth()][raster.getHeight()][3];
            for (int i = 0; i < raster.getWidth(); i++) {
                for (int j = 0; j < raster.getHeight(); j++) {
                    img[i][j] = raster.getPixel(i, j, (int[]) null);

                    if (!(img[i][j][0] == img[i][j][1] &&
                            img[i][j][1] == img[i][j][2])) {
                        //System.out.println("pixel not greyscale: " + img[i][j][0] + ", " + img[i][j][1] + ", " + img[i][j][2]);
                    }
                    if (img[i][j][0] > depthMax) {
                        depthMax = img[i][j][0];
                    }
                    if (img[i][j][0] < depthMin) {
                        depthMin = img[i][j][0];
                    }
                }
            }
            System.out.println("frame = " + f);
            System.out.println("depthMax = " + depthMax);
            System.out.println("depthMin = " + depthMin);

            // scale depth map to 0-1
            float[][] Z = new float[raster.getWidth()][raster.getHeight()];
            for (int i = 0; i < raster.getWidth(); i++) {
                for (int j = 0; j < raster.getHeight(); j++) {

                    // lineair interpolation
                    Z[i][j] = (img[i][j][0] - depthMin) /
                            (float) (depthMax - depthMin);
                }
            }
            Pixel[][] sis = Thimbleby.DrawAutoStereogram(Z, embedIn.getRaster());
            int[] sisToInt = PixelsToIntMatrix(sis);

            DataBufferInt buffer = new DataBufferInt(sisToInt, sisToInt.length);
            int[] bandMasks = {0xFF0000, 0xFF00, 0xFF, 0xFF000000}; // ARGB (yes, ARGB, as the masks are R, G, B, A always) order
            WritableRaster rasternew = Raster.createPackedRaster(buffer, raster.getWidth(), raster.getHeight(), raster.getWidth(), bandMasks, null);
            System.out.println("rasternew: " + rasternew);

            ColorModel cm = ColorModel.getRGBdefault();
            BufferedImage bi = new BufferedImage(cm, rasternew, cm.isAlphaPremultiplied(), null);
            ImageFrame imgfrnew = new ImageFrame(bi);
            writer.writeToSequence(imgfrnew.getImage());


        }

        writer.close();
        fos.close();

    }

    public static int[] PixelsToIntMatrix(Pixel[][] in)
    {
        System.out.println("in.length = " + in.length);
        System.out.println("in[0].length = " + in[0].length);
        System.out.println("int[] size = " + in.length * in[0].length);

        int[] out = new int[in.length * in[0].length];
        for(int i=0; i<in.length; i++)
        {
            for(int j=0; j<in[i].length ; j++)
            {
                out[(j*in.length)+i] = 0xff000000 | (in[i][j].R & 0xff) << 16 | (in[i][j].G & 0xff) << 8 | (in[i][j].B & 0xff);
            }
        }

        return out;
    }

    public BufferedImage rndImg(int w, int h)
    {
        Random rng = new Random();
        BufferedImage out = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Pixel[][] rd = new Pixel[w][h];
        for(int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int val = rng.nextInt(256);
                out.setRGB(x, y, new Color(val, val, val).getRGB());
            }
        }
        return out;
    }

}
