package vguch.image;

import java.io.File;

public class JpegGenerator implements ImageGenerator {
    public JpegGenerator() {
    }

    @Override
    public File generateTextImage(String text) throws ImageGenerationException {
        throw new UnsupportedOperationException();
    }
}
