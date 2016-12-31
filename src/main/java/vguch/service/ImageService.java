package vguch.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ImageService {

	BufferedImage getImageReader(String type, String path) throws IOException;
	void writeImage(BufferedImage image, String format, File file) throws IOException;
	BufferedImage process(BufferedImage image, String text);
}
