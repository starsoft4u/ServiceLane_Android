package com.servicelane.myservicelane.view.tutorial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialFragment extends BaseFragment {
    private static final String ARG_IMAGE = "image";
    private static final String ARG_TITLE = "title";
    private static final String ARG_BODY = "body";

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.textTitle)
    TextView textTitle;

    @BindView(R.id.textDescription)
    TextView textDescription;

    private int image;
    private String title, body;

    public static TutorialFragment newInstance(int image, String title, String body) {
        TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_BODY, body);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(ARG_IMAGE);
            title = getArguments().getString(ARG_TITLE);
            body = getArguments().getString(ARG_BODY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        ButterKnife.bind(this, view);

        imageView.setImageResource(image);
        textTitle.setText(title);
        textDescription.setText(body);

        return view;
    }
}
