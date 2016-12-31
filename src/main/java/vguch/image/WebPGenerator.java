package vguch.image;

import vguch.config.BotConfig;

public class WebPGenerator extends ImageGeneratorAdapter {
    public WebPGenerator() {
        super(BotConfig.FORMAT_WEBP, BotConfig.MIMETYPE_WEBP, BotConfig.FILE_PATH_WEBP);
    }
}
