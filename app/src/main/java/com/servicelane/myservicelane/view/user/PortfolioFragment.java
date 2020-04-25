package com.servicelane.myservicelane.view.user;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kennyc.view.MultiStateView;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.GridDecoration;
import com.servicelane.myservicelane.adapter.PortfolioAdapter;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseFragment;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PortfolioFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    private static final String ARG_PORTFOLIOS = "portfolios";

    @BindView(R.id.stateView)
    MultiStateView stateView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private User user;
    private List<Portfolio> portfolios = new ArrayList<>();

    public static PortfolioFragment newInstance(User user, List<Portfolio> portfolios) {
        PortfolioFragment fragment = new PortfolioFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_USER, user);
        bundle.putParcelableArrayList(ARG_PORTFOLIOS, (ArrayList<Portfolio>) portfolios);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Subscribe
    public void onPhotoTapped(Events.OnPortfolioPhotoTapped event) {
        int position = portfolios.indexOf(event.portfolio);
        new ImageViewer.Builder<>(getContext(), portfolios)
                .setFormatter(Portfolio::getImage)
                .setStartPosition(position)
                .show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
            portfolios = getArguments().getParcelableArrayList(ARG_PORTFOLIOS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_recycler_view_only, container, false);

        ButterKnife.bind(this, view);

        PortfolioAdapter adapter = new PortfolioAdapter(getContext(), user, portfolios, true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == 0 ? 2 : 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridDecoration(2, Helper.pixel(Objects.requireNonNull(getContext()), 16), true, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        emptyText.setText(R.string.empty_portfolio);
        stateView.setViewState(portfolios.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);

        return view;
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
