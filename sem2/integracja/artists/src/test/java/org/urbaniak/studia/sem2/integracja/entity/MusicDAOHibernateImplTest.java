package org.urbaniak.studia.sem2.integracja.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.urbaniak.studia.sem2.integracja.dao.MusicDAO;
import org.urbaniak.studia.sem2.integracja.service.MusicServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/org/urbaniak/studia/sem2/integracja/service/service-context.xml",
        "classpath:/org/urbaniak/studia/sem2/integracja/dao/dao-context.xml",
        "classpath:/test-context.xml" })
public class MusicDAOHibernateImplTest {
    @Resource
    private MusicDAO musicDAO;

    @Resource
    private MusicServiceImpl musicService;

    private static Logger logger = Logger
            .getLogger(MusicDAOHibernateImplTest.class.getName());

    /**
     * Simple tests excersize the various methods of MusicDAO
     */
    @Test
    public void testInserts() {
        String artistName = "Tenacious D";
        Artist artist = new Artist();
        artist.setName(artistName);
        artist.setGenre("Comedy");
        musicDAO.saveArtist(artist);
        assertNotNull(artist.getId());

        // Save an additional Artist
        Artist artist2 = new Artist();
        artist2.setName("Spinal Tap");
        artist2.setGenre("Mock Rock");
        musicDAO.saveArtist(artist2);

        // Test "loadAll"
        List<Artist> loadedArtists = musicDAO.getArtists();
        assertEquals(2, loadedArtists.size());
        for (Artist a : loadedArtists) {
            logger.info(a.toString());
        }

        // Create some records
        String record1Title = "The Pick of Destiny";
        String[] record1Tracks = { "Kickapoo", "Classico", "Baby", "Destiny",
                "History" };
        String record2Title = "Tenacious D";
        String[] record2Tracks = { "Tribute", "Wonderboy", "Dio" };

        Record record1 = createRecord(artist, record1Title, record1Tracks);
        Record record2 = createRecord(artist, record2Title, record2Tracks);

        // Load back artist1
        Artist artistOne = musicDAO.getArtistById(artist.getId());
        assertEquals("Tenacious D", artistOne.getName());
        assertEquals("Comedy", artistOne.getGenre());

        // Load back record 1
        Record recordOne = musicDAO.getRecordById(record1.getId());
        assertEquals(record1Title, recordOne.getTitle());
        assertEquals(artistName, recordOne.getArtist().getName());

        assertEquals(record1Tracks[0], recordOne.getTracks().get(0).getTitle());
        assertEquals(record1Tracks[1], recordOne.getTracks().get(1).getTitle());

        // Load back record 2
        Record recordTwo = musicDAO.getRecordById(record2.getId());
        assertEquals(record2Title, recordTwo.getTitle());
        assertEquals(artistName, recordTwo.getArtist().getName());

        assertEquals(record2Tracks[0], recordTwo.getTracks().get(0).getTitle());
        assertEquals(record2Tracks[1], recordTwo.getTracks().get(1).getTitle());

        // Test Loading a Track By Id and Make sure that all associations exist
        Track loadedTrack = musicDAO.getTrackById(recordTwo.getTracks().get(1)
                .getId());
        assertEquals("Wonderboy", loadedTrack.getTitle());
        assertEquals("Tenacious D", loadedTrack.getRecord().getTitle());
        assertEquals("Tenacious D", loadedTrack.getRecord().getArtist()
                .getName());
        assertEquals("Dio", loadedTrack.getRecord().getTracks().get(2)
                .getTitle());

        // Test "searchRecordsByTitle"
        List<Record> recordsSearch1 = musicDAO.searchRecordsByTitle("Destiny");
        assertEquals(1, recordsSearch1.size());

        List<Record> recordsSearch2 = musicDAO.searchRecordsByTitle("e");
        logger.info("Searched records: " + recordsSearch2);
        assertEquals(2, recordsSearch2.size());
    }

    @Test
    public void testMusicService() {
        List<Artist> artists = musicService.getArtists();
        assertNotNull(artists);
    }

    @Test
    public void testWebService() {
        Endpoint endpoint = Endpoint.create(musicService);
        endpoint.publish("http://localhost:9999/MusicService");
    }

    private Record createRecord(Artist artist, String title, String[] tracks) {
        Record record = new Record();
        record.setArtist(artist);
        record.setTitle(title);

        List<Track> tracks1 = new ArrayList<Track>();
        for (String trackTitle : tracks) {
            Track track = new Track();
            track.setTitle(trackTitle);
            track.setRecord(record);
            tracks1.add(track);
        }

        record.setTracks(tracks1);
        musicDAO.saveRecord(record);
        assertNotNull(record.getId());
        return record;
    }
}