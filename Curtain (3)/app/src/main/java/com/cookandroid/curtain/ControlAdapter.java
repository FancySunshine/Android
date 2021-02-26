package com.cookandroid.curtain;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ControlAdapter extends FragmentStateAdapter {
    public int mCount;
    public ControlAdapter(@NonNull FragmentActivity fragmentActivity, int count) {
        super(fragmentActivity);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new Fragment_Main();
        }
        else if(position == 1){
            return new Fragment_Curtain();
        }
        else {
            return new Fragment_Auto();
        }
    }



    @Override
    public int getItemCount() {
        return 3;
    }
}
