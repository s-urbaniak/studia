package org.urbaniak.studia.sem2.integracja.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.urbaniak.studia.sem2.integracja.client.service.ArtistService;
import org.urbaniak.studia.sem2.integracja.dao.MusicDAO;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

/**
 * @author sur
 * 
 *         Artist service. Provides CRUD as well as business methods for
 *         Artists.
 */
@Transactional
public class ArtistServiceImpl implements ArtistService {

    @Resource
    private MusicDAO musicDAO;

    public void setMusicDAO(MusicDAO musicDAO) {
        this.musicDAO = musicDAO;
    }

    public Artist add(Artist record) {
        Artist result = musicDAO.saveArtist(record);
        return result;
    }

    public List<Artist> fetch() {
        List<Artist> result = musicDAO.getArtists();
        return result;
    }

    public void remove(Artist record) {
        musicDAO.removeArtist(record);
    }

    public Artist update(Artist record) {
        Artist result = musicDAO.saveArtist(record);
        return result;
    }
}