package com.servicelane.myservicelane.view.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.annimon.stream.Stream;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.Review;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends BaseActivity {
    public static final String ARG_USER = "user";

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private int[] tabTitles = {R.string.identity, R.string.reviews, R.string.portfolio};
    private User user;
    private List<ServicePackage> services = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<Portfolio> portfolios = new ArrayList<>();

    public static void startActivity(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(ARG_USER, user);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_user);

        ButterKnife.bind(this);

        user = getIntent().getParcelableExtra(ARG_USER);

        title(user.getName());

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        loadUser();
    }

    private void loadUser() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", user.getIdString());

        post("/user/profile", params, response -> {
            JsonObject data = response.get("data").getAsJsonObject();
            user = new User(data.get("userInfo").getAsJsonObject());
            services = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new ServicePackage(item.getAsJsonObject())).toList();
            reviews = Stream.of(data.get("rvList").getAsJsonArray()).map(item -> new Review(item.getAsJsonObject())).toList();
            portfolios = Stream.of(data.get("prList").getAsJsonArray()).map(item -> new Portfolio(item.getAsJsonObject())).toList();

            viewPager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        });
    }

    private class PageAdapter extends FragmentStatePagerAdapter {
        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return IdentityFragment.newInstance(user, services);
                case 1:
                    return ReviewsFragment.newInstance(user, reviews);
                default:
                    return PortfolioFragment.newInstance(user, portfolios);
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return Helper.string(UserActivity.this, tabTitles[position]);
        }
    }
}
