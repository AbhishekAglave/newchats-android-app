package com.abhishek.newchats;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.abhishek.newchats.ui.main.FragmentRecent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.newchats.ui.main.SectionsPagerAdapter;
import com.abhishek.newchats.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("NewChats");
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    if (FragmentRecent.isSelectionModeActive()) {
                        FragmentRecent.selectionMode(false);
                    }
                    toolbar.getMenu().clear();
                } else if (tab.getPosition() == 1) {
                    View view = getCurrentFocus();
                    if (view != null) {
                        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(view.getWindowToken(),0);
                    }
                    TextView noRecentText = findViewById(R.id.no_recents);
                    if (FragmentRecent.getDataSize() < 1) {
                        noRecentText.setText("No Recents");
                    } else {
                        noRecentText.setText("");
                    }
                    toolbar.inflateMenu(R.menu.main_menu);
                    toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(@NonNull MenuItem item) {
                            if (FragmentRecent.isSelectionModeActive()) {
                                FragmentRecent.deleteSelected();
                                if (FragmentRecent.getDataSize() < 1) {
                                    noRecentText.setText("No Recents");
                                } else {
                                    noRecentText.setText("");
                                }
                            } else {
                                if (FragmentRecent.getDataSize() > 0) {
                                    FragmentRecent.selectionMode(true);
                                }
                            }
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (FragmentRecent.isSelectionModeActive()) {
            FragmentRecent.selectionMode(false);
        } else {
            super.onBackPressed();
        }
    }
}
