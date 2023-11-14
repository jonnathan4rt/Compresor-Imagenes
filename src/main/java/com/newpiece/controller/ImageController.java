package com.newpiece.controller;

import com.newpiece.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/compress")
    public ResponseEntity<byte[]> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
        if (files.isEmpty()) {
            return ResponseEntity.badRequest().build();
        } else {
            return imageService.processImages(files);
        }
    }
}
