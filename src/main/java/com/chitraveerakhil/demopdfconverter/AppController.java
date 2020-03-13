package com.chitraveerakhil.demopdfconverter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/pdfConverter")
public class AppController {

	@Autowired
	PdfGenerator pdfGenaratorUtil;
	
	@RequestMapping("/genFile")
	public ResponseEntity<byte[]> generateFile(@RequestParam("url") String url) throws IOException, DocumentException {
		
	    ResponseEntity<byte[]> response = pdfGenaratorUtil.createPdf(url); 
	    		return response  ;
	}
}
