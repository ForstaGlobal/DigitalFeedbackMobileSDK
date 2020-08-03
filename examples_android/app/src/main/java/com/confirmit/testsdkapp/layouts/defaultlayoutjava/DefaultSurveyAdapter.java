package com.confirmit.testsdkapp.layouts.defaultlayoutjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutClickLabelCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutIconLabelCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutLabelCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutSeparatorCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells.DefaultLayoutTextBoxCell;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutClickLabel;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutIconLabel;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutLabel;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutSection;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutTextBox;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultSurveyCellType;

import java.util.ArrayList;
import java.util.List;

public class DefaultSurveyAdapter extends RecyclerView.Adapter<DefaultLayoutCell> {
    private List<Integer> sectionPositions = new ArrayList<>();
    private List<DefaultSurveySectionInfo> sectionInfos = new ArrayList<>();
    private List<DefaultLayoutSection> sections;

    public DefaultSurveyAdapter(List<DefaultLayoutSection> sections) {
        super();
        this.sections = sections;
    }

    public void refresh() {
        getItemCount();
    }

    public int getItemIndex(int section, int row) {
        return sectionInfos.get(section).getItemAbsoluteIndex(row);
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionPositions.contains(position)) {
            return DefaultSurveyCellType.PAGE_HEADER.intValue();
        } else {
            for (DefaultSurveySectionInfo sectionInfo : sectionInfos) {
                if (!sectionInfo.isInSection(position)) {
                    continue;
                }

                DefaultLayoutSection section = sections.get(sectionInfo.getIndex());
                DefaultLayoutItem item = section.getItems().get(sectionInfo.getItemIndex(position));
                if (item instanceof DefaultLayoutLabel) {
                    return DefaultSurveyCellType.LABEL.intValue();
                }
                if (item instanceof DefaultLayoutIconLabel) {
                    return DefaultSurveyCellType.ICON_LABEL.intValue();
                }
                if (item instanceof DefaultLayoutTextBox) {
                    return DefaultSurveyCellType.TEXTBOX.intValue();
                }
                if (item instanceof DefaultLayoutClickLabel) {
                    return DefaultSurveyCellType.CLICK_LABEL.intValue();
                }
            }
        }

        return DefaultSurveyCellType.PAGE_HEADER.intValue();
    }

    @NonNull
    @Override
    public DefaultLayoutCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DefaultSurveyCellType type = DefaultSurveyCellType.fromValue(viewType);
        View view;
        switch (type) {
            case PAGE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_layout_section_header, parent, false);
                return new DefaultLayoutSeparatorCell(view);
            case LABEL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_layout_label, parent, false);
                return new DefaultLayoutLabelCell(view);
            case ICON_LABEL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_layout_icon_label, parent, false);
                return new DefaultLayoutIconLabelCell(view);
            case TEXTBOX:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_layout_textbox, parent, false);
                return new DefaultLayoutTextBoxCell(view);
            case CLICK_LABEL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_layout_click_label, parent, false);
                return new DefaultLayoutClickLabelCell(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultLayoutCell holder, int position) {
        for (DefaultSurveySectionInfo sectionInfo : sectionInfos) {
            if (sectionInfo.isInSection(position)) {
                DefaultLayoutSection section = sections.get(sectionInfo.getIndex());
                DefaultLayoutItem item = section.getItems().get(sectionInfo.getItemIndex(position));
                holder.setup(item, section);
            }
        }
    }

    @Override
    public int getItemCount() {
        sectionPositions.clear();
        sectionInfos.clear();

        int count = 0;
        int minRowIndex = 1;
        int maxRowIndex = 0;
        int totalSectionCount = getTotalSectionCount();

        for (int i = 0; i < totalSectionCount; i++) {
            int rowCount = getSectionItemCount(i);
            maxRowIndex = minRowIndex + rowCount;
            sectionPositions.add(i, minRowIndex - 1);
            sectionInfos.add(i, new DefaultSurveySectionInfo(i, minRowIndex, maxRowIndex, rowCount));

            count += rowCount;

            minRowIndex = maxRowIndex + 1;
        }

        return count + totalSectionCount;
    }


    private int getTotalSectionCount() {
        return sections.size();
    }

    private int getSectionItemCount(int section) {
        return sections.get(section).getItems().size();
    }
}
