package vguch.image;

import vguch.config.BotConfig;
import vguch.service.ImageService;
import vguch.service.impl.ImageServiceImpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public abstract class ImageGeneratorAdapter implements ImageGenerator {
    private String format, type, path;
    private ImageService imageService;

    public ImageGeneratorAdapter(String format, String type, String path) {
        this.format = format;
        this.type = type;
        this.path = path;
        this.imageService = new ImageServiceImpl();
    }

    @Override
    public File generateTextImage(String text) throws ImageGenerationException {
        try {
            File ret = generateFile();

            BufferedImage image = imageService.getImageReader(type, path);
            imageService.process(image, text);
            imageService.writeImage(image, format, ret);

            return ret;
        } catch (IOException e) {
            throw new ImageGenerationException(e);
        }
    }

    private File generateFile() {
        Random random = new Random();
        File ret = null;

        while (ret == null || ret.exists())
            ret = new File(
                    String.format("%s%d.%s",
                            BotConfig.FILE_PATH_NEW,
                            random.nextInt(),
                            format
                    )
            );

        return ret;
    }
}
