package org.urbaniak.studia.sem2.integracja.dao;

import java.util.Collection;
import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;
import org.urbaniak.studia.sem2.integracja.entity.Record;
import org.urbaniak.studia.sem2.integracja.entity.Track;

public interface MusicDAO {
    public Collection<Artist> getArtists();

    /**
     * Get a Artist Object given the id
     * 
     * @param id
     */
    public Artist getArtistById(Integer id);

    /**
     * Save an Artist Object
     */
    public Artist saveArtist(Artist artist);

    /**
     * Get a Record given the id
     * 
     * @param id
     */
    public Record getRecordById(Integer id);

    /**
     * Save a record
     * 
     * @param record
     */
    public Record saveRecord(Record record);

    /**
     * Get a Track given the id
     * 
     * @param id
     * @return
     */
    public Track getTrackById(Integer id);

    /**
     * Search records given the title of the record
     * 
     * @param title
     */
    public List<Record> searchRecordsByTitle(String title);
}
