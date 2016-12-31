package vguch.image;

import vguch.config.BotConfig;

public class JpegGenerator extends ImageGeneratorAdapter {
    public JpegGenerator() {
        super(BotConfig.FORMAT_JPG, BotConfig.MIMETYPE_JPG, BotConfig.FILE_PATH_JPG);
    }
}
