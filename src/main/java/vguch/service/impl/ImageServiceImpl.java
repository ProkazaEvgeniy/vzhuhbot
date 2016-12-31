package vguch.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.lang3.StringUtils;

import com.luciad.imageio.webp.WebPReadParam;

import vguch.config.BotConfig;
import vguch.service.ImageService;

public class ImageServiceImpl implements ImageService {

	@Override
	public BufferedImage getImageReader() throws FileNotFoundException, IOException {
		ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
		WebPReadParam readParam = new WebPReadParam();
		readParam.setBypassFiltering(true);
		reader.setInput(new FileImageInputStream(new File(BotConfig.FILE_PATH)));
		BufferedImage image = reader.read(0, readParam);
		return image;
	}
	
	@Override
	public void writeImage(BufferedImage image, String formatWebp, String newPath) throws IOException{
		ImageIO.write(image, formatWebp, new File(newPath));
	}
	
	@Override
	public BufferedImage process(BufferedImage image, String text) {
		Graphics2D g2d = image.createGraphics();
		Graphics2D g2d_back = image.createGraphics();
		g2d_back.drawImage(image, 0, 0, null);
		g2d_back.setPaint(Color.BLACK);
		g2d_back.setFont(new Font("Impact", Font.PLAIN, 51));
		g2d.drawImage(image, 0, 0, null);
		g2d.setPaint(Color.WHITE);
		g2d.setFont(new Font("Impact", Font.PLAIN, 50));
		int x = 5;
		int y = 270;
		List<String> resFinish = divideText(text);
		drawStringToImage(g2d_back, resFinish, x, y);
		drawStringToImage(g2d, resFinish, x, y);
		g2d_back.dispose();
		g2d.dispose();
		return image;
	}

	private List<String> divideText(String text) {
		String newTextWithoutEnter = charResultNoEnter(text);
		String[] resSplit = newTextWithoutEnter.split(" ");
		List<String> res = new LinkedList<>();
		String resultForRes = "";
		for (int i = 0; i < resSplit.length; i++) {
			resultForRes += resSplit[i] + " ";
			if (resultForRes.length() >= 13) {
				res.add(0, resultForRes.length() > 25 ? StringUtils.abbreviate(resultForRes, 0, 20) : resultForRes);
				resultForRes = "";
			}
		}
		if (!resultForRes.isEmpty()) {
			res.add(0, resultForRes);
		}
		return res;
	}

	int step = 50;

	private void drawStringToImage(Graphics2D g2d, List<String> res, int x, int y) {
		for (int i = res.size() - 1; i >= 0; i--, y += this.step) {
			g2d.drawString(res.get(i), x, y);
		}
	}

	private String charResultNoEnter(String text) {
		char[] chars = text.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '\n') {
				chars[i] = ' ';
			}
		}
		return new String(chars).replaceAll("[\\s]{2,}", " ");
	}
	
}