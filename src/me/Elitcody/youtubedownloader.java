package me.Elitcody;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramBotAdapter;

public class youtubedownloader {
	public static String getIdFromVideo(String url){
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url); //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
             return matcher.group();
        }
		return null;
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

	  private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
//	  public static String getAudioLink(String inputLink) 
//	  {
//	      String result = null;
//	      HttpURLConnection request = null;
//	      URL url = null;
//
//	      try 
//	      {
//	          url = new URL("http://YoutubeInMP3.com/fetch/?format=JSON&video=" + inputLink);
//	          System.out.println("[0] "+url.toString());
//	          request = (HttpURLConnection) url.openConnection();
//	          request.connect();
//
//	          InputStreamReader reader = new InputStreamReader((InputStream)request.getContent());
//
//	          JsonParser jsonParser = new JsonParser(); // GSON-Library
//
//
//	          JsonObject youtubeInMP3 = (JsonObject)jsonParser.parse(reader).getAsJsonObject();
//
//	          if (youtubeInMP3 != null) 
//	          {
//	              result = youtubeInMP3.get("link").getAsString();
//	          }
//	          request.disconnect();
//	          reader.close();
//	      } 
//	      catch (MalformedURLException e) 
//	      {
//	          e.printStackTrace();
//	      }
//	      catch (IOException e) 
//	      {
//	          e.printStackTrace();
//	      }
//
//	      return result;
//	  }
	@SuppressWarnings("deprecation")
	public void  download(String url) {
	      try {
			    Date d = new Date();
			    SimpleDateFormat form = new SimpleDateFormat("hh_mm_ss");
			    String s = form.format(d); // or if you want to save it in String str


			    URL urlpath = new URL("http://www.youtubeinmp3.com/fetch/?format=JSON&video=http://www.youtube.com/watch?v=5hEh9LiSzow");
			    File destination = new File(main.baseDIRECTORY+"/"+getIdFromVideo(url)+".mp3");
			    
//			    System.out.print("\n[A] "+getAudioLink(urlpath.toString()));
			    
			    JSONObject json = readJsonFromUrl(urlpath.toString());
			    System.out.print("\n[B] "+URLDecoder.decode((String) json.get("link")+"\n"));
			    downloadFileFromURL(URLDecoder.decode((String)json.get("link")) ,destination);

	            
	            File source = new File(main.baseDIRECTORY+"/"+getIdFromVideo(url)+".mp3");
	            File target = new File(main.baseDIRECTORY+"/"+getIdFromVideo(url)+".wav");
	            AudioAttributes audio = new AudioAttributes();
	            audio.setCodec("pcm_s16le");
	            audio.setBitRate(new Integer(16));
	            audio.setChannels(new Integer(2));
	            audio.setSamplingRate(new Integer(8000));
	            EncodingAttributes attrs = new EncodingAttributes();
	            attrs.setFormat("wav");
	            attrs.setAudioAttributes(audio);
	            Encoder encoder = new Encoder();
	            try {
	               encoder.encode(source, target, attrs);
	               System.out.println("Successfully created"); 
	           } catch (IllegalArgumentException e) {
	               // TODO Auto-generated catch block
	               e.printStackTrace();
	           } catch (InputFormatException e) {
	               // TODO Auto-generated catch block
	               e.printStackTrace();
	           } catch (EncoderException e) {
	               // TODO Auto-generated catch block
	               e.printStackTrace();
	           }
	    		final TelegramBot bot = TelegramBotAdapter.build("TOKENAPI");
				int chatId = CHATID;
	            source.delete();
	            main.sendMsg(bot, chatId, "File scaricato con successo il nuovo nome è: "+getIdFromVideo(url));
	            main.sendMsg(bot, chatId, "Per rinominarlo scrivi /renominate "+getIdFromVideo(url)+".wav nuovo_nome.wav");
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	}
}
