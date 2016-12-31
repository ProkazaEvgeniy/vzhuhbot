package vguch.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import vguch.config.BotConfig;
import vguch.service.ImageService;
import vguch.service.impl.ImageServiceImpl;

public class WebPGenerator implements ImageGenerator {

	private ImageService imageService;

	public WebPGenerator() {
		this.imageService = new ImageServiceImpl();
	}

	@Override
	public File generateTextImage(String text) throws ImageGenerationException, FileNotFoundException, IOException {
		BufferedImage image = imageService.getImageReaderWebP();
		imageService.process(image, text);
		Random r = new Random();
		String newPath = getNewPathWebP(r);
		imageService.writeImage(image, BotConfig.FORMAT_WEBP, newPath);
		File file = new File(newPath);
		return file;
	}

	private String getNewPathWebP(Random r) {
		return BotConfig.FILE_PATH_NEW + r.nextInt() + ".webp";
	}
}
