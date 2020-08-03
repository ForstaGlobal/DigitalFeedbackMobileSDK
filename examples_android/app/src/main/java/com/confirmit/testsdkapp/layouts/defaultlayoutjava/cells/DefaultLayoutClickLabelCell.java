package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.confirmit.testsdkapp.AppConfigs;
import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutClickLabel;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutClickLabelType;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;

public class DefaultLayoutClickLabelCell extends DefaultLayoutCell implements View.OnClickListener {
    private DefaultLayoutCellChangeListener listener;
    private DefaultLayoutClickLabel item;
    private Boolean itemSelected;
    private ConstraintLayout btnContainer;
    private TextView lblText;
    private ImageView imgIcon;

    public DefaultLayoutClickLabelCell(View view) {
        super(view);
        itemSelected = false;
        btnContainer = view.findViewById(R.id.containerButton);
        btnContainer.setOnClickListener(this);
        lblText = view.findViewById(R.id.txtLabel);
        imgIcon = view.findViewById(R.id.imgIcon);
    }

    @Override
    public void setup(DefaultLayoutItem item, DefaultLayoutCellChangeListener listener) {
        this.item = (DefaultLayoutClickLabel) item;
        this.listener = listener;

        if (this.item.getAnswer().getText().getHasStyle() && AppConfigs.INSTANCE.getHtmlText()) {
            lblText.setText(this.item.getAnswer().getText().getSpanned());
        } else {
            lblText.setText(this.item.getAnswer().getText().get());
        }

        float density = lblText.getResources().getDisplayMetrics().density;
        ConstraintSet set = new ConstraintSet();
        set.clone(btnContainer);
        set.setMargin(imgIcon.getId(), ConstraintSet.START, (int) (density * 24 + this.item.getIndent() * (density * 24)));
        set.applyTo(btnContainer);

        itemSelected = listener.getSelected(this.item.getAnswer());

        setIcon();
    }

    @Override
    public void onClick(View view) {
        if (itemSelected && item.getClickLabelType() == DefaultLayoutClickLabelType.RADIO) {
            return;
        }

        itemSelected = !itemSelected;
        setIcon();
        switch (item.getClickLabelType()) {
            case CHECKBOX:
                listener.onCheckClicked(item.getAnswer(), itemSelected);
                break;
            case RADIO:
                listener.onRadioClicked(item.getAnswer());
                break;
        }
    }

    private void setIcon() {
        int imageId;
        switch (item.getClickLabelType()) {
            case CHECKBOX:
                imageId = itemSelected ? R.drawable.ic_outline_check_black : R.drawable.ic_outline_box_black;
                break;
            default:
                imageId = itemSelected ? R.drawable.ic_radio_black : R.drawable.ic_radio_unchecked_black;
                break;
        }

        imgIcon.setImageResource(imageId);
        imgIcon.setColorFilter(ContextCompat.getColor(imgIcon.getContext(), itemSelected ? R.color.defaultSurveyButton : R.color.font));
    }
}
