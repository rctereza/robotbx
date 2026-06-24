package com.rctereza.robotbx.ocr;

import java.awt.Dimension;
import java.awt.Rectangle;
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

	private Dimension monitorSize;
	
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
	
	public Dimension getMonitorSize() {
		return monitorSize;
	}

	public String getText() throws Exception {

		monitorSize = TessUtils.getMonitorSize();

		ITesseract instance = TessUtils.getInstance();

		BufferedImage screenshot = TessUtils.captureScreen2(this.windowTitle, this.imageScale);

		String text = "";

		try {

			text = instance.doOCR(screenshot);

			logger.info("Text extracted: {}", text);
			
			Path path = Paths.get(Constants.OCR_SYSTEM_PATH + Constants.OCR_FILE_NAME);

			Files.writeString(path, text, StandardCharsets.UTF_8);

			logger.info("Text saved at.: {}", path.toAbsolutePath());

		} catch (TesseractException e) {

			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());

		}

		return text;
	}

	public String getText(int engineMode, int pageSegMode) throws Exception {

		monitorSize = TessUtils.getMonitorSize();

		ITesseract instance = TessUtils.getInstance(engineMode, pageSegMode);

		BufferedImage screenshot = TessUtils.captureScreen(this.windowTitle, this.imageScale);

		String text = "";

		try {

			text = instance.doOCR(screenshot);
			
			logger.info("Text extracted: {}", text);

			Path path = Paths.get(Constants.OCR_SYSTEM_PATH + Constants.OCR_FILE_NAME);

			Files.writeString(path, text, StandardCharsets.UTF_8);

			logger.info("Text saved at.: {}", path.toAbsolutePath());

		} catch (TesseractException e) {

			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());

		}

		return text;
	}

	public String getTextFrom(Rectangle rect, TessUtils.FIELD_TYPE ft) throws Exception {

		monitorSize = TessUtils.getMonitorSize();

		ITesseract instance = TessUtils.getInstance(ft);

		BufferedImage screenshot = TessUtils.captureScreenFrom(this.windowTitle, this.imageScale, rect);

		String text = "";

		try {

			text = instance.doOCR(screenshot);
			
			logger.info("Text extracted: {}", text);

			Path path = Paths.get(Constants.OCR_SYSTEM_PATH + Constants.OCR_FILE_NAME);

			Files.writeString(path, text, StandardCharsets.UTF_8);

			logger.info("Text saved at.: {}", path.toAbsolutePath());

		} catch (TesseractException e) {

			logger.error(e.getMessage(), e);
			throw new Exception(e.getMessage());

		}

		return text;
	}
	
	public int countDarkPixels(Rectangle rect, int darknessThreshold) throws Exception {

		monitorSize = TessUtils.getMonitorSize();

		BufferedImage screenshot = TessUtils.captureScreenFrom(this.windowTitle, this.imageScale, rect);

	    int count = 0;

	    for (int y = 0; y < screenshot.getHeight(); y++) {

	        for (int x = 0; x < screenshot.getWidth(); x++) {

	            int rgb = screenshot.getRGB(x, y);

	            int r = (rgb >> 16) & 0xFF;
	            int g = (rgb >> 8) & 0xFF;
	            int b = rgb & 0xFF;

	            int gray = (r + g + b) / 3;

	            if (gray < darknessThreshold) {
	                count++;
	            }
	        }
	    }
	    
	    logger.info("Pixel counted: {}", count);
	    
	    Path path = Paths.get(Constants.OCR_SYSTEM_PATH + Constants.OCR_FILE_NAME);

		Files.writeString(path, String.valueOf(count), StandardCharsets.UTF_8);

		logger.info("Pixel count saved at.: {}", path.toAbsolutePath());

	    return count;
	}

}
