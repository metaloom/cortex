package io.metaloom.loom.action.tika;

import java.io.IOException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import io.metaloom.worker.action.api.ProcessableMedia;

public class MediaTikaParser {

	/**
	 * Exclusions
	 */
	private static final Set<MediaType> EXCLUDES = new HashSet<>(Arrays.asList(
		MediaType.application("vnd.ms-visio.drawing"),
		MediaType.application("vnd.ms-visio.drawing.macroenabled.12"),
		MediaType.application("vnd.ms-visio.stencil"),
		MediaType.application("vnd.ms-visio.stencil.macroenabled.12"),
		MediaType.application("vnd.ms-visio.template"),
		MediaType.application("vnd.ms-visio.template.macroenabled.12"),
		MediaType.application("vnd.ms-visio.drawing")));

	/**
	 * Supported parsers.
	 */
	private static final Parser[] PARSERS = new Parser[] {
		// documents
		new org.apache.tika.parser.html.HtmlParser(),
		// new org.apache.tika.parser.rtf.RTFParser(),
		new org.apache.tika.parser.pdf.PDFParser(),
		new org.apache.tika.parser.txt.TXTParser(),
		new org.apache.tika.parser.microsoft.OfficeParser(),
		new org.apache.tika.parser.microsoft.OldExcelParser(),
		ParserDecorator.withoutTypes(xmlParser(), EXCLUDES),
		new org.apache.tika.parser.odf.OpenDocumentParser(),
		new org.apache.tika.parser.iwork.IWorkPackageParser(),
		new org.apache.tika.parser.xml.DcXMLParser(),
		new org.apache.tika.parser.epub.EpubParser(),

		// audio
		new org.apache.tika.parser.audio.AudioParser(),
		new org.apache.tika.parser.mp3.Mp3Parser(),

		// video
		new org.apache.tika.parser.video.FLVParser(),
		new org.apache.tika.parser.mp4.MP4Parser(),

		// images
		new org.apache.tika.parser.image.ImageParser(),
		// new org.apache.tika.parser.jpeg.JpegParser(),
		new org.apache.tika.parser.image.WebPParser(),

		// ogg (audio/video)
		new org.gagravarr.tika.OggParser()
	};

	private static OOXMLParser xmlParser() {
		OOXMLParser ooxmlParser = new OOXMLParser();
		ooxmlParser.setUseSAXDocxExtractor(true);
		ooxmlParser.setUseSAXPptxExtractor(true);
		return ooxmlParser;
	}

	private static final AutoDetectParser PARSER_INSTANCE = new AutoDetectParser(PARSERS);

	private static final AutoDetectParser TIKA_INSTANCE = new AutoDetectParser(PARSER_INSTANCE.getDetector(), PARSER_INSTANCE);

	public static String parse(ProcessableMedia media) throws IOException, TikaException, SAXException {
		try (InputStream ins = media.open()) {
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			TIKA_INSTANCE.parse(ins, handler, metadata);
			for (String name : metadata.names()) {
				System.out.println(name + " " + metadata.get(name));
			}

		}
		return null;
	}
}
