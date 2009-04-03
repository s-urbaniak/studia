package org.urbaniak.studia.sem2.integracja.client;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MusicService extends RemoteService {
    public List<Artist> getArtists();
}