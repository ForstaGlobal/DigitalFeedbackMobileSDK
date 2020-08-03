package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;

public abstract class DefaultLayoutCell extends RecyclerView.ViewHolder {

    public DefaultLayoutCell(View view) {
        super(view);
    }

    public abstract void setup(DefaultLayoutItem item, DefaultLayoutCellChangeListener listener);
}
