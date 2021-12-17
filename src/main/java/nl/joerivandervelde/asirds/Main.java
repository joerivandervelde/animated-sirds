package nl.joerivandervelde.asirds;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        File inputGIF = new File("gifs/UnawareGregariousIriomotecat-size_restricted.gif");
        File outputGIF = new File("outputgifs/UnawareGregariousIriomotecat-size_restricted-output.gif");
        AnimatedSIRDS asirds = new AnimatedSIRDS(inputGIF, outputGIF);
        asirds.convert();
    }
}
