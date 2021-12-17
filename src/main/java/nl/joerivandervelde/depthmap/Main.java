package nl.joerivandervelde.depthmap;

import nl.joerivandervelde.asirds.AnimatedSIRDS;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File outputGIF = new File("sphere.gif");
        new MakeDepthMap(outputGIF).go();
    }
}
