package me.Elitcody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.sun.jndi.toolkit.url.Uri;

public class audiomanager {
    public static AudioStream audioStream = null;

	final static TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
	static int chatId = CHATID;
	public static boolean isPlayin = false;
	public static boolean stop = false;
	@SuppressWarnings("static-access")
	public void handler(String filename)
	{
		File f = new File(main.baseDIRECTORY+"/"+filename);
		File directory = new File(main.baseDIRECTORY);
		if(f.exists())
		{
			if(!isPlayin){
				main.sendMsg(bot, chatId, "Sto riproducendo: "+f.getName());
				playSound(f.getPath());
			}else{
				main.sendMsg(bot, chatId, "Il file "+f.getName()+" è stato stoppato");
			    AudioPlayer.player.stop(audioStream);
			    isPlayin=false;
			}
		}else{
			main.sendMsg(bot, chatId, "Il file che cerchi non è presente nella directory base ecco la lista dei file: ");
			StringBuilder st = new StringBuilder();
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
		}
	}
	
	static Clip clip;
	static FloatControl gainControl;
	public synchronized void playSound(final String myurl) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String gongFile = myurl;

				    InputStream in = new FileInputStream(gongFile);
				    
				    // create an audiostream from the inputstream
				    audioStream = new AudioStream(in);
				 
				    // play the audio clip with the audioplayer class
				    AudioPlayer.player.start(audioStream);
				    isPlayin = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		}).start();
	}
}
