package be.kdg.youthcouncil.controllers.api;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequestMapping ("/api/qrcode")
@RestController
@AllArgsConstructor
public class RestQRCodeController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Environment environment;

	public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageConfig con = new MatrixToImageConfig(0xFF000000, 0xFFFFFFFF);

		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, con);
		return pngOutputStream.toByteArray();
	}

	public ResponseEntity<Resource> getQrCode(String url) throws IOException, WriterException {
		byte[] test = getQRCodeImage(url, 300, 300);
		ByteArrayResource resource = new ByteArrayResource(test);
		return ResponseEntity.ok()
		                     .contentType(MediaType.APPLICATION_OCTET_STREAM)
		                     .contentLength(resource.contentLength())
		                     .header(HttpHeaders.CONTENT_DISPOSITION,
				                     ContentDisposition.attachment()
				                                       .filename("qr-code")
				                                       .build().toString())
		                     .body(resource);
	}

	@GetMapping ("actionpoint/{municipality}/{id}")
	public ResponseEntity<Resource> getActionPointQrCode(@PathVariable String municipality, @PathVariable long id) throws IOException, WriterException {
		String host = environment.getProperty("server.host");
		String port = environment.getProperty("local.server.port");
		String domain = !port.equals("443") ? host + ":" + port : host;
		String fin = String.format("%s/youthcouncils/%s/actionpoints/%s", domain, municipality, id);
		return getQrCode(fin);
	}


}
