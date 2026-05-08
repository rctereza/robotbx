package com.rctereza.robotbx.ztest;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.ocr.ExtractImageText;

public class CaptureScreenshot {

	public static void main(String[] args) throws Exception {
		ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME);
		String text = mb.getText();
		//text = text.replace("\n", "");
		System.out.println(text);
	}

}
