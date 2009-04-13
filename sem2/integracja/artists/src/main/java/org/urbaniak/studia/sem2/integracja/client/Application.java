package org.urbaniak.studia.sem2.integracja.client;

import org.urbaniak.studia.sem2.integracja.client.datasource.ArtistDataSource;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class Application implements EntryPoint {

    public void onModuleLoad() {
        final ListGrid listGrid = new ListGrid();

        ListGridField id = new ListGridField("id", 100);
        id.setType(ListGridFieldType.INTEGER);

        ListGridField name = new ListGridField("name", 100);
        id.setType(ListGridFieldType.TEXT);

        ListGridField genre = new ListGridField("genre", 100);
        id.setType(ListGridFieldType.TEXT);

        listGrid.setFields(id, name, genre);

        listGrid.setWidth100();
        listGrid.setHeight100();

        DataSource ds = new ArtistDataSource();
        listGrid.setDataSource(ds);

        listGrid.fetchData();
        listGrid.draw();
    }
}