package com.confirmit.testsdkapp.layouts.defaultlayoutjava.cells;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutItem;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutTextBox;

public class DefaultLayoutTextBoxCell extends DefaultLayoutCell implements TextWatcher {
    private DefaultLayoutCellChangeListener listener;
    private DefaultLayoutTextBox item;
    private int viewId;
    private EditText txtText;
    private ConstraintLayout container;

    public DefaultLayoutTextBoxCell(View view) {
        super(view);
        viewId = view.getId();
        txtText = view.findViewById(R.id.editText);
        txtText.setBackgroundResource(R.drawable.round_corner_survey_textedit);
        txtText.addTextChangedListener(this);
        container = view.findViewById(R.id.constraintEditText);
    }

    @Override
    public void setup(DefaultLayoutItem item, DefaultLayoutCellChangeListener listener) {
        this.item = (DefaultLayoutTextBox) item;
        this.listener = listener;

        txtText.clearFocus();
        txtText.setText(this.item.getInitialValue());

        float density = txtText.getResources().getDisplayMetrics().density;
        ConstraintSet set = new ConstraintSet();
        set.clone(container);
        set.connect(viewId, ConstraintSet.LEFT, container.getId(), ConstraintSet.LEFT, (int) (density * 24));
        set.connect(viewId, ConstraintSet.RIGHT, container.getId(), ConstraintSet.RIGHT, (int) (density * 24));

        switch (this.item.getTextBoxType()) {
            case TEXT:
                txtText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                set.constrainHeight(R.id.editText, (int) (density * 120));
                break;
            case NUMERIC:
                txtText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                set.constrainHeight(R.id.editText, (int) (density * 50));
                break;
        }

        set.applyTo(container);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (!txtText.hasFocus()) {
            return;
        }

        listener.onTextUpdate(charSequence.toString());
    }
}
