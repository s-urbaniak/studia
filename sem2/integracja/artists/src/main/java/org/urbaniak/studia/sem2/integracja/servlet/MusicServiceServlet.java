package org.urbaniak.studia.sem2.integracja.servlet;

import java.util.List;

import javax.annotation.Resource;

import org.urbaniak.studia.sem2.integracja.client.MusicService;
import org.urbaniak.studia.sem2.integracja.entity.Artist;
import org.urbaniak.studia.sem2.integracja.service.MusicServiceImpl;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MusicServiceServlet extends RemoteServiceServlet implements
        MusicService {

    @Resource
    MusicServiceImpl musicService;

    public List<Artist> getArtists() {
        return musicService.getArtists();
    }

}