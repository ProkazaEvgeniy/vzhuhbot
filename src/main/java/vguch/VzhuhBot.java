package vguch;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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
import vguch.image.JpegGenerator;
import vguch.image.WebPGenerator;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class VzhuhBot extends TelegramLongPollingBot {
    private Cache<String, Session> sessions;

    public VzhuhBot() {
        this.sessions = CacheBuilder.newBuilder()
                .expireAfterAccess(10L, TimeUnit.MINUTES)
                .build();
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
            } catch (ImageGenerationException e) {
                answerText(message, "");
            }
        } else if (text.startsWith("/")) {
            switch (text.substring(1).toLowerCase()) {
                case "countusers":
                case "allusers2012":
                case "jpg":
                    session.setImageGenerator(new JpegGenerator());
                    break;
                case "webp":
                    session.setImageGenerator(new WebPGenerator());
                    break;
                default:
                    answerHelp(message);
                    return;
            }
            answerText(message, "");
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
                    ),
                    new Callable<Session>() {
                        @Override
                        public Session call() throws Exception {
                            return new Session();
                        }
                    }
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
