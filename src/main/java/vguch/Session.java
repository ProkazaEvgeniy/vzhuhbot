package vguch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import vguch.image.ImageGenerationException;
import vguch.image.ImageGenerator;

public class Session {
    private ImageGenerator imageGenerator;

    public File generateTextImage(String text) throws ImageGenerationException, FileNotFoundException, IOException {
        File document = imageGenerator.generateTextImage(text);
        imageGenerator = null;
        return document;
    }

    public boolean hasImageGenerator() {
        return imageGenerator != null;
    }
    
    public void setImageGenerator(ImageGenerator imageGenerator) {
        this.imageGenerator = imageGenerator;
    }
    
}
