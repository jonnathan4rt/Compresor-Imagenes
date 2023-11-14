package com.newpiece.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ImageService {

    public ResponseEntity<byte[]> processImages(List<MultipartFile> files) {
        try {
            if (files.size() == 1) {
                // Procesar un solo archivo de imagen
                MultipartFile file = files.get(0);
                BufferedImage image = ImageIO.read(file.getInputStream());

                // Convertir la imagen a un array de bytes
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", byteOutput);
                byte[] imageBytes = byteOutput.toByteArray();

                // Configurar encabezados HTTP para la respuesta de un solo archivo de imagen
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentDispositionFormData("attachment", file.getOriginalFilename());

                // Devolver la respuesta HTTP con la imagen individual
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(imageBytes);
            } else {
                // Procesar múltiples archivos de imagen y comprimir en un archivo ZIP
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                ZipOutputStream zipOutput = new ZipOutputStream(byteOutput);

                for (MultipartFile file : files) {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    if (image != null) {
                        // Agregar la imagen al archivo ZIP
                        String fileName = file.getOriginalFilename();
                        zipOutput.putNextEntry(new ZipEntry(fileName));

                        ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", imageOutput);
                        byte[] imageBytes = imageOutput.toByteArray();
                        zipOutput.write(imageBytes, 0, imageBytes.length);

                        imageOutput.close();
                        zipOutput.closeEntry();
                    } else {
                        // Manejo de error si falla al cargar la imagen
                    }
                }

                zipOutput.close();

                // Convertir el archivo ZIP a un array de bytes
                byte[] zipBytes = byteOutput.toByteArray();

                // Configurar encabezados HTTP para la respuesta del archivo ZIP
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", "imagenes_comprimidas.zip");

                // Devolver la respuesta HTTP con el archivo ZIP comprimido
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(zipBytes);
            }
        } catch (IOException e) {
            // Manejo de excepción de IO, devuelve una respuesta de error del servidor
            return ResponseEntity.status(500).build();
        }
    }
}

