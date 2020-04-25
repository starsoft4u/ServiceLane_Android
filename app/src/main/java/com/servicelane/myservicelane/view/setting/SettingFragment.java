package com.servicelane.myservicelane.view.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.adapter.MenuAdapter;
import com.servicelane.myservicelane.model.MenuCell;
import com.servicelane.myservicelane.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        List<MenuCell> menus = new ArrayList<>();
        if (Helper.StoreKey.ME.getUser() != null) {
            menus.add(new MenuCell(R.string.menu_status, MenuCell.Type.TITLE_ONLY));
            menus.add(new MenuCell(R.string.menu_status_activate, MenuCell.Type.SWITCH, Helper.StoreKey.ME.getUser().isActivate()));
        }
        menus.add(new MenuCell(R.string.menu_about_us, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_support, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_terms_of_service, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_privacy_policy, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.sign_out, MenuCell.Type.BUTTON));

        MenuAdapter adapter = new MenuAdapter(menus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
