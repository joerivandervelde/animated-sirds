package nl.joerivandervelde.asirds;

import java.awt.image.WritableRaster;
import java.util.Random;

/**
 * from:
 * Thimbleby et al. "Algorithm for drawing an autostereogram" IEEE 1994
 */
public class Thimbleby {

    // DEBUG
    private static final boolean shiftViz = false;

    /**
     * DPI: Output device has 72 pixels per inch
     * E: Eye separation is assumed to be 2.5 inches
     * mu: Depth of field (fraction of viewing distance)
     * far: Stereo separation according to far plane, Z=O
     * rng: Random number generator for creating pixels
     */
    private static final int DPI = 72;
    private static final int E = (int) Math.round(2.5 * DPI);
    private static final double mu = 1 / 3.0;
    private static final int far = separation(0);
    private static final Random rng = new Random();

    /**
     * Stereo separation according to position Z
     *
     * @param Z
     * @return
     */
    private static int separation(float Z) {
        return (int) Math.round((1 - mu * Z) * E / (2 - mu * Z));
    }

    /**
     * Draw a dot on a pixel raster
     *
     * @param rstr
     * @param atX
     * @param atY
     */
    private static void DrawSquare(Pixel[][] rstr, int atX, int atY,
                                   short color) {
        for (int x = -3; x <= 3; x++)
            for (int y = -3; y <= 3; y++)
                rstr[atX + x][atY + y] = new Pixel(color);
    }

    /**
     * Input: depth map Z[][] with values 0.0 - 1.0
     * Output: random dot autostereogram of same size
     *
     * @param Z
     * @return
     */
    public static Pixel[][] DrawAutoStereogram(float Z[][],
                                             WritableRaster embedIn) {
        int maxX = Z.length;
        int maxY = Z[0].length;
        Pixel[][] out = new Pixel[maxX][maxY];
        for (int y = 0; y < maxY; y++) {
            int[] same = new int[maxX];
            for (int x = 0; x < maxX; x++) {
                same[x] = x;
            }
            for (int x = 0; x < maxX; x++) {
                int s = separation(Z[x][y]);
                int left = x - (s + (s & y & 1)) / 2;
                int right = left + s;
                if (0 <= left && right < maxX) {
                    boolean visible;
                    int t = 1;
                    double zt;
                    do {
                        zt = Z[x][y] + 2 * (2 - mu * Z[x][y]) * t / (mu * E);
                        visible = Z[x - t][y] < zt && Z[x + t][y] < zt;
                        t++;
                    } while (visible && zt < 1);
                    if (visible) {
                        for (int k = same[left]; k != left && k != right; k =
                                same[left]) {
                            if (k < right) {
                                left = k;
                            } else {
                                left = right;
                                right = k;
                            }
                        }
                        same[left] = right;
                    }
                }
            }

            // required for visualization of shift
            int shiftXmin = Integer.MAX_VALUE;
            int shiftXmax = Integer.MIN_VALUE;
            for (int x = maxX - 1; x >= 0; x--) {
                if (same[x] != x) {

                    if(same[x]-x > shiftXmax) { shiftXmax = same[x]-x; }
                    if(same[x]-x < shiftXmin) { shiftXmin = same[x]-x; }
                }
            }

            // TODO based on frame and depth, move pixels / introduce new ??
            Pixel[] pix = new Pixel[maxX];
            for (int x = maxX - 1; x >= 0; x--) {
                if (same[x] == x) {
                    if(shiftViz)
                        pix[x] = new Pixel(new int[]{255, 0, 0});
                    else
                        pix[x] = embedIn == null ? new Pixel((short)rng.nextInt(256)) : new Pixel(embedIn.getPixel(x, y, (int[]) null));

                } else {
                    if(shiftViz)
                        pix[x] = new Pixel((short) (((same[x]-x - shiftXmin) / ((float) shiftXmax - shiftXmin)) * 255));
                    else
                        pix[x] = pix[same[x]];
                }
                out[x][y] = pix[x];
            }

        }
        DrawSquare(out, maxX / 2 - far / 2, maxY * 19 / 20, (short)0);
        DrawSquare(out, maxX / 2 + far / 2, maxY * 19 / 20, (short)0);
        return out;
    }

}
