package com.ill.controller;

import com.ill.model.Singer;
import com.ill.repository.SingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/singer")
public class SingerController {

    @Autowired
    SingerRepository singerRepository;

    @GetMapping("/all")
    public List<Singer> getAllSinger() {
        return singerRepository.findAll();
    }

    @PostMapping("/all")
    public Singer createSinger(@Valid @RequestBody Singer singer) {
        return singerRepository.save(singer);
    }
}
