package com.confirmit.testsdkapp.layouts.defaultlayoutjava;

public class DefaultSurveySectionInfo {

    private int index;
    private int minRowIndex;
    private int maxRowIndex;
    private int rowCount;

    public DefaultSurveySectionInfo(int index, int minRowIndex, int maxRowIndex, int rowCount) {
        this.index = index;
        this.minRowIndex = minRowIndex;
        this.maxRowIndex = maxRowIndex;
        this.rowCount = rowCount;
    }

    public boolean isInSection(int position) {
        return minRowIndex <= position && position < maxRowIndex;
    }

    public int getItemIndex(int position) {
        return position - minRowIndex;
    }

    public int getItemAbsoluteIndex(int row) {
        return minRowIndex + row;
    }

    public int getIndex() {
        return index;
    }

    public int getRowCount() {
        return rowCount;
    }


}
