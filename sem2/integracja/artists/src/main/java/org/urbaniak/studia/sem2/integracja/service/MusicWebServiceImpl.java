package org.urbaniak.studia.sem2.integracja.service;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.urbaniak.studia.sem2.integracja.client.service.ArtistService;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

/**
 * @author sur
 * 
 *         Service for operations on the Artist/Track/Record domain objects
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class MusicWebServiceImpl {
    @Resource
    private ArtistService artistService;

    public void setArtistService(ArtistService artistService) {
        this.artistService = artistService;
    }

    @WebMethod
    public Artist add(Artist record) {
        return this.artistService.add(record);
    }

    @WebMethod
    public List<Artist> fetch() {
        return this.artistService.fetch();
    }

    @WebMethod
    public void remove(Artist record) {
        this.artistService.remove(record);
    }

    @WebMethod
    public Artist update(Artist record) {
        return this.artistService.update(record);
    }
}