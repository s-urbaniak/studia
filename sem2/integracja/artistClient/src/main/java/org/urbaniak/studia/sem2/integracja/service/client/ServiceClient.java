package org.urbaniak.studia.sem2.integracja.service.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.urbaniak.studia.sem2.integracja.service.Artist;
import org.urbaniak.studia.sem2.integracja.service.MusicWebServiceImpl;
import org.urbaniak.studia.sem2.integracja.service.MusicWebServiceImplService;

public class ServiceClient {
    final static Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    public static void main(String[] args) {
        MusicWebServiceImplService remoteService = new MusicWebServiceImplService();
        MusicWebServiceImpl musicService = remoteService.getMusicWebServiceImplPort();
        logger.debug("Welcome to the web service client ...");
        logger.debug("fetching remote artists ...");
        List<Artist> l = musicService.fetch();
        for (Artist a : l) {
            logger.debug(new StringBuilder("id=").append(a.getId()).append(
                    ", genre=").append(a.getGenre()).append(", name=").append(
                    a.getName()).toString());
        }

        logger.debug("deleting last artist ...");
        musicService.remove(l.get(l.size()-1));

        logger.debug("adding new artist ...");
        Artist a = new Artist();
        a.setGenre("webservice");
        a.setName("webservice Name");
        musicService.add(a);
    }
}