package org.urbaniak.studia.sem2.integracja.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Application implements EntryPoint {

    public void onModuleLoad() {
        RootPanel root = RootPanel.get();
        Label newLabel = new Label("This is a test label ...");
        root.add(newLabel);
    }

}