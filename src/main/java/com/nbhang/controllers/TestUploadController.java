package com.nbhang.controllers;

import com.nbhang.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestUploadController {

    private final FileStorageService fileStorageService;

    @GetMapping("/upload")
    public String showUploadForm() {
        return "test-upload-form";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        System.out.println("=== TEST UPLOAD ===");
        System.out.println("File received: " + (file != null));

        if (file != null) {
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("File empty: " + file.isEmpty());

            if (!file.isEmpty()) {
                try {
                    String filename = fileStorageService.storeFile(file, 999L);
                    System.out.println("File stored: " + filename);
                    return "SUCCESS! File uploaded: " + filename;
                } catch (Exception e) {
                    System.err.println("Upload failed: " + e.getMessage());
                    e.printStackTrace();
                    return "ERROR: " + e.getMessage();
                }
            }
        }

        return "No file received";
    }
}
