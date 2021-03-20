package com.cookandroid.curtain;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ControlAdapter extends FragmentStateAdapter {
    public int mCount;
    Fragment[] frags = new Fragment[]{
            new Fragment_Machine(),
            new Fragment_Main(),
            new Fragment_Curtain(),
            new Fragment_Auto()

    };

    public ControlAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return frags[position];
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
