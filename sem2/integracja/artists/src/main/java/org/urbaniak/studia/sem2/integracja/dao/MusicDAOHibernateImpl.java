package org.urbaniak.studia.sem2.integracja.dao;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.stat.Statistics;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.urbaniak.studia.sem2.integracja.entity.Artist;
import org.urbaniak.studia.sem2.integracja.entity.Record;
import org.urbaniak.studia.sem2.integracja.entity.Track;

/**
 * Implementation of MusicDAO implemented by extending Spring's
 * HibernateDaoSupport
 * 
 * @author sur
 */
public class MusicDAOHibernateImpl extends DAOHibernateImpl implements MusicDAO {

    private static Logger logger = Logger.getLogger(MusicDAOHibernateImpl.class
            .getName());

    public void getStatistics() {
        Statistics stats = getHibernateTemplate().getSessionFactory()
                .getStatistics();
    }

    public List<Artist> getArtists() {
        return getHibernateTemplate().loadAll(Artist.class);
    }

    public Artist getArtistById(Integer id) {
        Artist artist = (Artist) getHibernateTemplate().load(Artist.class, id);
        logger.fine("Got artist: " + artist);
        return artist;
    }

    public Artist saveArtist(Artist artist) {
        getHibernateTemplate().saveOrUpdate(artist);
        return artist;
    }

    public void removeArtist(Artist artist) {
        getHibernateTemplate().delete(artist);
    }

    public Record getRecordById(Integer id) {
        Record record = (Record) getHibernateTemplate().load(Record.class, id);
        logger.fine("Got record: " + record);
        return record;
    }

    public void saveOrUpdateArtistList(List<Artist> list) {
        this.saveOrUpdateBatch(list);
    }

    public Record saveRecord(Record record) {
        getHibernateTemplate().saveOrUpdate(record);
        return record;
    }

    public Track getTrackById(Integer id) {
        Track track = (Track) getHibernateTemplate().load(Track.class, id);
        logger.fine("Got track: " + track);
        return track;
    }

    public List<Record> searchRecordsByTitle(String title) {
        return getHibernateTemplate().findByNamedQuery("record.titleLike",
                "%" + title + "%");
    }
}