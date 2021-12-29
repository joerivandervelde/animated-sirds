package nl.joerivandervelde.asirds;

import nl.joerivandervelde.asirds.depthmap.MakeDepthMap;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception
    {

        // make noisy asirds of moving sphere where radius is scaled to 0.4 with distance and depth to 0.1
        new MakeDepthMap(new File("gifs/a-sphere-r0.4-d0.1.gif"), 0.4, 0.1).go();
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/a-sphere-r0.4-d0.1-noisy.gif"), true).convert();

        // try out 'calm' sirds image from same depth map
        new AnimatedSIRDS(new File("gifs/a-sphere-r0.4-d0.1.gif"), new File("gifs/b-sphere-r0.4-d0.1-calm.gif"), false).convert();

        // make noisy asirds where depth is not distance scaled, only radius
        new MakeDepthMap(new File("gifs/c-sphere-r0.4-d1.0.gif"), 0.4, 1.0).go();
        new AnimatedSIRDS(new File("gifs/c-sphere-r0.4-d1.0.gif"), new File("gifs/c-sphere-r0.4-d1.0-noisy.gif"), true).convert();

        // make noisy asirds where radius is not distance scaled, only depth
        new MakeDepthMap(new File("gifs/d-sphere-r1.0-d0.1.gif"), 1.0, 0.1).go();
        new AnimatedSIRDS(new File("gifs/d-sphere-r1.0-d0.1.gif"), new File("gifs/d-sphere-r1.0-d0.1-noisy.gif"), true).convert();

    }
}
