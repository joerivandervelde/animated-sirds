package nl.joerivandervelde.asirds;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception
    {
        // default image convert to noisy sirds
        new AnimatedSIRDS(new File("gifs/sphere-r0.4-d0.4.gif"), new File("gifs/sphere-r0.4-d0.4-noisy.gif"), true).convert();

        // try out 'calm' sirds image
        new AnimatedSIRDS(new File("gifs/sphere-r0.4-d0.4.gif"), new File("gifs/sphere-r0.4-d0.4-calm.gif"), false).convert();

        // noisy image where depth is not distance scaled, only radius
        new AnimatedSIRDS(new File("gifs/sphere-r0.4-d1.0.gif"), new File("gifs/sphere-r0.4-d1.0-noisy.gif"), true).convert();

        // noisy image where radius is not distance scaled, only depth
        new AnimatedSIRDS(new File("gifs/sphere-r1.0-d0.4.gif"), new File("gifs/sphere-r1.0-d0.4-noisy.gif"), true).convert();
    }
}
