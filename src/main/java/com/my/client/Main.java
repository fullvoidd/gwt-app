package com.my.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.my.client.frames.*;
import com.my.client.service.SpringService;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.*;

public class Main implements EntryPoint {
    private final VerticalPanel mainPanel = new VerticalPanel();
    private final HorizontalPanel topPanel = new HorizontalPanel();
    private final HorizontalPanel bottomPanel = new HorizontalPanel();
    private final Tree tree = new Tree();
    private final ScrollPanel treeBox = new ScrollPanel(tree);
    private final ListBox listBox = new ListBox();

    public void onModuleLoad() {
        Defaults.setServiceRoot(GWT.getHostPageBaseURL());

        mainPanel.setStyleName("topPanel");

        listBox.getElement().setDraggable(Element.DRAGGABLE_TRUE);

        mainPanel.add(topPanel);
        topPanel.setSpacing(10);
        addListBoxAndButtons();
        MoveBoxToBoxButtons.addButtonsForMoveBoxToBox(listBox, tree, topPanel);
        addTreeBoxAndButtons();

        bottomPanel.setWidth("830px");
        mainPanel.add(bottomPanel);
        mainPanel.setCellHorizontalAlignment(bottomPanel, HasHorizontalAlignment.ALIGN_LEFT);
        SaveAndLoadButtons.addSaveAndLoadButtons(listBox, tree, treeBox, bottomPanel);

        RootPanel.get().add(mainPanel);
        RootPanel.get().setWidgetPosition(mainPanel, 320, 150);
    }

    public void addListBoxAndButtons() {
        MoveInBoxButtons.createButtonsForMoveElementsInListBox(listBox, topPanel);

        VerticalPanel vPanel = new VerticalPanel();
        HorizontalPanel hPanel = new HorizontalPanel();
        vPanel.setSpacing(7);
        hPanel.setSpacing(5);
        final FileUploadField uploader = new FileUploadField();
        uploader.setWidth("20em");

        final FormPanel formPanel = new FormPanel();
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setAction("controllers/parseFile");
        formPanel.add(uploader);
        hPanel.add(formPanel);

        addSubmitButton(hPanel, formPanel);
        vPanel.add(hPanel);

        listBox.setStyleName("listBox");
        listBox.setSize("25em", "30em");
        listBox.setVisibleItemCount(25);
        vPanel.add(listBox);
        topPanel.add(vPanel);
    }

    public void addSubmitButton(HorizontalPanel hPanel, final FormPanel formPanel) {
        Button button = new Button("Submit", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                formPanel.submit();
            }
        });
        button.setHeight("24px");
        button.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        hPanel.add(button);

        formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                SpringService service = GWT.create(SpringService.class);
                service.uploadFile(new MethodCallback<List<String>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert(exception.getMessage());
                    }

                    @Override
                    public void onSuccess(Method method, List<String> response) {
                        for (String str : response) {
                            listBox.addItem(str);
                        }
                    }
                });
            }
        });
    }

    public void addTreeBoxAndButtons() {
        VerticalPanel treeVPanel = new VerticalPanel();
        treeVPanel.setSpacing(10);
        Button cleanAllButton = new Button("Очистить все списки", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                listBox.clear();
                tree.clear();
            }
        });
        treeVPanel.add(cleanAllButton);
        treeVPanel.setCellHorizontalAlignment(cleanAllButton, HasHorizontalAlignment.ALIGN_RIGHT);

        tree.addTextItem("Пушкин").addTextItem("Евгений Онегин");
        tree.getItem(0).addTextItem("Сказка");
        tree.addTextItem("Лермонтов").getElement();
        treeBox.setSize("25em", "29em");
        treeBox.setStyleName("treeBox");

        treeVPanel.add(treeBox);
        topPanel.add(treeVPanel);

        MoveInBoxButtons.createButtonsForMoveElementsInTreeBox(tree, topPanel);

    }

}
