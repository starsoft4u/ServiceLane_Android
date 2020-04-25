package com.servicelane.myservicelane.view.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.kennyc.view.MultiStateView;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.ProviderAdapter;
import com.servicelane.myservicelane.model.Common;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends BaseFragment implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.pickerCategory)
    AppCompatSpinner pickerCategory;

    @BindView(R.id.pickerParish)
    AppCompatSpinner pickerParish;

    @BindView(R.id.pickerRatingContainer)
    View pickerRatingContainer;

    @BindView(R.id.pickerRating)
    AppCompatSpinner pickerRating;

    @BindView(R.id.pickerReviewContainer)
    View pickerReviewContainer;

    @BindView(R.id.pickerReview)
    AppCompatSpinner pickerReview;

    @BindView(R.id.textPriceRange)
    TextView textPriceRange;

    @BindView(R.id.priceRange)
    View priceRange;

    @BindView(R.id.textPriceFrom)
    EditText textPriceFrom;

    @BindView(R.id.textPriceTo)
    EditText textPriceTo;

    @BindView(R.id.buttonContainer)
    View buttonContainer;

    @BindView(R.id.stateView)
    MultiStateView stateView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Common> categories = new ArrayList<>();
    private ArrayAdapter categoryAdapter;

    private List<String> parishes = new ArrayList<>();
    private ArrayAdapter parishAdapter;

    private List<Common> reviews = new ArrayList<>();

    private List<User> providers = new ArrayList<>();

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetupFilter(Events.OnSearchSetupResponse event) {
        categories.clear();
        categories.addAll(event.categories);
        categoryAdapter.notifyDataSetChanged();
        pickerCategory.setSelection(0);

        parishes.clear();
        parishes.addAll(event.parishes);
        parishAdapter.notifyDataSetChanged();

        pickerRating.setSelection(0);
        pickerReview.setSelection(0);
        textPriceFrom.setText("");
        textPriceTo.setText("");

        providers.clear();
        providers.addAll(event.providers);
        recyclerView.setAdapter(new ProviderAdapter(getContext(), providers));
        stateView.setViewState(providers.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);

        showSearchPanel(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchResult(Events.OnSearchResponse event) {
        providers.clear();
        providers.addAll(event.providers);
        recyclerView.setAdapter(new ProviderAdapter(getContext(), providers));
        stateView.setViewState(providers.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);

        showSearchPanel(false);
    }

    @Subscribe
    public void onFilterAction(Events.OnSearchFilter event) {
        toggleSearchPanel();
    }

    @OnClick(R.id.btnFilter)
    void onUpdateSearch() {
        search();
    }

    @OnClick(R.id.btnReset)
    void onReset() {
        EventBus.getDefault().post(new Events.OnSearchSetupRequest());
    }

    @OnClick(R.id.btnFromUp)
    void onFromUpAction() {
        try {
            int val = Integer.parseInt(textPriceFrom.getText().toString());
            val = Math.max(val + 1, 0);
            textPriceFrom.setText(String.valueOf(val));
        } catch (Exception e) {
            textPriceFrom.setText("0");
        }
    }

    @OnClick(R.id.btnFromDown)
    void onFromDownAction() {
        try {
            int val = Integer.parseInt(textPriceFrom.getText().toString());
            val = Math.max(val - 1, 0);
            textPriceFrom.setText(String.valueOf(val));
        } catch (Exception e) {
            textPriceFrom.setText("0");
        }
    }

    @OnClick(R.id.btnToUp)
    void onToUpAction() {
        try {
            int val = Integer.parseInt(textPriceTo.getText().toString());
            val = Math.max(val + 1, 0);
            textPriceTo.setText(String.valueOf(val));
        } catch (Exception e) {
            textPriceTo.setText("0");
        }
    }

    @OnClick(R.id.btnToDown)
    void onToDownAction() {
        try {
            int val = Integer.parseInt(textPriceTo.getText().toString());
            val = Math.max(val - 1, 0);
            textPriceTo.setText(String.valueOf(val));
        } catch (Exception e) {
            textPriceTo.setText("0");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        showSearchPanel(false);

        // picker category
        categories.add(new Common(0, "Select category"));
        categoryAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.picker_text, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickerCategory.setAdapter(categoryAdapter);
        pickerCategory.setOnItemSelectedListener(this);

        // picker parish
        parishes.add("Select parish");
        parishAdapter = new ArrayAdapter<>(getContext(), R.layout.picker_text, parishes);
        parishAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickerParish.setAdapter(parishAdapter);
        pickerParish.setOnItemSelectedListener(this);

        // picker rating
        ArrayAdapter rateAdapter = new ArrayAdapter<>(getContext(), R.layout.picker_text, new String[]{"Star rating", "1 & above", "2 & above", "3 & above", "4 & above", "5 & above"});
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickerRating.setAdapter(rateAdapter);
        pickerRating.setOnItemSelectedListener(this);

        // picker review
        reviews.add(new Common(0, "Number of reviews"));
        reviews.add(new Common(1, "0 - 10"));
        reviews.add(new Common(2, "10 - 25"));
        reviews.add(new Common(3, "25 - 50"));
        reviews.add(new Common(4, "50 - 100"));
        reviews.add(new Common(5, "100 - 200"));
        reviews.add(new Common(6, "200 - 500"));
        reviews.add(new Common(7, "500+"));
        ArrayAdapter reviewAdapter = new ArrayAdapter<>(getContext(), R.layout.picker_text, reviews);
        reviewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickerReview.setAdapter(reviewAdapter);
        pickerReview.setOnItemSelectedListener(this);

        // Providers
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ProviderAdapter(getContext(), providers));

        emptyText.setText(R.string.empty_search);

        return view;
    }

    @Override
    protected void viewDidLoad() {
        EventBus.getDefault().post(new Events.OnSearchSetupRequest());
    }

    private void toggleSearchPanel() {
        boolean visible = buttonContainer.getVisibility() == View.GONE;
        showSearchPanel(visible);
    }

    private void showSearchPanel(boolean show) {
        int visible = show ? View.VISIBLE : View.GONE;
        Stream.of(pickerRatingContainer, pickerReviewContainer, textPriceRange, priceRange, buttonContainer).forEach(item -> item.setVisibility(visible));
    }

    private void search() {
        int categoryId = categories.get(pickerCategory.getSelectedItemPosition()).getId();
        String parish = pickerParish.getSelectedItemPosition() == 0 ? "" : parishes.get(pickerParish.getSelectedItemPosition());
        int rate = pickerRating.getSelectedItemPosition();
        int reviewCnt = reviews.get(pickerReview.getSelectedItemPosition()).getId();
        String from = textPriceFrom.getText().toString();
        String to = textPriceTo.getText().toString();

        EventBus.getDefault().post(new Events.OnSearchRequest(categoryId, parish, rate, reviewCnt, from, to));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            search();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
