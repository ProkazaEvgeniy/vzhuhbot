package vguch;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import vguch.image.ImageGenerationException;
import vguch.image.ImageGenerator;
import vguch.image.ImageGeneratorProvider;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class VzhuhBot extends TelegramLongPollingBot {
    private LoadingCache<String, Session> sessions;

    public VzhuhBot() {
        this.sessions = CacheBuilder.newBuilder()
                .expireAfterAccess(10L, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<String, Session>() {
                            @Override
                            public Session load(String s) throws Exception {
                                return new Session();
                            }
                        }
                );
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())
            handleTextMessage(update.getMessage());
    }

    private void handleTextMessage(Message message) {
        String text = message.getText();
        Session session = getSession(message);
        if (session.hasImageGenerator()) {
            try {
                File document = session.generateTextImage(text);
                answerDocument(message, document);
                answerText(message, "");
            } catch (ImageGenerationException e) {
                answerText(message, "");
            }
        } else if (text.startsWith("/")) {
            ImageGenerator imageGenerator = ImageGeneratorProvider
                    .getImageGeneratorFor(text.substring(1));
            if (imageGenerator != null) {
                session.setImageGenerator(imageGenerator);
                answerText(message, "");
            } else answerHelp(message);
        } else answerHelp(message);
    }

    private void answerHelp(Message message) {
        answerText(message, "");
    }

    private void answerText(Message message, String text) {
        try {
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(message.getChatId());
            sendMessage.setText(text);

            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void answerDocument(Message message, File document) {
        try {
            SendDocument sendDocument = new SendDocument();

            sendDocument.setChatId(message.getChatId());
            sendDocument.setNewDocument(document);

            sendDocument(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private Session getSession(Message message) {
        try {
            return sessions.get(
                    String.format("%d:%d",
                            message.getChatId(),
                            message.getFrom().getId()
                    )
            );
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();

        try {
            new TelegramBotsApi().registerBot(new VzhuhBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
