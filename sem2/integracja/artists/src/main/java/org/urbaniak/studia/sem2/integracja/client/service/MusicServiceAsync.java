package org.urbaniak.studia.sem2.integracja.client.service;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MusicServiceAsync {
    public void getArtists(AsyncCallback<List<Artist>> callback);
}