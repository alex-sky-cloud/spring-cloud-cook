package com.example.circuitbreake.controller;

import com.example.circuitbreake.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final AlbumService service;

    @GetMapping("/albums")
    public String albums() {
        return service.getAlbumList();
    }
}