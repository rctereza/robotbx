package com.rctereza.robotbx.ztest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.ocr.ExtractImageText;

public class CaptureScreenshot {

	private static final Logger logger = LoggerFactory.getLogger(CaptureScreenshot.class);
	
	public static void main(String[] args) {
		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME);
		try {
			String text = mb.getText();
			System.out.println(text);
			System.out.println(mb.getMonitorSize());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		System.out.println("Done!");
	}

}
