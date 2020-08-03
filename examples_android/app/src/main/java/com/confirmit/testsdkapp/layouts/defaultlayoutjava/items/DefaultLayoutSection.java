package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutCellChangeListener;

import java.util.List;

public interface DefaultLayoutSection extends DefaultLayoutCellChangeListener {
    List<DefaultLayoutItem> getItems();

    void load(DefaultLayoutSectionListener listener, int section);
}
