package com.confirmit.testsdkapp.layouts.defaultlayoutjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.confirmit.mobilesdk.ui.SurveyFrame;
import com.confirmit.mobilesdk.ui.SurveyFrameConfig;
import com.confirmit.mobilesdk.ui.SurveyFrameLifecycleListener;
import com.confirmit.mobilesdk.ui.SurveyPage;
import com.confirmit.mobilesdk.ui.questions.Question;
import com.confirmit.testsdkapp.AppConfigs;
import com.confirmit.testsdkapp.R;
import com.confirmit.testsdkapp.fragments.BaseDialogFragment;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutPageSection;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutQuestionSection;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutSection;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.DefaultLayoutSectionListener;
import com.confirmit.testsdkapp.layouts.defaultlayoutjava.items.IndexPath;
import com.confirmit.testsdkapp.viewmodels.models.SurveyModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class DefaultSurveyFragment extends BaseDialogFragment implements View.OnClickListener, SurveyFrameLifecycleListener, DefaultLayoutSectionListener {
    private SurveyFrame surveyFrame;
    private SurveyModel surveyModel;

    private SurveyLayoutOnCloseListener onCloseListener;

    private ConstraintLayout containerSystem;
    private ImageButton btnClose;
    private Button btnFinish;
    private Button btnNext;
    private Button btnBack;
    private TextView txtSystem;
    private TextView titleSystem;
    private ImageView imgSystem;
    private TextView txtToolbar;
    private ConstraintLayout toolbar;
    private RecyclerView recyclerView;

    private DefaultSurveyAdapter adapter;

    private static final String BTN_NEXT_TAG = "btnNext";
    private static final String BTN_FINISH_TAG = "btnFinish";
    private static final String BTN_BACK_TAG = "btnBack";
    private static final String BTN_CLOSE_TAG = "btnClose";

    private List<DefaultLayoutSection> sections = new ArrayList<>();

    public void initialize(SurveyModel surveyModel) {
        this.surveyModel = surveyModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveyFrame = new SurveyFrame();
        setStyle(STYLE_NO_FRAME, R.style.AlertDialogTheme);
    }

    @Override
    public void onActivityCreated(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
    }

    @Override
    public void initContentView(@NotNull View rootView, @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        containerSystem = rootView.findViewById(R.id.containerSystem);
        txtToolbar = rootView.findViewById(R.id.toolbarTitle);
        toolbar = rootView.findViewById(R.id.toolbar);
        btnClose = rootView.findViewById(R.id.btnClose);
        btnFinish = rootView.findViewById(R.id.btnFinish);
        btnNext = rootView.findViewById(R.id.btnNext);
        btnBack = rootView.findViewById(R.id.btnBack);
        txtSystem = rootView.findViewById(R.id.txtSystem);
        titleSystem = rootView.findViewById(R.id.titleSystem);
        imgSystem = rootView.findViewById(R.id.imgSystem);
        recyclerView = rootView.findViewById(R.id.defaultSurveyRecyclerView);

        adapter = new DefaultSurveyAdapter(sections);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        containerSystem.setVisibility(ConstraintLayout.GONE);

        btnClose.setOnClickListener(this);
        btnClose.setTag(BTN_CLOSE_TAG);
        btnFinish.setOnClickListener(this);
        btnFinish.setTag(BTN_FINISH_TAG);
        btnBack.setOnClickListener(this);
        btnBack.setTag(BTN_BACK_TAG);
        btnNext.setOnClickListener(this);
        btnNext.setTag(BTN_NEXT_TAG);

        txtToolbar.setText(surveyModel.getSurvey().getName());

        try {
            SurveyFrameConfig surveyFrameConfig = new SurveyFrameConfig(surveyModel.getSurvey().getServerId(), surveyModel.getSurvey().getSurveyId());
            surveyFrameConfig.setLanguageId(surveyModel.getSelectedLanguage().getId());
            surveyFrameConfig.setRespondentValues(surveyModel.getRespondentValues());

            surveyFrame.load(surveyFrameConfig);
            surveyFrame.setSurveyFrameLifeCycleListener(this);
            surveyFrame.start();
        } catch (Exception e) {
            errorCloseSurvey(e);
        }
    }

    private void closeSurvey() {
        hideKeyboard();
        dismiss();
    }

    private void errorCloseSurvey(final Exception e) {
        postUi(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                dismiss();
                if (onCloseListener != null) {
                    onCloseListener.onCloseError(e.toString());
                }
                return Unit.INSTANCE;
            }
        });
    }

    public void setOnCloseListener(SurveyLayoutOnCloseListener listener) {
        this.onCloseListener = listener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_default_layout;
    }

    // generated due to kotlin property inheritance
    @Override
    public void setLayoutId(int i) {
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals(BTN_CLOSE_TAG)) {
            surveyFrame.quit(AppConfigs.INSTANCE.getUploadIncomplete());
        } else if (view.getTag().equals(BTN_FINISH_TAG)) {
            closeSurvey();
        } else if (view.getTag().equals(BTN_NEXT_TAG)) {
            hideKeyboard();
            surveyFrame.next();
        } else if (view.getTag().equals(BTN_BACK_TAG)) {
            hideKeyboard();
            surveyFrame.back();
        }
    }

    @Override
    public void onSurveyPageReady(@NotNull SurveyPage page) {
        sections.clear();
        int startIndex = 0;
        if (!page.getTitle().isEmpty()) {
            sections.add(new DefaultLayoutPageSection(page));
            startIndex = 1;
        }
        List<Question> questions = page.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            DefaultLayoutQuestionSection section = new DefaultLayoutQuestionSection(question);
            section.load(this, i + startIndex);
            sections.add(section);

        }

        adapter.notifyDataSetChanged();
        updateNavigationButton();
    }

    @Override
    public void onSurveyErrored(@NotNull SurveyPage page, @NotNull Map<String, String> values, @NotNull Exception exception) {
        sections.clear();
        adapter.notifyDataSetChanged();
        updateNavigationSystemButton();

        containerSystem.setVisibility(ConstraintLayout.VISIBLE);
        txtSystem.setText(Objects.requireNonNull(page.getText()).get());
        titleSystem.setText(page.getTitle().get());
        imgSystem.setColorFilter(ContextCompat.getColor(getContext(), R.color.defaultSurveyError));
        imgSystem.setImageResource(R.drawable.ic_sentiment_very_dissatisfied);
    }

    @Override
    public void onSurveyFinished(@NotNull SurveyPage page, @NotNull Map<String, String> values) {
        sections.clear();
        adapter.notifyDataSetChanged();
        updateNavigationSystemButton();

        containerSystem.setVisibility(ConstraintLayout.VISIBLE);
        txtSystem.setText(Objects.requireNonNull(page.getText()).get());
        titleSystem.setText(page.getTitle().get());
        imgSystem.setColorFilter(ContextCompat.getColor(getContext(), R.color.defaultSurveyButton));
        imgSystem.setImageResource(R.drawable.ic_thumb_up_alt_black);
    }

    @Override
    public void onSurveyQuit(@NotNull Map<String, String> values) {
        closeSurvey();
    }

    @Override
    public void onReloadPage(final List<IndexPath> add, final List<IndexPath> refresh, final List<IndexPath> remove) {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                adapter.refresh();
                for (IndexPath index : add) {
                    adapter.notifyItemInserted(adapter.getItemIndex(index.getSection(), index.getRow()));
                }
                for (IndexPath index : remove) {
                    adapter.notifyItemRemoved(adapter.getItemIndex(index.getSection(), index.getRow()));
                }
                for (IndexPath index : refresh) {
                    adapter.notifyItemChanged(adapter.getItemIndex(index.getSection(), index.getRow()));
                }
            }
        });
    }

    private void updateNavigationSystemButton() {
        btnBack.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.VISIBLE);
    }

    private void updateNavigationButton() {
        btnBack.setVisibility(surveyFrame.getPage().getShowBackward() ? View.VISIBLE : View.INVISIBLE);
        btnNext.setVisibility(surveyFrame.getPage().getShowForward() ? View.VISIBLE : View.INVISIBLE);
        btnFinish.setVisibility(surveyFrame.getPage().getShowOk() ? View.VISIBLE : View.INVISIBLE);
    }

}
