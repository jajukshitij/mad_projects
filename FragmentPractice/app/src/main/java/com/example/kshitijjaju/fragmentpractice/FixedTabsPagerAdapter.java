package com.example.kshitijjaju.fragmentpractice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    public FixedTabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return new FirstFragment();
            case 1:
                return new FirstFragment();
            case 2:
                return new FirstFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return mContext.getString(R.string.hello_blank_fragment);
            case 1:
                return "Title 1";
            case 2:
                return "Title 2";
            default:
                return null;
        }
    }
}
