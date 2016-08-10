package me.Elitcody;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDriver;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class camhandler {
	public camhandler(){
		Dimension[] nonStandardResolutions = new Dimension[] {
				WebcamResolution.PAL.getSize(),
				WebcamResolution.HD720.getSize(),
				new Dimension(2000, 1000),
				new Dimension(1000, 500),
			};
		Webcam webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(nonStandardResolutions);
		webcam.setViewSize(WebcamResolution.HD720.getSize());
		webcam.open();
		
		try {
			ImageIO.write(webcam.getImage(), "JPEG", new File(main.baseDIRECTORY+"/Test.jpeg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		webcam.close();
	}
	
}
