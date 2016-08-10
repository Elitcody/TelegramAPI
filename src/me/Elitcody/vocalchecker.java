package me.Elitcody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;
import com.pengrad.telegrambot.model.Audio;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import COM.rsa.jsafe.au;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.File;
import javax.sound.sampled.*;

public class vocalchecker {	
	
	public void play(File file) 
	{
	    try
	    {
	        final Clip clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));

	        clip.addLineListener(new LineListener()
	        {
	            @Override
	            public void update(LineEvent event)
	            {
	                if (event.getType() == LineEvent.Type.STOP)
	                    clip.close();
	            }
	        });

	        clip.open(AudioSystem.getAudioInputStream(file));
	        clip.start();
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	
	public static void downloadFileFromURL(String urlString, File destination) {    
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
	int chatId = CHATID;
	
	public vocalchecker()
	{

		
		new Thread(new Runnable() {
			
			public void run() {
				String exFileId = "";
				while(true){
					GetUpdatesResponse updatesResponse = bot.execute(new GetUpdates());
					List<Update> updates = updatesResponse.updates();
					
					String fileId = updates.get(updates.size()-1).message().voice().fileId();
					if(!(updates.get(updates.size()-1).toString().contains("voice=null"))){
						if(!exFileId.equalsIgnoreCase(fileId)){
							exFileId = fileId;
							GetFileResponse getFileResponse = bot.execute(new GetFile(fileId));
							String fullPath = bot.getFullFilePath(getFileResponse.file());
							
							System.out.println("\n"+fullPath);
							downloadFileFromURL(fullPath, new File(main.baseDIRECTORY+"/RecordAudio.oga"));
							play(new File(main.baseDIRECTORY+"/RecordAudio.oga"));
							
						}
						
					}	
				}
				
				
			}
		}).start();
	}
}
