package com.confirmit.testsdkapp.layouts.defaultlayoutjava.items;

public class IndexPath {
    private int row;
    private int section;

    IndexPath(int row, int section) {
        this.row = row;
        this.section = section;
    }

    public int getRow() {
        return row;
    }

    public int getSection() {
        return section;
    }
}
