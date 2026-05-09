package com.rctereza.robotbx.ocr;

import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;

public class ExtractImageText {

	private static final Logger logger = LoggerFactory.getLogger(ExtractImageText.class);

	private String windowTitle = "";
	
	private Double imageScale = 2.0;

	static {
		OpenCV.loadLocally();
	}
	
	public ExtractImageText() {
	}
	
	public ExtractImageText(String windowTitle) {
		this.windowTitle = windowTitle;
	}
	
	public ExtractImageText(String windowTitle, double imageScale) {
		this.windowTitle = windowTitle;
		this.imageScale = imageScale;
	}

	public String getText() throws Exception {
		
		String text = "";

		ITesseract instance = TessUtils.getInstance();

		BufferedImage screenshot = TessUtils.captureScreen2(this.windowTitle, this.imageScale);
		
		try {
			
			text = instance.doOCR(screenshot);
			
			Path path = Paths.get(Constants.OCR_SYSTEM_PATH + Constants.OCR_FILE_NAME);
			
			Files.writeString(path, text, StandardCharsets.UTF_8);
			
			logger.info("Text extracted saved at.: [" + path.toAbsolutePath() + "]");
			
		} catch (TesseractException e) {
			
			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());
			
		}
		
		return text;
	}
}
