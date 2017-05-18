package com.kaushik.learningspringboot.learningspringbootvideo.repository;

import com.kaushik.learningspringboot.learningspringbootvideo.domain.Image;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kghos1 on 5/15/2017.
 */
@Repository
public interface ImageRepository extends PagingAndSortingRepository<Image, Long> {
    Image findByName(String name);
}
