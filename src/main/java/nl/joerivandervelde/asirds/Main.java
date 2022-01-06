package nl.joerivandervelde.asirds;

import nl.joerivandervelde.asirds.depthmap.MakeDepthMap;

import java.io.File;

public class Main {

    /**
     * re-create all graphics used in README.md
     * ....except the first non-animated SIRDS image
     * which is created by taking the first frame from
     * the reference images using Preview 11.0 (Apple Inc.)
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {

        // reference: noisy asirds of moving sphere where radius is scaled to 0.4 with distance and depth to 0.1
        new MakeDepthMap(new File("gifs/a-sphere-r0.4-d0.1.gif"), 0.4, 0.1).go();
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/a-sphere-r0.4-d0.1-noisy.gif"), true, false, false).convert();

        // try out 'calm' sirds image from reference depth map
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/b-sphere-r0.4-d0.1-calm.gif"), false, false, false).convert();

        // make noisy asirds where depth is not distance scaled, only radius
        new MakeDepthMap(new File("gifs/c-sphere-r0.4-d1.0.gif"), 0.4, 1.0).go();
        new AnimatedSIRDS(new File("gifs/c-sphere-r0.4-d1.0.gif"), new File("gifs/c-sphere-r0.4-d1.0-noisy.gif"), true, false, false).convert();

        // make noisy asirds where radius is not distance scaled, only depth
        new MakeDepthMap(new File("gifs/d-sphere-r1.0-d0.1.gif"), 1.0, 0.1).go();
        new AnimatedSIRDS(new File("gifs/d-sphere-r1.0-d0.1.gif"), new File("gifs/d-sphere-r1.0-d0.1-noisy.gif"), true, false, false).convert();

        // use reference depth map, but now color sirds instead of monochrome
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/e-sphere-color-noisy.gif"), true, true, false).convert();

        // use reference depth map, but now low-res instead of normal
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/f-sphere-lowres-noisy.gif"), true, false, true).convert();

    }
}
