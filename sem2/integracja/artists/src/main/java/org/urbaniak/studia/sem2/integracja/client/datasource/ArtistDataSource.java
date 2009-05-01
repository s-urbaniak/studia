package org.urbaniak.studia.sem2.integracja.client.datasource;

import java.util.List;

import org.urbaniak.studia.sem2.integracja.client.service.ArtistService;
import org.urbaniak.studia.sem2.integracja.client.service.ArtistServiceAsync;
import org.urbaniak.studia.sem2.integracja.entity.Artist;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ArtistDataSource extends GwtRpcDataSource {

    public ArtistDataSource() {
        DataSourceField field;
        field = new DataSourceIntegerField("id", "ID");
        field.setPrimaryKey(true);
        // AutoIncrement on server.
        field.setRequired(false);
        addField(field);

        field = new DataSourceTextField("genre", "Genre");
        field.setRequired(true);
        addField(field);

        field = new DataSourceTextField("name", "Name");
        field.setRequired(false);
        addField(field);
    }

    @Override
    protected void executeFetch(final String requestId,
            final DSRequest request, final DSResponse response) {
        // These can be used as parameters to create paging.
        // request.getStartRow ();
        // request.getEndRow ();
        // request.getSortBy ();
        ArtistServiceAsync service = getService();

        service.fetch(new AsyncCallback<List<Artist>>() {
            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(List<Artist> result) {
                ListGridRecord[] list = new ListGridRecord[result.size()];
                for (int i = 0; i < list.length; i++) {
                    ListGridRecord record = new ListGridRecord();
                    copyValues(result.get(i), record);
                    list[i] = record;
                }
                response.setData(list);
                processResponse(requestId, response);
            }
        });
    }

    @Override
    protected void executeAdd(final String requestId, final DSRequest request,
            final DSResponse response) {
        // Retrieve record which should be added.
        JavaScriptObject data = request.getData();
        ListGridRecord rec = new ListGridRecord(data);
        Artist testRec = new Artist();
        copyValues(rec, testRec);

        ArtistServiceAsync service = getService();

        service.add(testRec, new AsyncCallback<Artist>() {
            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(Artist result) {
                ListGridRecord[] list = new ListGridRecord[1];
                ListGridRecord newRec = new ListGridRecord();
                copyValues(result, newRec);
                list[0] = newRec;
                response.setData(list);
                processResponse(requestId, response);
            }
        });
    }

    @Override
    protected void executeUpdate(final String requestId,
            final DSRequest request, final DSResponse response) {
        // Retrieve record which should be updated.
        JavaScriptObject jsData = request.getData();
        DataClass data = new DataClass(jsData);

        Artist artist = new Artist();
        copyValues(data, artist);

        ArtistServiceAsync service = getService();
        service.update(artist, new AsyncCallback<Artist>() {

            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(Artist result) {
                ListGridRecord[] list = new ListGridRecord[1];
                ListGridRecord updRec = new ListGridRecord();
                copyValues(result, updRec);
                list[0] = updRec;
                response.setData(list);
                processResponse(requestId, response);
            }

        });
    }

    @Override
    protected void executeRemove(final String requestId,
            final DSRequest request, final DSResponse response) {

        JavaScriptObject data = request.getData();
        final ListGridRecord rec = new ListGridRecord(data);

        Artist testRec = new Artist();
        copyValues(rec, testRec);

        ArtistServiceAsync service = getService();

        service.remove(testRec, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                response.setStatus(RPCResponse.STATUS_FAILURE);
                processResponse(requestId, response);
            }

            public void onSuccess(Object result) {
                ListGridRecord[] list = new ListGridRecord[1];
                // We do not receive removed record from server.
                // Return record from request.
                list[0] = rec;
                response.setData(list);
                processResponse(requestId, response);
            }

        });
    }

    private ArtistServiceAsync getService() {
        ArtistServiceAsync service = GWT.create(ArtistService.class);

        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL()
                + "../dispatcher/artist.rpc");

        return service;
    }

    private static void copyValues(DataClass from, Artist to) {
        to.setId(from.getAttributeAsInt("id"));
        to.setName(from.getAttributeAsString("name"));
        to.setGenre(from.getAttributeAsString("genre"));
    }

    private static void copyValues(Artist from, DataClass to) {
        to.setAttribute("id", from.getId());
        to.setAttribute("name", from.getName());
        to.setAttribute("genre", from.getGenre());
    }
}