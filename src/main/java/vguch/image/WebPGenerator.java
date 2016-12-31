package vguch.image;

import java.io.File;

public class WebPGenerator implements ImageGenerator {
    public WebPGenerator() {
    }

    @Override
    public File generateTextImage(String text) throws ImageGenerationException {
        throw new UnsupportedOperationException();
    }
}
