package com.example.login_register.controller;

import com.example.login_register.model.Url;
import com.example.login_register.service.UrlService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }


    @GetMapping("/all")
    public List<Url> getAll() {
        return urlService.findAll();
    }

    @GetMapping("/s/{shortUrl}")
    public RedirectView getUrl(@PathVariable String shortUrl) {
        Url found = urlService.findAndCount(shortUrl);
        log.info(String.format("URL Retrieved: %s", found.getFullUrl()));
        return new RedirectView(found.getFullUrl());
    }

    @PostMapping("/create")
    public ModelAndView createUrl(@RequestParam String fullUrl){
        UrlValidator urlValidator = new UrlValidator(
                new String[]{"http", "https"}
        );
        if(!urlValidator.isValid(fullUrl)){
            throw new RuntimeException(String.format("URL Invalid: %s", fullUrl));
        }
        Url savedUrl = urlService.createUrl(fullUrl);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("success");
        mav.addObject("shortUrl", savedUrl.getShortUrl());
        log.info(savedUrl.getShortUrl());
        return mav;
    }

}
