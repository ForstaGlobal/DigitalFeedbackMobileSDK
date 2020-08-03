package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.confirmit.testsdkapp.AppConfigs;
import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutLabel;

public class DefaultLayoutLabelCell extends DefaultLayoutCell {
    private DefaultLayoutLabel item;
    private TextView lblText;
    private ConstraintLayout container;

    public DefaultLayoutLabelCell(View view) {
        super(view);
        lblText = view.findViewById(R.id.txtLabel);
        container = view.findViewById(R.id.container);
    }

    @Override
    public void setup(DefaultLayoutItem item, DefaultLayoutCellChangeListener listener) {
        this.item = (DefaultLayoutLabel) item;

        float density = lblText.getResources().getDisplayMetrics().density;
        ConstraintSet set = new ConstraintSet();
        set.clone(container);
        set.setMargin(lblText.getId(), ConstraintSet.START, (int) (density * 24 + this.item.getIndent() * (density * 24)));
        set.applyTo(container);

        switch (this.item.getLabelType()) {
            case TEXT:
                lblText.setTypeface(null, Typeface.NORMAL);
                lblText.setTextSize(16f);
                lblText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                break;
            case INSTRUCTION:
                lblText.setTypeface(null, Typeface.NORMAL);
                lblText.setTextSize(14f);
                lblText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                break;
            case PAGE_TITLE:
                lblText.setTypeface(null, Typeface.BOLD);
                lblText.setTextSize(16f);
                lblText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                break;
        }

        if (this.item.getValue().getHasStyle() && AppConfigs.INSTANCE.getHtmlText()) {
            lblText.setText(this.item.getValue().getSpanned());
        } else {
            lblText.setText(this.item.getValue().get());
        }
    }
}
