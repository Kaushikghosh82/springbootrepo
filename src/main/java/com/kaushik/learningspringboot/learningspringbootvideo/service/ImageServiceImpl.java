package com.kaushik.learningspringboot.learningspringbootvideo.service;

import com.kaushik.learningspringboot.learningspringbootvideo.domain.Image;
import com.kaushik.learningspringboot.learningspringbootvideo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by kghos1 on 5/15/2017.
 */
@Service
public class ImageServiceImpl implements ImageService {
    private static final String UPLOAD_DIRECTORY = "upload-dir";
    private static final String SLASH = "/";
    private final ResourceLoader resourceLoader;
    private final ImageRepository repository;

    @Autowired
    public ImageServiceImpl(ImageRepository repository, ResourceLoader resourceLoader) {
        this.repository = repository;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Resource findOneImage(String fileName) {
        return resourceLoader.getResource("file:" + UPLOAD_DIRECTORY + SLASH + fileName);
    }

    @Override
    public void createImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename()));
            repository.save(new Image(file.getOriginalFilename()));
        }
    }

    @Override
    public void deleteImage(String fileName) throws IOException {
        final Image byName = repository.findByName(fileName);
        repository.delete(byName);
        Files.deleteIfExists(Paths.get(UPLOAD_DIRECTORY, fileName));
    }

    @Override
    public Page<Image> findPage(Pageable pageble) {
        return repository.findAll(pageble);
    }

    @Bean
    CommandLineRunner setUp(final ImageRepository repository) throws IOException {
        return args -> {
            FileSystemUtils.deleteRecursively(new File(UPLOAD_DIRECTORY));
            Files.createDirectory(Paths.get(UPLOAD_DIRECTORY));

            FileCopyUtils.copy("Test File", new FileWriter(UPLOAD_DIRECTORY + "/test"));
            repository.save(new Image("test"));

            FileCopyUtils.copy("Test File2", new FileWriter(UPLOAD_DIRECTORY + "/test2"));
            repository.save(new Image("test2"));

            FileCopyUtils.copy("Test File3", new FileWriter(UPLOAD_DIRECTORY + "/test3"));
            repository.save(new Image("test3"));
        };
    }
}
