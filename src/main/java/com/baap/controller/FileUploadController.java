package com.baap.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    // Endpoint to handle image uploads
    @PostMapping("/image")
    public String handleImageUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        // Generate a unique file name
        String fileName = generateFileName(file.getOriginalFilename());

        // Save file to the server
        saveFile(file, fileName, "images");

        // Return success message
        return "Image uploaded successfully: " + fileName;
    }

    // Endpoint to handle video uploads
    @PostMapping("/video")
    public String handleVideoUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Please select a file to upload";
        }

        // Generate a unique file name
        String fileName = generateFileName(file.getOriginalFilename());

        // Save file to the server
        saveFile(file, fileName, "videos");

        // Return success message
        return "Video uploaded successfully: " + fileName;
    }

    // Method to generate a unique file name
    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }

    // Method to save file to the server
    private void saveFile(MultipartFile file, String fileName, String directory) throws IOException {
        // Create directory if it doesn't exist
        Path path = Paths.get(directory);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Save file to the server
        byte[] bytes = file.getBytes();
        Path filePath = Paths.get(directory + File.separator + fileName);
        Files.write(filePath, bytes);
    }
}
