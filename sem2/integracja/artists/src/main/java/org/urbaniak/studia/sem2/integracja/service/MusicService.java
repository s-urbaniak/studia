package org.urbaniak.studia.sem2.integracja.service;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.urbaniak.studia.sem2.integracja.dao.MusicDAO;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

/**
 * @author sur
 *
 * Service for operations on the Artist/Track/Record domain objects
 */
@WebService
public class MusicService {
    @Resource
    private MusicDAO musicDAO;

    /**
     * @see MusicDAO#getArtists()
     */
    @WebMethod
    public List<Artist> getArtists() {
        return musicDAO.getArtists();
    }
}