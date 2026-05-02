package com.rctereza.robotbx.ztest;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotocr.MessageBox2;

public class CaptureScreenshot {

	public static void main(String[] args) throws Exception {
		MessageBox2 mb = new MessageBox2(Constants.PROGRAM_NAME);
		String text = mb.getText();
		//text = text.replace("\n", "");
		System.out.println(text);
	}

}
