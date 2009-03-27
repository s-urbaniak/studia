package org.urbaniak.studia.sem2.integracja.client;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Application implements EntryPoint {

    public void onModuleLoad() {
        MusicServiceAsync musicService = GWT.create(MusicService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) musicService;

        System.out.println(GWT.getModuleBaseURL());

        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
                + "../dispatcher/music.rpc");

        AsyncCallback<List<Artist>> callback = new AsyncCallback<List<Artist>>() {
            public void onSuccess(List<Artist> result) {
                System.out.println(result);
            }

            public void onFailure(Throwable ex) {
                ex.printStackTrace();
            }
        };

        musicService.getArtists(callback);

        RootPanel root = RootPanel.get();
        Label newLabel = new Label("Changed label ...");
        root.add(newLabel);
    }

}