package org.urbaniak.studia.sem2.integracja.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.urbaniak.studia.sem2.integracja.dao.MusicDAO;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

@WebService(targetNamespace = "org.urbaniak.studia.sem2.integracja.service")
public class ArtistService {

    private MusicDAO musicDAO;

    @WebMethod
    public List<Artist> getArtists() {
        return musicDAO.getArtists();
    }
}
