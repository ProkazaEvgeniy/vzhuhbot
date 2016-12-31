package vguch.config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import vguch.service.FileService;
import vguch.service.ImageService;
import vguch.service.impl.FileServiceImpl;
import vguch.service.impl.ImageServiceImpl;

public class VzhuhBot extends TelegramLongPollingBot {

	private Cache<Integer, Update> cache;
	private String formatWebp = "webp";
	private String formatJpg = "jpg";

	public VzhuhBot() {
		this.cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
	}

	public String getBotUsername() {
		return BotConfig.USERNAMEMYPROJECT;
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text

		if (update.hasMessage() && update.getMessage().hasText()) {
			SendMessage messageDefault = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("Please specify what format you want to create (Пожалуйста укажите какой формат вы хотите создать)");
			SendMessage messageWebP = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("New format (Создать в формате) - /WebP");
			SendMessage messageJPG = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("New format (Создать в формате) - /jpg");
			SendMessage messageOK = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("OK, write text (ОК, пишите текст)");

			try {
				FileService fileService = new FileServiceImpl();
				ImageService imageService = new ImageServiceImpl();
				String id = update.getMessage().getFrom().getId() + "";
				String firstName = update.getMessage().getFrom().getFirstName();
				String lastName = update.getMessage().getFrom().getLastName();
				String userName = update.getMessage().getFrom().getUserName();
				String newText = getNewText(id, firstName, lastName);
				String resText = fileService.findTextToChar(fileService.read(BotConfig.FILE_PATH_USERS), '{', '}');
				if (update.getMessage().getText().equals("/countusers")) {
					String countUsers = fileService.readUsers(BotConfig.FILE_PATH_USERS);
					SendMessage messageResUsers = new SendMessage()
							.setChatId(update.getMessage().getChatId())
							.setText("The number of registered users = " + countUsers);
					sendMessage(messageResUsers);
				} else if (update.getMessage().getText().equals("/allusers2012")) {
					sendDocument(BotConfig.FILE_PATH_USERS, messageDefault.getChatId());
				} else if (update.getMessage().getText().equals("/start")) {
					cache.put(update.getMessage().getFrom().getId(), update);
					defaultSendMessage(messageDefault, messageWebP, messageJPG);
				} else if (update.getMessage().getText().equals("/WebP")) {
					sendMessage(messageOK);
					if (!(resText.contains(id))) {
						fileService.update(BotConfig.FILE_PATH_USERS, newText);
					}
					BufferedImage image = imageService.getImageReaderWebP();
					SendMessage messageToInput = new SendMessage()
							.setChatId(update.getMessage().getChatId())
							.setText(update.getMessage().getText());
					imageService.process(image, messageToInput.getText());
					Random r = new Random();
					String newPath = getNewPath(r, id, firstName, lastName, userName);
					imageService.writeImage(image, formatWebp, newPath);
					sendDocument(newPath, messageToInput.getChatId());
					defaultSendMessage(messageDefault, messageWebP, messageJPG);
					fileService.delete(newPath);
				} else if (update.getMessage().getText().equals("/jpg")) {

				}
			} catch (IOException | TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getBotToken() {
		return BotConfig.TOKENMYPROJECT;
	}

	public void sendDocument(String filePath, String chatId) {
		SendDocument sendDocumentRequest = new SendDocument();
		sendDocumentRequest.setChatId(chatId);
		sendDocumentRequest.setNewDocument(new File(filePath));
		try {
			sendDocument(sendDocumentRequest);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private String getNewPath(Random r, String id, String firstName, String lastName, String userName) {
		return BotConfig.FILE_PATH_NEW + r.nextInt() + "id=" + id + "$From=" + firstName + "_" + lastName + "_"
				+ userName + ".webp";
	}

	private String getNewText(String id, String firstName, String lastName) {
		return "{" + id + "}" + " = " + firstName + "_" + lastName;
	}

	private void defaultSendMessage(SendMessage messageDefault, SendMessage messageWebP, SendMessage messageJPG)
			throws TelegramApiException {
		sendMessage(messageDefault);
		sendMessage(messageWebP);
		sendMessage(messageJPG);
	}
}
