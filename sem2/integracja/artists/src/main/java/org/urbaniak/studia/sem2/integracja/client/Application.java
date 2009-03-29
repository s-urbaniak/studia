package org.urbaniak.studia.sem2.integracja.client;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.smartgwt.client.util.SC;

public class Application implements EntryPoint {

    public void onModuleLoad() {
        MusicServiceAsync musicService = GWT.create(MusicService.class);

        ServiceDefTarget endpoint = (ServiceDefTarget) musicService;
        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
                + "../dispatcher/music.rpc");

        AsyncCallback<List<Artist>> callback = new AsyncCallback<List<Artist>>() {
            public void onSuccess(List<Artist> result) {
            }

            public void onFailure(Throwable ex) {
                ex.printStackTrace();
            }
        };

        musicService.getArtists(callback);
        SC.say("Hello SmartGWT");
    }

}