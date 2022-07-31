package com.shruti.whatsapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shruti.whatsapp.Fragments.RequestsFragment;
import com.shruti.whatsapp.Fragments.ChatsFragment;
import com.shruti.whatsapp.Fragments.GroupsFragment;

// This class is use for Tab Layout!!
public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public FragmentsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Set position of each Fragments
        switch (position){
            case 0: return new ChatsFragment();
            case 1: return new GroupsFragment();
          //  case 2: return new RequestsFragment();
            default: return new ChatsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2; // Because we r using 3 fragments
    }

    // Set TITLE for our  TabLayout
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;

        if(position==0){
            title = "CHATS";
        }
        if(position==1){
            title = "GROUPS";
        }
//        if(position==2){
//            title = "REQUESTS";
//        }

        return title;
    }
}
