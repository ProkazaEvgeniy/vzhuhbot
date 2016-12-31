package vguch.image;

public class ImageGeneratorProvider {
    private ImageGeneratorProvider() {
    }

    public static ImageGenerator getImageGeneratorFor(String type) {
        switch (type.toLowerCase()) {
            case "jpeg":
            case "jpg":
                new JpegGenerator();
                break;
            case "webp":
                new WebPGenerator();
        }

        return null;
    }
}
