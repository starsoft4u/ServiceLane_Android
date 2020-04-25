package com.servicelane.myservicelane.view.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.BaseFragment;
import com.servicelane.myservicelane.adapter.MenuAdapter;
import com.servicelane.myservicelane.model.MenuCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ButterKnife.bind(this, view);

        List<MenuCell> menus = new ArrayList<>();
        menus.add(new MenuCell(R.string.menu_profile, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_service_packages, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_portfolio, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_plan, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.menu_password, MenuCell.Type.DEFAULT));
        menus.add(new MenuCell(R.string.sign_out, MenuCell.Type.BUTTON));

        MenuAdapter adapter = new MenuAdapter(menus);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
