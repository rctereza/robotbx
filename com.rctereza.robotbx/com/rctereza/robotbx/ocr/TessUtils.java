package com.rctereza.robotbx.ocr;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rctereza.robotbx.Constants;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

public class TessUtils {

	private static final Logger logger = LoggerFactory.getLogger(TessUtils.class);
	
	private static Dimension monitorSize;

	public static Path prepareTessData() throws IOException {

		Path dir = Paths.get(Constants.OCR_SYSTEM_PATH, "tessdata");

		if (!Files.exists(dir)) {

			Path tessdataDir = Files.createDirectories(dir);

			try (InputStream in = TessUtils.class.getResourceAsStream("/tessdata/eng.traineddata")) {
				Files.copy(in, tessdataDir.resolve("eng.traineddata"));
			}

			try (InputStream in = TessUtils.class.getResourceAsStream("/tessdata/por.traineddata")) {
				Files.copy(in, tessdataDir.resolve("por.traineddata"));
			}

			try (InputStream in = TessUtils.class.getResourceAsStream("/tessdata/osd.traineddata")) {
				Files.copy(in, tessdataDir.resolve("osd.traineddata"));
			}
		}

		return dir;

	}

	public static ITesseract getInstance() throws IOException {

		Path dir = prepareTessData();

		ITesseract instance = new Tesseract();

		instance.setDatapath(dir.toString());

		instance.setLanguage("eng+por");

		instance.setOcrEngineMode(1);

		//instance.setPageSegMode(11); // sparse text (best for UI)
		instance.setPageSegMode(6);

		instance.setVariable("user_defined_dpi", "300");

		//instance.setVariable("tessedit_char_whitelist",	"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789:\\._- ");

		return instance;

	}
	
	public static ITesseract getInstance(int engineMode, int pageSegMode) throws IOException {

		Path dir = prepareTessData();

		ITesseract instance = new Tesseract();

		instance.setDatapath(dir.toString());

		instance.setLanguage("eng+por");

		instance.setOcrEngineMode(engineMode);

		instance.setPageSegMode(pageSegMode);

		instance.setVariable("user_defined_dpi", "300");

		return instance;
	}


	public static BufferedImage captureScreen(String windowTitle, Double imageScale) throws IOException, AWTException {
		
		BufferedImage result = null;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		Rectangle captureRect = new Rectangle(screenSize); // full screen

		if (windowTitle != null && !windowTitle.trim().equals("")) {
			try {
				WindowDimensions wd = new WindowDimensions(windowTitle);
				captureRect = wd.getRectangle();
				monitorSize = wd.getMonitor();
				logger.info("Found [{}] {}", windowTitle, captureRect);
			} catch (Exception e) {
				logger.error("Not found [{}], using full screen instead.\n{}", windowTitle, e.getMessage(), e);
			}
		}

		Robot robot = new Robot();

		// 1. Capture the screen area as an image
		BufferedImage screenshot = robot.createScreenCapture(captureRect);

		// 2. Convert to OpenCV Mat
		Mat mat = bufferedImageToMat(screenshot);

		// 3. Detect edges
		Mat edges = detectEdges(mat);

		// 4. Find largest rectangle
		Rect rect = findLargestRectangle(edges);

		if (rect != null) {
			
			logger.info("Detected rectangle: {}.", rect);

			// 5. Crop region
			BufferedImage cropped = screenshot.getSubimage(rect.x, rect.y, rect.width, rect.height);

			// 6. Convert image colors to gray
			BufferedImage imageRepainted = toGray(cropped);

			// 7. Make image a 2x its size
			BufferedImage imageBigger = scaleImage(imageRepainted, imageScale);

			//String userDir = System.getProperty(Constants.OCR_SYSTEM_PATH);
			File dir = new File(Constants.OCR_SYSTEM_PATH);
			dir.mkdirs();

			// 8. Save the captured image to a file
			File file = new File(dir, Constants.OCR_IMAGE_NAME);
			ImageIO.write(imageBigger, "PNG", file);

			logger.info("Screen captured and saved at [{}].",file.getAbsolutePath());
			
			// (Optional) Save to verify
			file = new File(dir, "detected.png");
			ImageIO.write(screenshot, "PNG", file);

			result = imageBigger;

		} else {
			logger.warn("No rectangle detected.");
		}
		
		return result;
	}
	
	public static BufferedImage captureScreen2(String windowTitle, Double imageScale) throws Exception {
		
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // full screen
//		Rectangle captureRect = new Rectangle(screenSize);
		
		WindowDimensions wd = new WindowDimensions(windowTitle);
		Rectangle captureRect = wd.getRectangle();
		monitorSize = wd.getMonitor();
		
		logger.info("Found [{}] {}/{} - Scale {}", windowTitle, monitorSize, captureRect, imageScale);

		Robot robot = new Robot();

		BufferedImage screenshot = robot.createScreenCapture(captureRect);

		BufferedImage imageRepainted = toGray(screenshot);
		
		BufferedImage imageBigger = scaleImage(imageRepainted, imageScale);
		
//		String userDir = System.getProperty(Constants.OCR_SYSTEM_PATH);
		File dir = new File(Constants.OCR_SYSTEM_PATH);
		dir.mkdirs();

		File file = new File(dir, Constants.OCR_IMAGE_NAME);
		ImageIO.write(imageBigger, "PNG", file);

		logger.info("Window captured and saved at: [{}].",file.getAbsolutePath());
		
		return imageBigger;
	}

	public static Dimension getMonitorSize() {
		return monitorSize;
	}
	
	private static BufferedImage toGray(BufferedImage img) {

		BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		Graphics g = gray.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		return gray;
	}

	private static BufferedImage scaleImage(BufferedImage img, double scale) {
		int w = (int) (img.getWidth() * scale);
		int h = (int) (img.getHeight() * scale);

		BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaled.createGraphics();
		g.drawImage(img, 0, 0, w, h, null);
		g.dispose();

		return scaled;
	}

	private static Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		int[] data = bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth());

		byte[] bytes = new byte[bi.getWidth() * bi.getHeight() * 3];

		for (int i = 0; i < data.length; i++) {
			bytes[i * 3] = (byte) ((data[i] >> 16) & 0xFF); // R
			bytes[i * 3 + 1] = (byte) ((data[i] >> 8) & 0xFF); // G
			bytes[i * 3 + 2] = (byte) (data[i] & 0xFF); // B
		}

		mat.put(0, 0, bytes);
		return mat;
	}

	private static Mat detectEdges(Mat src) {
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

		Mat edges = new Mat();
		Imgproc.Canny(gray, edges, 50, 150);

		return edges;
	}

	private static Rect findLargestRectangle(Mat edges) {

		List<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();

		Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

		double maxArea = 0;
		Rect bestRect = null;

		for (MatOfPoint contour : contours) {
			Rect rect = Imgproc.boundingRect(contour);
			double area = rect.area();

			if (area > maxArea) {
				maxArea = area;
				bestRect = rect;
			}
		}

		return bestRect;
	}

}
