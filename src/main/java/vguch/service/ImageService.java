package vguch.service;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {

	BufferedImage getImageReaderWebP() throws FileNotFoundException, IOException;
	BufferedImage getImageReaderJPG() throws FileNotFoundException, IOException;
	void writeImage(BufferedImage image, String format, String newPath) throws IOException;
	BufferedImage process(BufferedImage image, String text);
}
