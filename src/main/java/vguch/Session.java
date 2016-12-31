package vguch;

import vguch.image.ImageGenerationException;
import vguch.image.ImageGenerator;
import vguch.service.AdminService;

import java.io.File;

public class Session {
    private ImageGenerator imageGenerator;
    private AdminService adminService;

    public File generateTextImage(String text) throws ImageGenerationException {
        File image = imageGenerator.generateTextImage(text);
        imageGenerator = null;
        return image;
    }

    public boolean hasImageGenerator() {
        return imageGenerator != null;
    }

    public void setImageGenerator(ImageGenerator imageGenerator) {
        this.imageGenerator = imageGenerator;
    }
}
