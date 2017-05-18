package com.kaushik.learningspringboot.learningspringbootvideo.service;

import com.kaushik.learningspringboot.learningspringbootvideo.domain.Image;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by kghos1 on 5/18/2017.
 */
public interface ImageService {
    Resource findOneImage(String fileName);
    void createImage(MultipartFile file) throws IOException;
    void deleteImage(String fileName) throws IOException;
    Page<Image> findPage(Pageable pageble);
}
