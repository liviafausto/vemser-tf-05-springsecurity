package br.com.dbc.wbhealth.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.FileInputStream;
import java.io.IOException;

public class ImageConverter {
    public static String convertToBase64(String imagePath) throws IOException {
        try (FileInputStream imageStream = new FileInputStream("src/main/java/br/com/dbc/wbhealth/imagens/Logo_WBHEALTH.png")) {
            byte[] imageBytes = new byte[imageStream.available()];
            imageStream.read(imageBytes);
            return Base64.encodeBase64String(imageBytes);
        }
    }

}
