package com.webproject.blog.service.ServiceInterface;

import java.util.Map;

import org.springframework.http.ResponseEntity;


public interface GifService {
    ResponseEntity <Map> getGif (String tag);
}
