package vguch.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import vguch.config.BotConfig;
import vguch.service.ImageService;
import vguch.service.impl.ImageServiceImpl;

public class JpegGenerator implements ImageGenerator {
	
	private ImageService imageService;
	
    public JpegGenerator() {
    	this.imageService = new ImageServiceImpl();
    }

    @Override
    public File generateTextImage(String text) throws ImageGenerationException, IOException {
    	BufferedImage image = imageService.getImageReaderJPG();
		imageService.process(image, text);
		Random r = new Random();
		String newPath = getNewPathJPG(r);
		imageService.writeImage(image, BotConfig.FORMAT_JPG, newPath);
		File file = new File(newPath);
		return file;
    }
    
    private String getNewPathJPG(Random r) {
		return BotConfig.FILE_PATH_NEW + r.nextInt() + ".jpg";
	}
}
