package com.gestion.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {

    public static String encodeImage(MultipartFile image) throws IOException {

        return Base64.getEncoder().encodeToString(image.getBytes());
    }


}
