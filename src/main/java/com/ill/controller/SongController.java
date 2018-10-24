package com.ill.controller;

import com.ill.model.Singer;
import com.ill.repository.SingerRepository;
import com.ill.transactional.SongTransactional;
import com.ill.model.Song;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/song")
public class SongController {
    private static final Logger log = LogManager.getLogger(SongTransactional.class);
    private SongTransactional songTransactional;
    @Autowired
    SingerRepository singerRepository;

    public SongController(SongTransactional songTransactional) {
        this.songTransactional = songTransactional;
    }

  //  @RequestMapping(value = "song", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/listSong")
    public List<List<Song>> getAllSong() {
        log.debug("Inside getAllSong Ctrl");
        return songTransactional.getAllSong();
    }

    @GetMapping("/singers")
    public List<Singer> getAllSinger() {
        List<Singer> singerLst = singerRepository.findAll();
        singerLst.sort(Comparator.comparing(Singer::getName));
        return singerLst;
    }


    @PostMapping("/entrySong")
    public ResponseEntity<Song> createUpdateSong(@Valid @RequestBody Song song) throws URISyntaxException {
        System.out.println("song.getId()=" + song.getId() );
        Song result = null;
        try {
            if (song.getId() == null) {
                System.out.println("Insert Song" );
                result = songTransactional.createSong(song);
            }
            else {
                System.out.println("Update Song" );
                result = songTransactional.updateSong(song);
            }

            return ResponseEntity .ok(result) ;
        } catch (EntityExistsException e) {
            return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/songYear")
    public List<List<Song>> getAllSongByYear(@RequestParam(value = "year", required = false) String year) {
        log.debug("Inside getAllSongByYear Ctrl, year= " + year);
        List<List<Song>> songLst = songTransactional.getAllSongByYear(year);
        System.out.println("Rakesh SongLst size=" + songLst.size() );

        return songLst;
    }

    @RequestMapping(value = "/songSinger")
    public List<List<Song>>  getAllSongBySinger(@RequestParam(value = "name", required = false) String name) {
        log.debug("Inside getAllSongBySinger Ctrl, name= " + name);
        List<List<Song>> songLst = songTransactional.getAllSongBySinger(name);
        System.out.println("Rakesh SongLst=" + songLst );

        return songLst;
    }


    @RequestMapping("/deleteSong")
    public ResponseEntity deleteSong(@RequestParam(value = "id", required = false) String songId) throws URISyntaxException {
        System.out.println("Delete Id=" + songId);
        if (songId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        //(@PathVariable("key") String key

        try {
            songTransactional.deleteSong(Long.parseLong(songId));
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PostMapping("/songUpt")
    public ResponseEntity<Song> updateSong(@Valid @RequestBody Song songDetails) throws URISyntaxException {
        if (songDetails.getId() == null) {
            return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);
        }

        try {
            Song result = songTransactional.updateSong(songDetails);

            return ResponseEntity.created(new URI("/song/listSong/" + result.getId())).body(result);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Song>(HttpStatus.NOT_FOUND);
        }
    }
}
