package com.servicelane.myservicelane.view.explore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kennyc.view.MultiStateView;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.GridDecoration;
import com.servicelane.myservicelane.adapter.ProviderMiniAdapter;
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

public class ExploreFragment extends BaseFragment {
    @BindView(R.id.parishView)
    GridLayout parishView;

    @BindView(R.id.textParish)
    TextView textParish;

    @BindView(R.id.stateView)
    MultiStateView stateView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<User> providers = new ArrayList<>();

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @OnClick(R.id.btnViewAll)
    public void onReset() {
        EventBus.getDefault().post(new Events.OnExploreRequest(""));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExploreResponsed(Events.OnExploreResponse event) {
        textParish.setText(event.parish);
        ((View) textParish.getParent()).setVisibility(TextUtils.isEmpty(event.parish) ? View.GONE : View.VISIBLE);

        providers.clear();
        providers.addAll(event.providers);
        recyclerView.setAdapter(new ProviderMiniAdapter(getContext(), providers));
        stateView.setViewState(providers.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        ButterKnife.bind(this, view);

        // Parishes
        for (int i = 0; i < parishView.getChildCount(); i++) {
            parishView.getChildAt(i).setOnClickListener(this::onParishAction);
        }

        // Provider
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridDecoration(2, Helper.pixel(Objects.requireNonNull(getContext()), 16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new ProviderMiniAdapter(getContext(), providers));

        emptyText.setText(R.string.empty_search);

        return view;
    }

    @Override
    protected void viewDidLoad() {
        EventBus.getDefault().post(new Events.OnExploreRequest(""));
    }

    private void onParishAction(View view) {
        if (view instanceof Button) {
            Button button = (Button) view;
            String parish = button.getText().toString();
            EventBus.getDefault().post(new Events.OnExploreRequest(parish));
        }
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
}
