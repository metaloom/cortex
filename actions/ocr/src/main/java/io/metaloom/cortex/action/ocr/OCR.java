package io.metaloom.cortex.action.ocr;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCR {
	
	public static String ocr(BufferedImage img) throws TesseractException {
		ITesseract tesseract = new Tesseract();
		// https://tesseract-ocr.github.io/tessdoc/Data-Files
		tesseract.setDatapath("tessdata");
		return tesseract.doOCR(img);
	}
}
