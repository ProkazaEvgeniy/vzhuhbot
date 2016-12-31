package vguch.service;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {

	BufferedImage getImageReader() throws FileNotFoundException, IOException;
	void writeImage(BufferedImage image, String formatWebp, String newPath) throws IOException;
	BufferedImage process(BufferedImage image, String text);
}
