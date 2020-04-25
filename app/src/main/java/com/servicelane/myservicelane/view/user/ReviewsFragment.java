package com.servicelane.myservicelane.view.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kennyc.view.MultiStateView;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.ReviewAdapter;
import com.servicelane.myservicelane.model.Review;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsFragment extends BaseFragment {
    private static final String ARG_USER = "user";
    private static final String ARG_REVIEWS = "reviews";

    @BindView(R.id.stateView)
    MultiStateView stateView;

    @BindView(R.id.emptyText)
    TextView emptyText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private User user;
    private List<Review> reviews = new ArrayList<>();

    public static ReviewsFragment newInstance(User user, List<Review> reviews) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_USER, user);
        bundle.putParcelableArrayList(ARG_REVIEWS, (ArrayList<Review>) reviews);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
            reviews = getArguments().getParcelableArrayList(ARG_REVIEWS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_recycler_view_only, container, false);

        ButterKnife.bind(this, view);

        ReviewAdapter adapter = new ReviewAdapter(getContext(), user, reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        emptyText.setText(R.string.empty_review);
        stateView.setViewState(reviews.isEmpty() ? MultiStateView.VIEW_STATE_EMPTY : MultiStateView.VIEW_STATE_CONTENT);

        return view;
    }
}
