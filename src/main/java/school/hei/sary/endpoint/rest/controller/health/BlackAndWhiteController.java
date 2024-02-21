package com.app.wallet.controller;

import jakarta.annotation.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@RestController
public class BlackAndWhiteController {
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<ByteArrayResource> getBlackAndWhiteImage(@PathVariable String imageName) {
        String imageUrl = "http://localhost:8001/upload/" + imageName;
        try {
            BufferedImage originalImage = ImageIO.read(new URL(imageUrl));
            BufferedImage blackAndWhiteImage = convertToBlackAndWhite(originalImage);
            String formatName="png";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(blackAndWhiteImage, formatName, baos);

            ByteArrayResource resource =  new ByteArrayResource(baos.toByteArray());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BufferedImage convertToBlackAndWhite(BufferedImage originalImage) {
        BufferedImage blackAndWhiteImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = blackAndWhiteImage.createGraphics();
        graphics.drawImage(originalImage,  0,  0, null);
        graphics.dispose();
        return blackAndWhiteImage;
    }

    @PutMapping("/black/{id}")
    public ResponseEntity<String> uploadImage(@RequestBody MultipartFile image,@PathVariable String id) throws IOException {
        BufferedImage originalImage = ImageIO.read(image.getInputStream());
        BufferedImage blackAndWhiteImage = convertToBlackAndWhite(originalImage);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(blackAndWhiteImage, "png", baos);
        return ResponseEntity.ok("Image uploaded and converted to black and white successfully."+id);
    }
}