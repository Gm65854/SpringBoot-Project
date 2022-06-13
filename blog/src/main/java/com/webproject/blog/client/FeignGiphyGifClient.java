package com.webproject.blog.client;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Feign client to get random gif from giphy.com


@org.springframework.cloud.openfeign.FeignClient(name = "giphyClient", url = "${giphy.url.general}")
public interface FeignGiphyGifClient extends GifClient {
    
    @Override
    @GetMapping("/random")
    ResponseEntity<Map> getRandomGif (
        @RequestParam("api_key") String apiKey,
        @RequestParam("tag") String tag
    );

    
}
