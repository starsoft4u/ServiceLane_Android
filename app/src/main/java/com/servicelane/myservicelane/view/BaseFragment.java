package com.servicelane.myservicelane.view;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    protected boolean hasInitiated = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            viewWillAppear();
            if (!hasInitiated) {
                viewDidLoad();
                hasInitiated = true;
            }
        }
    }

    protected void viewDidLoad() {
    }

    protected void viewWillAppear() {
    }
}
