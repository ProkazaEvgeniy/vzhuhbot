package vguch.image;

import java.io.File;

public interface ImageGenerator {
    File generateTextImage(String text) throws ImageGenerationException;
}
