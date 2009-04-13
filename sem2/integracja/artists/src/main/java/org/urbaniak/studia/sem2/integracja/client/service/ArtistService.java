package org.urbaniak.studia.sem2.integracja.client.service;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ArtistService extends RemoteService {

    List<Artist> fetch();

    Artist add(Artist record);

    Artist update(Artist record);

    void remove(Artist record);

}