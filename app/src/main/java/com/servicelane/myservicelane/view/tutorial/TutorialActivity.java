package com.servicelane.myservicelane.view.tutorial;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.servicelane.myservicelane.Helper;
import com.servicelane.myservicelane.R;
import com.servicelane.myservicelane.view.BaseActivity;
import com.servicelane.myservicelane.view.auth.SignUpActivity;
import com.viewpagerindicator.LinePageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.pageIndicator)
    LinePageIndicator pageIndicator;

    private int[] images;
    private String[] titles, descriptions;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.btnGetStarted)
    void onGetStatedAction() {
        SignUpActivity.startActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ButterKnife.bind(this);

        // load resources
        images = new int[]{R.drawable.tutorial1, R.drawable.tutorial2, R.drawable.tutorial3};
        titles = Helper.stringArray(this, R.array.get_started_titles);
        descriptions = Helper.stringArray(this, R.array.get_started_descriptions);

        // viewpager and page indicator
        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pageIndicator.setViewPager(viewPager);
    }

    private class PageAdapter extends FragmentStatePagerAdapter {
        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TutorialFragment.newInstance(images[position], titles[position], descriptions[position]);
        }

        @Override
        public int getCount() {
            return images.length;
        }
    }
}
