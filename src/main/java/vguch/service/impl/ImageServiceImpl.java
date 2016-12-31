package vguch.service.impl;

import com.luciad.imageio.webp.WebPReadParam;
import org.apache.commons.lang3.StringUtils;
import vguch.config.BotConfig;
import vguch.service.ImageService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ImageServiceImpl implements ImageService {

    public ImageServiceImpl() {
    }

    public BufferedImage getImageReader(String type, String path) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByMIMEType(type).next();
        WebPReadParam readParam = new WebPReadParam();
        readParam.setBypassFiltering(true);
        reader.setInput(new FileImageInputStream(new File(path)));
        return reader.read(0, readParam);
    }

    @Override
    public void writeImage(BufferedImage image, String format, File file) throws IOException {
        ImageIO.write(image, format, file);
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
        for (String aResSplit : resSplit) {
            resultForRes += aResSplit + " ";
            if (resultForRes.length() >= 13) {
                res.add(0, resultForRes.length() > 25
                        ? StringUtils.abbreviate(resultForRes, 0, 20)
                        : resultForRes);
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
