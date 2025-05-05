package com.malsoryz.tebakyo;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final FragmentManager fragmentManager;
    private final int containerId;
    private final Context context;
    private final Map<String, Class<? extends Fragment>> routes = new HashMap<>();

    public Router(FragmentManager fragmentManager, int containerId, Context context) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context.getApplicationContext();
    }

    public void addRoute(String name, Class<? extends Fragment> fragmentClass) {
        routes.put(name, fragmentClass);
    }

    public void navigateTo(String routeName, boolean addToBackStack, boolean animation) {
        Class<? extends Fragment> fragmentClass = routes.get(routeName);
        try {
            if (fragmentClass != null) {
                Fragment fragment = fragmentClass.newInstance();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (animation) transaction.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                );
                transaction.replace(containerId, fragment, routeName);
                if (addToBackStack) transaction.addToBackStack(routeName);
                transaction.commit();
            } else {
                Toast.makeText(context, String.format("Route %s is not exist", routeName), Toast.LENGTH_SHORT).show();
                throw new IllegalArgumentException("Route not registered: " + routeName);
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Fragment class must have a public no-arg constructor");
        }
    }

    public void navigateBack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }
}
