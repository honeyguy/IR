package com.ill.transactional;

import com.google.common.collect.Lists;
import com.ill.exception.ResourceNotFoundException;
import com.ill.model.Song;
import com.ill.repository.SingerRepository;
import com.ill.repository.SongRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SongTransactional implements Filter {
    private SongRepository songRepository;
    @Autowired
    private SingerRepository singerRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Max-Age", "3600");
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Autowired
    public SongTransactional(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    private static final Logger log = LogManager.getLogger(SongTransactional.class);

    public List<List<Song>> getAllSong() {
        List<Song> songLst = songRepository.findAll();
        List<List<Song>> resultLst = null;
        int size = songLst.size();
        int elem_index = (size - 1) / 3;

        resultLst = Lists.partition(songLst, elem_index);
        //resultLst = songLst.stream().limit(5) .collect(Collectors.toList());

        return resultLst;
    }

    public List<List<Song>> getAllSongByYear(String year) {
        log.debug("Inside getAllSongByYear, year= " + year);
        List<Song> songLst = songRepository.findAll();
        List<List<Song>> resultLst = null;
        int size = songLst.size();
        int elem_index = (size - 1) / 3;

        List<Song> songSearchLst = songLst.stream()
                .filter(song -> song.getYear().equals(year))
                .collect(Collectors.toList());

        resultLst = Lists.partition(songSearchLst, elem_index);

        return resultLst;
    }


    public List<List<Song>> getAllSongBySinger(String singer) {
        log.debug("getAllSongBySinger: singer = " + singer);
        List<Song> songLst = songRepository.findAll();
        List<List<Song>> resultLst = null;
        int size = songLst.size();
        int elem_index = (size - 1) / 3;

      /*  String[] splitted = Arrays.stream(input.split(","))
                .map(String::trim)
                .toArray(String[]::new);*/

        if(singer.contains(",")) {
            String singerName[]  =  singer.split(",");

            songLst = songLst.stream( )
                    .filter(song -> song.getSinger().contains(singerName[0]) )
                    .collect(Collectors.toList())
                    .stream().filter(song -> song.getSinger().contains(singerName[1]))
                    .collect(Collectors.toList());
        }
        else {
            songLst = songLst.stream()
                    .filter(song -> song.getSinger().contains(singer) )
                    .collect(Collectors.toList());
        }

        songLst.sort(Comparator.comparing(Song::getTitle));

        resultLst = Lists.partition(songLst, elem_index);

        return resultLst;
    }



    public Song createSong(Song song) {
        if (song.getId() != null && songRepository.existsById(song.getId())) {
            throw new EntityExistsException("There is already existing entity with such ID in the database.");
        }

        return songRepository.save(song);
    }

    public Song updateSong(Song songDetails) {
        if (songDetails.getId() != null && !songRepository.existsById(songDetails.getId())) {
            throw new EntityNotFoundException("There is no entity with such ID in the database.");
        }

        Song song = songRepository.findById(songDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Song", "id", songDetails.getId()));

        Song updatedSong = songRepository.save(songDetails);

        return updatedSong;
    }

}


