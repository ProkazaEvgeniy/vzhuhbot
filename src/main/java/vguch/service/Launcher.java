package vguch.service;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import vguch.config.VzhuhBot;

public class Launcher {

	public static void main(String[] args) {
		 // TODO Initialize Api Context
		 ApiContextInitializer.init();
        // TODO Instantiate Telegram Bots API
		 TelegramBotsApi botsApi = new TelegramBotsApi();
        // TODO Register our bot
		 try {
	            botsApi.registerBot(new VzhuhBot());
	        } catch (TelegramApiException e) {
	            e.printStackTrace();
	        }
	}
}
