package com.rctereza.robotbx.ztest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;
import com.rctereza.robotbx.ocr.ExtractImageText;

public class CaptureScreenshot {

	private static final Logger logger = LoggerFactory.getLogger(CaptureScreenshot.class);

	/**
	 * PAGE_SEG_MODE
	 * 
	 * Orientation and script detection only.
	 */
	private static final int PSM_OSD_ONLY = 0;
	/**
	 * Automatic page segmentation with orientation and script detection. (OSD)
	 */
	private static final int PSM_AUTO_OSD = 1;
	/**
	 * Automatic page segmentation, but no OSD, or OCR.
	 */
	private static final int PSM_AUTO_ONLY = 2;
	/**
	 * Fully automatic page segmentation, but no OSD.
	 */
	private static final int PSM_AUTO = 3;
	/**
	 * Assume a single column of text of variable sizes.
	 */
	private static final int PSM_SINGLE_COLUMN = 4;
	/**
	 * Assume a single uniform block of vertically aligned text.
	 */
	private static final int PSM_SINGLE_BLOCK_VERT_TEXT = 5;
	/**
	 * Assume a single uniform block of text.
	 */
	private static final int PSM_SINGLE_BLOCK = 6;
	/**
	 * Treat the image as a single text line.
	 */
	private static final int PSM_SINGLE_LINE = 7;
	/**
	 * Treat the image as a single word.
	 */
	private static final int PSM_SINGLE_WORD = 8;
	/**
	 * Treat the image as a single word in a circle.
	 */
	private static final int PSM_CIRCLE_WORD = 9;
	/**
	 * Treat the image as a single character.
	 */
	private static final int PSM_SINGLE_CHAR = 10;
	/**
	 * Find as much text as possible in no particular order.
	 */
	private static final int PSM_SPARSE_TEXT = 11;
	/**
	 * Sparse text with orientation and script detection.
	 */
	private static final int PSM_SPARSE_TEXT_OSD = 12;
	/**
	 * Treat the image as a single text line, bypassing hacks that are
	 * Tesseract-specific.
	 */
	private static final int PSM_RAW_LINE = 13;

	/**
	 * OCR_ENGINE_MODE
	 * 
	 * Run Tesseract only - fastest
	 */
	private static final int OEM_TESSERACT_ONLY = 0;
	/**
	 * Run just the LSTM line recognizer
	 */
	private static final int OEM_LSTM_ONLY = 1;
	/**
	 * Run the LSTM recognizer, but allow fallback to Tesseract when things get
	 * difficult
	 */
	private static final int OEM_TESSERACT_LSTM_COMBINED = 2;
	/**
	 * Specify this mode when calling <code>init_*()</code>, to indicate that any of
	 * the above modes should be automatically inferred from the variables in the
	 * language-specific config, command-line configs, or if not specified in any of
	 * the above should be set to the default <code>OEM_TESSERACT_ONLY</code>.
	 */
	private static final int OEM_DEFAULT = 3;
	
	//private static final Double imageScale = 2.0;

	public static void main(String[] args) {

		int[] OCR_ENGINE_MODE = { OEM_TESSERACT_ONLY, OEM_LSTM_ONLY, OEM_TESSERACT_LSTM_COMBINED, OEM_DEFAULT };
		
		int[] PAGE_SEG_MODE = { PSM_OSD_ONLY, PSM_AUTO_OSD, PSM_AUTO_ONLY, PSM_AUTO, PSM_SINGLE_COLUMN,
				PSM_SINGLE_BLOCK_VERT_TEXT, PSM_SINGLE_BLOCK, PSM_SINGLE_LINE, PSM_SINGLE_WORD, PSM_CIRCLE_WORD,
				PSM_SINGLE_CHAR, PSM_SPARSE_TEXT, PSM_SPARSE_TEXT_OSD, PSM_RAW_LINE };

		try {

			ExtractImageText mb = new ExtractImageText(Constants.PROGRAM_NAME, 2.0);
			System.out.println("Text at Scale: 2.0\n" + mb.getText(OCR_ENGINE_MODE[1], PAGE_SEG_MODE[1]));
			
//			mb = new ExtractImageText(Constants.PROGRAM_NAME, 4.0);
//			System.out.println("Text at Scale: 4.0\n" + mb.getText(OCR_ENGINE_MODE[1], PAGE_SEG_MODE[1]));
	
//			for (int i=0; i<OCR_ENGINE_MODE.length; i++) {
//				
//				for (int j=1; j<4; j++) {
//					
//					System.out.println("OCR_ENGINE_MODE: " + String.valueOf(OCR_ENGINE_MODE[1]) + ", PAGE_SEG_MODE: " + String.valueOf(PAGE_SEG_MODE[j]) + ", IMAGE_SCALE: " + imageScale.toString());
//					
//					System.out.println(mb.getText(OCR_ENGINE_MODE[1], PAGE_SEG_MODE[j]));
//					
//				}
//				
//			}
			

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

		}
		
		System.out.println("Done!");
	}
}
