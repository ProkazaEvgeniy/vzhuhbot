package vguch.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageGenerator {
    File generateTextImage(String text) throws ImageGenerationException, FileNotFoundException, IOException;
}
