package com.abhishek.newchats.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.abhishek.newchats.R;
import com.abhishek.newchats.databinding.FragmentRecentBinding;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRecent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRecent extends Fragment {
    public static Context context;
    public static ArrayList<RecentItemModel> data = new ArrayList<RecentItemModel>();
    public static RecentRecyclerListAdapter adapter;

    static Toolbar toolbar;

    public FragmentRecent() {
        // Required empty public constructor
    }

    public static FragmentRecent newInstance(int position) {
        FragmentRecent fragment = new FragmentRecent();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.abhishek.newchats.databinding.FragmentRecentBinding binding = FragmentRecentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        context = root.getContext().getApplicationContext();
        RecyclerView list = root.findViewById(R.id.recent_list);
        list.setLayoutManager(new LinearLayoutManager(context));
        this.loadData();
        adapter = new RecentRecyclerListAdapter(context, data);
        list.setAdapter(adapter);
        return root;
    }

    private void loadData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("recents", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recentList", null);
        Type type = new TypeToken<ArrayList<RecentItemModel>>() {
        }.getType();
        data = gson.fromJson(json, type);
        if (data == null) {
            data = new ArrayList<>();
        }
    }

    public static void addToRecent(String num, String msg) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).number.equals(num)) {
                data.remove(data.get(i));
                adapter.notifyDataSetChanged();
            }
        }
        data.add(0, new RecentItemModel(R.drawable.no_profile_picture_15254, num, msg));
        SharedPreferences sharedPreferences = context.getSharedPreferences("recents", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("recentList", json);
        editor.apply();
        adapter.notifyDataSetChanged();
    }

    public static void selectionMode(boolean bool) {
        if (bool) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).img = R.drawable.ic_baseline_check_box_outline_blank_24;
                adapter.notifyDataSetChanged();
            }
            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_delete_outline_24);
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).img = R.drawable.no_profile_picture_15254;
                adapter.notifyDataSetChanged();
            }
            toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_edit_note_24);
        }
    }
    public static void selectionMode(int index) {
            for (int i = 0; i < data.size(); i++) {
                if(i==index){
                    data.get(i).img = R.drawable.ic_outline_check_box_24;
                }else if(data.get(i).img == R.drawable.no_profile_picture_15254){
                    data.get(i).img = R.drawable.ic_baseline_check_box_outline_blank_24;
                }
                toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_baseline_delete_outline_24);
                adapter.notifyDataSetChanged();
            }
    }

    public static boolean isSelectionModeActive() {
        if (data.size() > 0) {
            return data.get(0).img != R.drawable.no_profile_picture_15254;
        }
        return false;
    }
    public static int getDataSize(){
        return data.size();
    }

    public static void deleteFromRecent(int index) {
        data.remove(index);
        SharedPreferences sharedPreferences = context.getSharedPreferences("recents", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString("recentList", json);
        editor.apply();
        adapter.notifyDataSetChanged();
        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
    }

    public static void selectUnSelect(int index) {
        for (int i = 0; i < data.size(); i++) {
            if (i == index && data.get(i).img == R.drawable.ic_baseline_check_box_outline_blank_24) {
                data.get(i).img = R.drawable.ic_outline_check_box_24;
            } else if (i == index && data.get(i).img == R.drawable.ic_outline_check_box_24) {
                data.get(i).img = R.drawable.ic_baseline_check_box_outline_blank_24;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public static void deleteSelected() {
        boolean isRemoved = data.removeIf(element -> element.img == R.drawable.ic_outline_check_box_24);
        if(isRemoved){
            SharedPreferences sharedPreferences = context.getSharedPreferences("recents", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(data);
            editor.putString("recentList", json);
            editor.apply();
            adapter.notifyDataSetChanged();
            selectionMode(false);
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Please Select", Toast.LENGTH_SHORT).show();
        }
    }

    public static void copyPhone(int index) {
        String phone = data.get(index).number;
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("phone", phone);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
    }

    public static void openChat(int index) {
        String phone = data.get(index).number;
        String msg = data.get(index).msg;
        if (phone.charAt(0) == '+') {
            StringBuilder sb = new StringBuilder(phone);
            sb.deleteCharAt(0);
            phone = sb.toString();
        }
        try{
            Uri uri = Uri.parse("https://api.whatsapp.com/send/?phone=" + phone + "&text=" + msg + "&type=phone_number");
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.setPackage("com.whatsapp");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "Can't Open WhatsApp It may not be installed", Toast.LENGTH_SHORT).show();
        }
    }

    public static void dialPhone(int index) {
        try{
            String phone = data.get(index).number;
            Uri number = Uri.parse("tel:" + phone);
            Intent i = new Intent(Intent.ACTION_DIAL, number);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "Can't Open WhatsApp It may not be installed", Toast.LENGTH_SHORT).show();
        }
    }

}