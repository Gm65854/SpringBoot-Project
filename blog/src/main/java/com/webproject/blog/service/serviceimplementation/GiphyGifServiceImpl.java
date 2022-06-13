package com.webproject.blog.service.serviceimplementation;
import com.webproject.blog.client.GifClient;
import com.webproject.blog.service.ServiceInterface.GifService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;







// working with Giphy.com

@Service
public class GiphyGifServiceImpl implements GifService {

    private GifClient gifClient;
    @Value("#{giphy.api.key}")
    private String apiKey;

    @Autowired
    public GiphyGifServiceImpl (GifClient gifClient) {
        this.gifClient = gifClient;
    }

    /**
     * Answer from Giphy.com is sent to the client as ResponceEntity
     * one minor modification - adding compareResult 
     * that's for visual result checking.
     *
     * @param tag
     * @return
     */

        @Override
        public ResponseEntity<Map> getGif(String tag) {
            ResponseEntity<Map> result = gifClient.getRandomGif(this.apiKey, tag);
            result.getBody().put("compareResult", tag);
            return result;
        }
    
}
