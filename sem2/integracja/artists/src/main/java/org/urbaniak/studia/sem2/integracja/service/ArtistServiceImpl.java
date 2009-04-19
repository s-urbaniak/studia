package org.urbaniak.studia.sem2.integracja.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.urbaniak.studia.sem2.integracja.dao.MusicDAO;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

/**
 * @author sur
 * 
 * Artist service. Provides CRUD as well as business methods for Artists.
 */
@Component
public class ArtistServiceImpl implements
        org.urbaniak.studia.sem2.integracja.client.service.ArtistService {

    @Resource
    private MusicDAO musicDAO;

    public MusicDAO getMusicDAO() {
        return musicDAO;
    }

    public void setMusicDAO(MusicDAO musicDAO) {
        this.musicDAO = musicDAO;
    }

    /* (non-Javadoc)
     * @see org.urbaniak.studia.sem2.integracja.client.service.ArtistService#add(org.urbaniak.studia.sem2.integracja.entity.Artist)
     */
    public Artist add(Artist record) {
        Artist result = musicDAO.saveArtist(record);
        return result;
    }

    /* (non-Javadoc)
     * @see org.urbaniak.studia.sem2.integracja.client.service.ArtistService#fetch()
     */
    public List<Artist> fetch() {
        List<Artist> result = musicDAO.getArtists();
        return result;
    }

    public void remove(Artist record) {
        musicDAO.removeArtist(record);
    }

    public Artist update(Artist record) {
        Artist result = musicDAO.saveArtist(record);
        return record;
    }

}
