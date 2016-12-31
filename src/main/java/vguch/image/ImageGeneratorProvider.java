package vguch.image;

public class ImageGeneratorProvider {
    private ImageGeneratorProvider() {
    }

    public static ImageGenerator getImageGeneratorFor(String type) {
        switch (type.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return new JpegGenerator();
            case "webp":
                return new WebPGenerator();
            default:
                return null;
        }
    }
}
