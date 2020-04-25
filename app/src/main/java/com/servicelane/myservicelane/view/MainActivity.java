package com.servicelane.myservicelane.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.annimon.stream.Stream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.servicelane.myservicelane.Events;
import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.model.Common;
import com.servicelane.myservicelane.model.MenuCell;
import com.servicelane.myservicelane.model.Portfolio;
import com.servicelane.myservicelane.model.Review;
import com.servicelane.myservicelane.model.ServicePackage;
import com.servicelane.myservicelane.model.User;
import com.servicelane.myservicelane.view.account.AccountFragment;
import com.servicelane.myservicelane.view.account.PasswordActivity;
import com.servicelane.myservicelane.view.account.PlanActivity;
import com.servicelane.myservicelane.view.account.PortfolioActivity;
import com.servicelane.myservicelane.view.account.ProfileActivity;
import com.servicelane.myservicelane.view.account.ServicePackageActivity;
import com.servicelane.myservicelane.view.auth.SignUpActivity;
import com.servicelane.myservicelane.view.explore.ExploreFragment;
import com.servicelane.myservicelane.view.home.HomeFragment;
import com.servicelane.myservicelane.view.search.SearchFragment;
import com.servicelane.myservicelane.view.setting.AboutUsActivity;
import com.servicelane.myservicelane.view.setting.SettingFragment;
import com.servicelane.myservicelane.view.setting.SupportActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private int[] tabIcons = {R.drawable.ic_home, R.drawable.ic_search, R.drawable.ic_explore, R.drawable.ic_account, R.drawable.ic_settings};
    private int[] tabCustomerIcons = {R.drawable.ic_home, R.drawable.ic_search, R.drawable.ic_explore, R.drawable.ic_settings};
    private int[] tabTitles = {R.string.home, R.string.search, R.string.explore, R.string.account, R.string.settings};
    private int[] tabCustomerTitles = {R.string.home, R.string.search, R.string.explore, R.string.settings};

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        boolean isCustomer = Helper.StoreKey.ME.getUser() == null;

        // View pager
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), isCustomer);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        // Tab layout
        tabLayout.setupWithViewPager(viewPager);
        if (isCustomer) {
            for (int i = 0; i < tabCustomerIcons.length; i++) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(tabCustomerIcons[i]);
                Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(i)).getIcon()).setColorFilter(ContextCompat.getColor(this, i == 0 ? R.color.colorPrimary : R.color.grey), PorterDuff.Mode.SRC_IN);
            }
        } else {
            for (int i = 0; i < tabIcons.length; i++) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setIcon(tabIcons[i]);
                Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(i)).getIcon()).setColorFilter(ContextCompat.getColor(this, i == 0 ? R.color.colorPrimary : R.color.grey), PorterDuff.Mode.SRC_IN);
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                invalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Objects.requireNonNull(tab.getIcon()).setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.grey), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Event
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onMenuItemSelected(Events.OnMenuItemSelected event) {
        MenuCell item = event.menuCell;

        switch (item.getTitle()) {
            case R.string.menu_terms_of_service:
                PrivacyPolicyActivity.startActivity(this, false);
                break;

            case R.string.menu_privacy_policy:
                PrivacyPolicyActivity.startActivity(this, true);
                break;

            case R.string.menu_status_activate:
                Map<String, String> params = new HashMap<>();
                params.put("userId", Objects.requireNonNull(Helper.StoreKey.ME.getUser()).getIdString());
                params.put("status", event.checked ? "1" : "0");
                post("/user/activate", params, false, response -> Helper.StoreKey.ME.setUser(new User(response.getAsJsonObject("data"))));
                break;

            case R.string.menu_support:
                SupportActivity.startActivity(this);
                break;

            case R.string.menu_about_us:
                AboutUsActivity.startActivity(this);
                break;

            case R.string.menu_password:
                PasswordActivity.startActivity(this);
                break;

            case R.string.menu_service_packages:
                ServicePackageActivity.startActivity(this);
                break;

            case R.string.menu_plan:
                PlanActivity.startActivity(this);
                break;

            case R.string.menu_profile:
                ProfileActivity.startActivity(this);
                break;

            case R.string.menu_portfolio:
                PortfolioActivity.startActivity(this);
                break;

            case R.string.sign_out:
                Helper.StoreKey.ME.clear();
                SignUpActivity.startActivity(this);
                break;
        }
    }

    @Subscribe
    public void onHomeRequested(Events.OnHomeRequest event) {
        Map<String, String> params = new HashMap<>();
        params.put("catId", String.valueOf(event.categoryId));

        post("/user/home", params, response -> {
            JsonObject data = response.get("data").getAsJsonObject();
            // Provider count
            int cnt = data.get("pvCount").getAsInt();

            // Providers
            List<User> providers = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new User(item.getAsJsonObject())).toList();

            // Category list
            List<Common> categories = Stream.of(data.get("catList").getAsJsonArray()).map(item -> {
                JsonObject json = item.getAsJsonObject();
                return new Common(json.get("ID").getAsInt(), json.get("Category").getAsString());
            }).toList();
            categories.add(0, new Common(0, "Select category"));

            // Top 5 categories
            List<Common> topCategories = Stream.of(data.get("topCatList").getAsJsonArray()).map(item -> {
                JsonObject json = item.getAsJsonObject();
                return new Common(json.get("ID").getAsInt(), json.get("Category").getAsString());
            }).toList();

            // Return result
            EventBus.getDefault().post(new Events.OnHomeResponse(event.categoryId, cnt, providers, categories, topCategories));
        });
    }

    @Subscribe
    public void onSearchSetupRequested(Events.OnSearchSetupRequest event) {
        get("/user/search", response -> {
            JsonObject data = response.get("data").getAsJsonObject();

            // Category list
            List<Common> categories = Stream.of(data.get("catList").getAsJsonArray()).map(item -> {
                JsonObject json = item.getAsJsonObject();
                return new Common(json.get("ID").getAsInt(), json.get("Category").getAsString());
            }).toList();
            categories.add(0, new Common(0, "Select category"));

            // Parish list
            List<String> parishes = Stream.of(data.get("parishList").getAsJsonArray()).map(JsonElement::getAsString).toList();
            parishes.add(0, "Select parish");

            // Providers
            List<User> providers = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new User(item.getAsJsonObject())).toList();

            EventBus.getDefault().post(new Events.OnSearchSetupResponse(categories, parishes, providers));
        });
    }

    @Subscribe
    public void onSearchRequested(Events.OnSearchRequest event) {
        Map<String, String> params = new HashMap<>();
        params.put("catId", String.valueOf(event.categoryId));
        params.put("parish", event.parish);
        params.put("rate", String.valueOf(event.rate));
        params.put("reviewCnt", String.valueOf(event.reviewCnt));
        params.put("from", event.from);
        params.put("to", event.to);

        post("/user/search", params, response -> {
            List<User> providers = new ArrayList<>();
            if (response.has("data")) {
                providers = Stream.of(response.get("data").getAsJsonArray()).map(item -> new User(item.getAsJsonObject())).toList();
            }
            EventBus.getDefault().post(new Events.OnSearchResponse(providers));
        });
    }

    @Subscribe
    public void onExploreRequested(Events.OnExploreRequest event) {
        Map<String, String> params = new HashMap<>();
        params.put("parish", event.parish);

        post("/user/explore", params, response -> {
            JsonObject data = response.get("data").getAsJsonObject();
            List<String> parishes = Stream.of(data.get("parishList").getAsJsonArray()).map(JsonElement::getAsString).toList();
            List<User> providers = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new User(item.getAsJsonObject())).toList();
            EventBus.getDefault().post(new Events.OnExploreResponse(event.parish, parishes, providers));
        });
    }

    @Subscribe
    public void onProfileRequested(Events.OnProfileRequest event) {
        Map<String, String> params = new HashMap<>();
        params.put("userId", String.valueOf(event.userId));

        post("/user/profile", params, response -> {
            JsonObject data = response.get("data").getAsJsonObject();
            User user = new User(data.get("userInfo").getAsJsonObject());
            List<Portfolio> portfolios = Stream.of(data.get("prList").getAsJsonArray()).map(item -> new Portfolio(item.getAsJsonObject())).toList();
            List<Review> reviews = Stream.of(data.get("rvList").getAsJsonArray()).map(item -> new Review(item.getAsJsonObject())).toList();
            List<ServicePackage> services = Stream.of(data.get("pvList").getAsJsonArray()).map(item -> new ServicePackage(item.getAsJsonObject())).toList();
            EventBus.getDefault().post(new Events.OnProfileResponse(user, portfolios, reviews, services));
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int curTabIndex = tabLayout.getSelectedTabPosition();

        if (curTabIndex == 0) {
            title(R.string.app_name);
        } else {
            title(Objects.requireNonNull(tabLayout.getTabAt(curTabIndex)).getText());
        }

        // search filter
        if (curTabIndex == 1) {
            getMenuInflater().inflate(R.menu.search, menu);
            return true;
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionFilter) {
            EventBus.getDefault().post(new Events.OnSearchFilter());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PageAdapter extends FragmentStatePagerAdapter {
        private boolean isCustomer;

        PageAdapter(FragmentManager fm, boolean isCustomer) {
            super(fm);
            this.isCustomer = isCustomer;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return SearchFragment.newInstance();
                case 2:
                    return ExploreFragment.newInstance();
                case 3:
                    return isCustomer ? SettingFragment.newInstance() : AccountFragment.newInstance();
                default:
                    return SettingFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return isCustomer ? tabCustomerTitles.length : tabTitles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return Helper.string(MainActivity.this, isCustomer ? tabCustomerTitles[position] : tabTitles[position]);
        }
    }
}
