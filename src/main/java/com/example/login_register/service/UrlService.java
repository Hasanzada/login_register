package com.example.login_register.service;

import com.example.login_register.model.Url;
import com.example.login_register.repository.UrlRepository;
import lombok.extern.log4j.Log4j2;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

@Log4j2
@Service
@PropertySource("classpath:url.properties")
public class UrlService {

    @Value("${url.prefix}")
    private String URL_PREFIX;
    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createUrl(String fullUrl){
        String shortedUrl = Hashing.murmur3_32().hashString(fullUrl, StandardCharsets.UTF_8).toString();
        log.info("shorted URL generated: " + shortedUrl);
        Url url = new Url();
        url.setShortUrl(shortedUrl);
        url.setFullUrl(fullUrl);
        url.setCreationDate(Instant.now());
        url.setVisitCount(0l);
        url.setIsActive(true);
        saveUrl(url);
        return url;
    }

    public Url saveUrl(Url url){
        return urlRepository.save(url);
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }

    public Url findAndCount(String shortUrl){
        Url url = urlRepository.findByShortUrl(URL_PREFIX + shortUrl)
                .orElseThrow(() -> new RuntimeException(String.format("there is no url for %s", shortUrl)));
        url.setVisitCount(url.getVisitCount()+1);
        saveUrl(url);
        return url;
    }


}
