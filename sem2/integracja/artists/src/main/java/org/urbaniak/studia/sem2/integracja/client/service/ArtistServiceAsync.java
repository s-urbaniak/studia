package org.urbaniak.studia.sem2.integracja.client.service;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ArtistServiceAsync {

    public abstract void fetch (AsyncCallback<List<Artist>> asyncCallback);

    public abstract void add (Artist record, AsyncCallback<Artist> asyncCallback);

    public abstract void update (Artist record, AsyncCallback<Artist> asyncCallback);

    public abstract void remove (Artist record, AsyncCallback asyncCallback);
}