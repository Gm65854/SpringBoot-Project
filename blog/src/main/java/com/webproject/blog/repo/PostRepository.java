package com.webproject.blog.repo;

import com.webproject.blog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
    
}
