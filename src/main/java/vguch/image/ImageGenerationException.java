package vguch.image;

public class ImageGenerationException extends Exception {
    public ImageGenerationException() {
    }

    public ImageGenerationException(String message) {
        super(message);
    }

    public ImageGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageGenerationException(Throwable cause) {
        super(cause);
    }

    public ImageGenerationException(String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
