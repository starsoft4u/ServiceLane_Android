package com.servicelane.myservicelane.view.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.GridDecoration;
import com.servicelane.myservicelane.adapter.ProviderMiniAdapter;
import com.servicelane.myservicelane.model.Common;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseFragment;
import com.servicelane.myservicelane.view.account.ProfileActivity;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class HomeFragment extends BaseFragment {
    @BindView(R.id.profilePanel)
    View profilePanel;

    @BindView(R.id.imgAvatar)
    CircleImageView imgAvatar;

    @BindView(R.id.textName)
    TextView textName;

    @BindView(R.id.ratingBar)
    MaterialRatingBar ratingBar;

    @BindView(R.id.textProviders)
    TextView textProviders;

    @BindView(R.id.topCategoryPanel)
    View topCategoryPanel;

    @BindView(R.id.btnCat0)
    Button btnCat0;

    @BindView(R.id.btnCat1)
    Button btnCat1;

    @BindView(R.id.btnCat2)
    Button btnCat2;

    @BindView(R.id.btnCat3)
    Button btnCat3;

    @BindView(R.id.btnCat4)
    Button btnCat4;

    @BindView(R.id.pickerCategory)
    AppCompatSpinner pickerCategory;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<Common> categories = new ArrayList<>();
    private ArrayAdapter<Common> categoryAdapter;

    private List<User> providers = new ArrayList<>();
    private ProviderMiniAdapter providerAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @OnClick(R.id.imgAvatar)
    void onAvatarTapped() {
        ProfileActivity.startActivity(Objects.requireNonNull(getContext()));
    }

    @OnClick(R.id.textName)
    void onUsernameClicked() {
        ProfileActivity.startActivity(Objects.requireNonNull(getContext()));
    }

    @OnClick(R.id.btnViewAll)
    void onViewAllAction() {
        EventBus.getDefault().post(new Events.OnHomeRequest(0));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadHome(Events.OnHomeResponse event) {
        textProviders.setText(Helper.string(Objects.requireNonNull(getContext()), R.string.pick_one_from, new DecimalFormat("#,###,###").format(event.providerCnt)));
        textProviders.setVisibility(event.providerCnt < 200 ? View.GONE : View.VISIBLE);

        // Initial
        if (event.categoryId == 0) {
            // Top categories
            if (event.topCategories.size() == 5) {
                Button[] buttons = {btnCat0, btnCat1, btnCat2, btnCat3, btnCat4};
                for (int i = 0; i < event.topCategories.size(); i++) {
                    Common item = event.topCategories.get(i);
                    buttons[i].setText(item.getName());
                    buttons[i].setOnClickListener(v -> onTopCategoryClicked(item.getId()));
                }
                topCategoryPanel.setVisibility(View.VISIBLE);
            } else {
                topCategoryPanel.setVisibility(View.GONE);
            }

            // Category picker
            categories.clear();
            categories.addAll(event.categories);
            categoryAdapter.notifyDataSetChanged();
            pickerCategory.setSelection(0);
        }

        providers.clear();
        providers.addAll(event.providers);
        providerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        topCategoryPanel.setVisibility(View.GONE);

        // User
        User me = Helper.StoreKey.ME.getUser();
        if (me != null) {
            Picasso.get().load(me.getPhoto()).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(imgAvatar);
            textName.setText(me.getName());
            ratingBar.setRating(me.getRate());
            profilePanel.setVisibility(View.VISIBLE);
        } else {
            profilePanel.setVisibility(View.GONE);
        }

        // Categories
        categories.add(new Common(0, "Select category"));
        categoryAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), R.layout.picker_text, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickerCategory.setAdapter(categoryAdapter);
        pickerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (categories.get(i).getId() > 0) {
                    EventBus.getDefault().post(new Events.OnHomeRequest(categories.get(i).getId()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Providers
        providerAdapter = new ProviderMiniAdapter(getContext(), providers);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new GridDecoration(2, Helper.pixel(Objects.requireNonNull(getContext()), 16), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(providerAdapter);

        EventBus.getDefault().post(new Events.OnHomeRequest(0));

        return view;
    }

    private void onTopCategoryClicked(int id) {
        EventBus.getDefault().post(new Events.OnHomeRequest(id));
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
