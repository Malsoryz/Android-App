package com.malsoryz.etrplayers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class Utils {
    public static void openLink(Context context, String link) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
    }
    public static void loadFragment(FragmentActivity activity, Fragment fragment, int fragmentPoint) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentPoint, fragment)
                .commit();
    }
}
