package com.chitraveerakhil.demopdfconverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

@Component
public class PdfGenerator {

	public ResponseEntity<byte[]> createPdf(String page) throws IOException, DocumentException {

		ResponseEntity<byte[]> response = null;
		// String processedHtml = templateEngine.process(getContent(url),
		// context);

		String xhtml = convertToXhtml(page);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(xhtml);
			renderer.layout();
			renderer.createPDF(outputStream, false);
			renderer.finishPDF();
			byte[] contents = outputStream.toByteArray();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			// Here you have to set the actual filename of your pdf
			String filename = page + ".pdf";
			headers.setContentDispositionFormData(filename, filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			response = new ResponseEntity<>(contents, headers, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outputStream.close();
		}
		return response;
	}

	private String getContent(String link) throws IOException {
		// TODO Auto-generated method stub
		URL url = new URL(link);
		Scanner sc = new Scanner(url.openStream());
		StringBuffer sb = new StringBuffer();
		while (sc.hasNext()) {
			sb.append(sc.next());
			// System.out.println(sc.next());
		}
		// Retrieving the String from the String Buffer object
		String result = sb.toString();
		System.out.println(result);
		// Removing the HTML tags
		result = result.replaceAll("<[^>]*>", "");
		sc.close();
		return result;
	}

	private String convertToXhtml(String page) throws IOException {
		Connection conn = Jsoup.connect(page);

		Document document = conn.get();
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		return document.html();
	}
}
