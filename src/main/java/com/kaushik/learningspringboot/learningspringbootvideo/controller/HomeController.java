package com.kaushik.learningspringboot.learningspringbootvideo.controller;

import com.kaushik.learningspringboot.learningspringbootvideo.domain.Image;
import com.kaushik.learningspringboot.learningspringbootvideo.service.ImageService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by kghos1 on 5/15/2017.
 */
@Controller
public class HomeController {

    private static final String BASE_PATH = "/images";
    private static final String FILE_NAME = "{fileName:.+}";
    private final ImageService imageService;

    @Autowired
    public HomeController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(method = RequestMethod.GET, value = BASE_PATH + "/" + FILE_NAME + "/raw")
    @ResponseBody
    public ResponseEntity<?> oneRawImage(@PathVariable String fileName) {
        try {
            Resource file = imageService.findOneImage(fileName);
            return ResponseEntity.ok()
                    .contentLength(file.contentLength())
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(file.getInputStream()));
        } catch (IOException exception) {
            return ResponseEntity.badRequest()
                    .body("Couldn't find " + fileName + " => " + exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = BASE_PATH)
    @ResponseBody
    public ResponseEntity<?> createFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws URISyntaxException {
        try {
            imageService.createImage(file);
            final URI localUri = new URI(request.getRequestURL().toString() + "/")
                    .resolve(file.getOriginalFilename() + "/raw");
            return ResponseEntity.created(localUri)
                    .body("Successfully uploaded " + file.getOriginalFilename());
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to upload " + file.getOriginalFilename() + " => " + exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = BASE_PATH + "/" + FILE_NAME)
    @ResponseBody
    public ResponseEntity<?> deleteFile(@PathVariable String fileName) {
        try {
            imageService.deleteImage(fileName);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Successfully deleted " + fileName);
        } catch (IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete " + fileName + " => " + exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String index(Model model, Pageable pageable) {
        System.out.println("I am here");
        final Page<Image> page = imageService.findPage(pageable);
        model.addAttribute("page", page);
        System.out.println("Page - "+ page.getTotalPages());
        for (Image image: page.getContent()) {
            System.out.println("Image Id - "+ image.getId());
            System.out.println("Image Name - "+ image.getName());
        }
        return "index";
    }
}

