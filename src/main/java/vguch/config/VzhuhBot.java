package vguch.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.api.methods.send.SendDocument;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.luciad.imageio.webp.WebPReadParam;

import vguch.service.FileService;
import vguch.service.impl.FileServiceImpl;

public class VzhuhBot extends TelegramLongPollingBot {

	private Cache<Integer, Update> cache;

	public VzhuhBot() {
		this.cache = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();
	}

	public String getBotUsername() {
		return BotConfig.USERNAMEMYPROJECT;
	}

	public void onUpdateReceived(Update update) {
		// We check if the update has a message and the message has text

		if (update.hasMessage() && update.getMessage().hasText()) {
			SendMessage messageDefault = new SendMessage().setChatId(update.getMessage().getChatId()).setText(
					"Please specify what format you want to create (Пожалуйста укажите какой формат вы хотите создать)");
			SendMessage messageWebP = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("New format (Создать в формате) - /WebP");
			SendMessage messageJPG = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("New format (Создать в формате) - /jpg");
			SendMessage messageOK = new SendMessage().setChatId(update.getMessage().getChatId())
					.setText("OK, write text (ОК, пиши текст)");
			
			
			try {
				FileService fileService = new FileServiceImpl();
				if (update.getMessage().getText().equals("/countusers")) {
					String countUsers = fileService.readUsers(BotConfig.FILE_PATH_USERS);
					SendMessage messageResUsers = new SendMessage().setChatId(update.getMessage().getChatId())
							.setText("The number of registered users = " + countUsers);
					sendMessage(messageResUsers);
				} else if (update.getMessage().getText().equals("/allusers2012")) {
					sendDocument(BotConfig.FILE_PATH_USERS, messageDefault.getChatId());
				} else if (update.getMessage().getText().equals("/start")) {
					cache.put(update.getMessage().getFrom().getId(), update);
					sendMessage(messageDefault);
					sendMessage(messageWebP);
					sendMessage(messageJPG);
				} else if (update.getMessage().getText().equals("/WebP")) {
					sendMessage(messageOK);
					String id = update.getMessage().getFrom().getId() + "";
					String firstName = update.getMessage().getFrom().getFirstName();
					String lastName = update.getMessage().getFrom().getLastName();
					String userName = update.getMessage().getFrom().getUserName();
					String newText = "{" + id + "}" + " = " + firstName + "_" + lastName;
					String resText = fileService.findTextToChar(fileService.read(BotConfig.FILE_PATH_USERS), '{', '}');
					if (!(resText.contains(id))) {
						fileService.update(BotConfig.FILE_PATH_USERS, newText);
					}
					
					ImageReader reader = ImageIO.getImageReadersByMIMEType("image/webp").next();
					WebPReadParam readParam = new WebPReadParam();
					readParam.setBypassFiltering(true);
					reader.setInput(new FileImageInputStream(new File(BotConfig.FILE_PATH)));
					BufferedImage image = reader.read(0, readParam);
					SendMessage messageToInput = new SendMessage().setChatId(update.getMessage().getChatId())
							.setText(update.getMessage().getText());
					process(image, messageToInput.getText());
					Random r = new Random();
					String newPath = BotConfig.FILE_PATH_NEW + r.nextInt() + "id=" + id + "$From=" + firstName + "_"
							+ lastName + "_" + userName + ".webp";
					ImageIO.write(image, "webp", new File(newPath));
					sendDocument(newPath, messageToInput.getChatId());
					sendMessage(messageDefault);
					sendMessage(messageWebP);
					sendMessage(messageJPG);
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

	private BufferedImage process(BufferedImage image, String text) {
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
