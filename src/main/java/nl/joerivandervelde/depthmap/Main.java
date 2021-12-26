package nl.joerivandervelde.depthmap;

import nl.joerivandervelde.asirds.AnimatedSIRDS;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        new MakeDepthMap(new File("gifs/sphere-r0.4-d0.4.gif"), 0.4, 0.4).go();
        new MakeDepthMap(new File("gifs/sphere-r1.0-d0.4.gif"), 1.0, 0.4).go();
        new MakeDepthMap(new File("gifs/sphere-r0.4-d1.0.gif"), 0.4, 1.0).go();
    }
}
