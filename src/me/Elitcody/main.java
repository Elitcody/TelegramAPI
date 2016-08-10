package me.Elitcody;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;

public class main {

	public static boolean doCommand = false;
	
	static Path currentRelativePath = Paths.get("");

	public static String baseDIRECTORY = currentRelativePath.toAbsolutePath().toString();
	public static void sendPhoto(TelegramBot bot,int chatId, File f){
		SendPhoto photo = new SendPhoto(chatId, f);
		bot.execute(photo);
	}
	public static void sendMsg(TelegramBot bot,int chatId, String txt){
		SendMessage msg = new SendMessage(chatId, txt).parseMode(ParseMode.HTML);
		bot.execute(msg);
	}
	

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Gui g = new Gui();
//		vocalchecker v = new vocalchecker();
		g.runApplet();
		cmdhandler c = new cmdhandler();
		c.run();
	
		/*
		 *  TODO: READ VOCALS
		 *  NEW FILE>RUNNABLE CHECK>DOWNLOAD>PLAYIT AUTO
		 */
	}
}
