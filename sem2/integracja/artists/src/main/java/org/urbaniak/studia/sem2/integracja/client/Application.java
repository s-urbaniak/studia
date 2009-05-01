package org.urbaniak.studia.sem2.integracja.client;

import org.urbaniak.studia.sem2.integracja.client.datasource.ArtistDataSource;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripSeparator;

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

        final DataSource datasource = new ArtistDataSource();
        listGrid.setDataSource(datasource);

        final DynamicForm form = new DynamicForm();
        form.setTitle("Test");
        form.setShowResizeBar(true);
        form.setResizeBarTarget("next");

        TextItem idInput = new TextItem("id");
        idInput.setDisabled(true);
        TextItem nameInput = new TextItem("name");
        TextItem genreInput = new TextItem("genre");

        form.setFields(idInput, nameInput, genreInput);
        form.setDataSource(datasource);

        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
            public void onSelectionChanged(SelectionEvent event) {
                form.editSelectedData(listGrid);
            }
        });

        listGrid.fetchData();

        Layout editAreaLayout = new HLayout();
        editAreaLayout.addMember(form);
        editAreaLayout.addMember(listGrid);

        ToolStrip toolStrip = new ToolStrip();

        ImgButton newToolButton = new ImgButton();
        newToolButton.setSize(32);
        newToolButton.setSrc("icons/document-new.png");
        newToolButton.setShowFocused(false);
        newToolButton.setShowRollOver(false);
        newToolButton.setShowFocusedAsOver(false);
        newToolButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                form.editNewRecord();
            }
        });

        ImgButton saveToolButton = new ImgButton();
        saveToolButton.setSize(32);
        saveToolButton.setSrc("icons/document-save.png");
        saveToolButton.setShowFocused(false);
        saveToolButton.setShowRollOver(false);
        saveToolButton.setShowFocusedAsOver(false);
        saveToolButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                form.saveData();
            }
        });

        ToolStripSeparator stripSeparator = new ToolStripSeparator();
        stripSeparator.setHeight(28);

        ImgButton deleteToolButton = new ImgButton();
        deleteToolButton.setSize(32);
        deleteToolButton.setSrc("icons/user-trash.png");
        deleteToolButton.setShowFocused(false);
        deleteToolButton.setShowRollOver(false);
        deleteToolButton.setShowFocusedAsOver(false);
        deleteToolButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                datasource.removeData(listGrid.getSelectedRecord());
                form.editNewRecord();
            }
        });

        toolStrip.addMember(newToolButton);
        toolStrip.addMember(saveToolButton);
        toolStrip.addMember(stripSeparator);
        toolStrip.addMember(deleteToolButton);

        Layout windowLayout = new VLayout();
        windowLayout.addMember(toolStrip);
        windowLayout.addMember(editAreaLayout);
        windowLayout.setWidth("100%");
        windowLayout.setHeight("100%");
        windowLayout.draw();
    }
}