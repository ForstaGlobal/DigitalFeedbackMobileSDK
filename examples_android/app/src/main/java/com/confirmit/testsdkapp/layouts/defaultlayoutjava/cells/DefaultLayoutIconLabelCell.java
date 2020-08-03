package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.confirmit.mobilesdk.ui.ValidationQuestionError;
import com.confirmit.testsdkapp.AppConfigs;
import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutIconLabel;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;

import java.util.List;

public class DefaultLayoutIconLabelCell extends DefaultLayoutCell {

    private DefaultLayoutIconLabel item;
    private ImageView imgIcon;
    private TextView lblText;

    public DefaultLayoutIconLabelCell(View view) {
        super(view);
        lblText = view.findViewById(R.id.txtLabel);
        imgIcon = view.findViewById(R.id.imgIcon);
    }

    @Override
    public void setup(DefaultLayoutItem item, DefaultLayoutCellChangeListener listener) {
        this.item = (DefaultLayoutIconLabel) item;

        Context context = imgIcon.getContext();
        imgIcon.setImageResource(this.item.getIconId());
        if (!this.item.getErrors().isEmpty()) {
            lblText.setText(getErrorMessage(this.item.getErrors()));
            lblText.setTextColor(ContextCompat.getColor(context, R.color.defaultSurveyError));
            imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyError));
        } else {
            imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.defaultSurveyFont));
            lblText.setTextColor(ContextCompat.getColor(context, R.color.defaultSurveyFont));

            if (this.item.getValue().getHasStyle() && AppConfigs.INSTANCE.getHtmlText()) {
                lblText.setText(this.item.getValue().getSpanned());
            } else {
                lblText.setText(this.item.getValue().get());
            }
        }
    }

    private String getErrorMessage(List<ValidationQuestionError> errors) {
        StringBuilder result = new StringBuilder();
        for (ValidationQuestionError error : errors) {
            result.append(error.getMessage()).append("\n");
        }

        return result.toString();
    }
}
