package org.urbaniak.studia.sem2.integracja.service;

import java.util.ArrayList;
import java.util.List;

import org.urbaniak.studia.sem2.integracja.client.service.ArtistService;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

public class ArtistServiceImplMock implements ArtistService {

    private static List<Artist> list;

    static {
        list = new ArrayList<Artist>();
        Artist entry;

        entry = new Artist();
        entry.setId(1);
        entry.setName("name 1");
        entry.setGenre("rock");
        list.add(entry);

        entry = new Artist();
        entry.setId(2);
        entry.setName("name 2");
        entry.setGenre("pop");
        list.add(entry);

        entry = new Artist();
        entry.setId(3);
        entry.setName("Sergiusz Urbaniak");
        entry.setGenre("rock");
        list.add(entry);

        entry = new Artist();
        entry.setId(4);
        entry.setName("Madonna");
        entry.setGenre("pop");
        list.add(entry);

        entry = new Artist();
        entry.setId(5);
        entry.setName("Dire Straits");
        entry.setGenre("rock");
        list.add(entry);

        entry = new Artist();
        entry.setId(6);
        entry.setName("Frank Zegler");
        entry.setGenre("classical guitar");
        list.add(entry);
    }

    public Artist add(Artist record) {
        list.add(record);
        return record;
    }

    public List<Artist> fetch() {
        return list;
    }

    public void remove(Artist record) {
    }

    public Artist update(Artist record) {
        return record;
    }

}