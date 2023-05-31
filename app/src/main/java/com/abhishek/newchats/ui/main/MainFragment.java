package com.abhishek.newchats.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abhishek.newchats.R;
import com.abhishek.newchats.databinding.FragmentMainBinding;
import com.hbb20.CountryCodePicker;

public class MainFragment extends Fragment {
    FragmentRecent fragmentRecent = new FragmentRecent();
    Context context;
    private FragmentMainBinding binding;

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext().getApplicationContext();
        EditText phone = root.findViewById(R.id.editTextPhone);
        EditText msg = root.findViewById(R.id.editTextMsg);
        Button btn = root.findViewById(R.id.button);
        CountryCodePicker ccp = root.findViewById(R.id.ccp);
        PackageManager pm=context.getPackageManager();
        btn.setOnClickListener(view -> {
            if (!phone.getText().toString().equals("")) {
                try {
                    Uri uri = Uri.parse("https://api.whatsapp.com/send/?phone=" + ccp.getSelectedCountryCode() + phone.getText().toString() + "&text=" + msg.getText().toString() + "&type=phone_number");
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp");
                    FragmentRecent.addToRecent("+"+ccp.getSelectedCountryCode() +phone.getText().toString(),msg.getText().toString());
                    Toast.makeText(context, ("Opening"), Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Can't Open WhatsApp It may not be installed", Toast.LENGTH_SHORT).show();
                }
            }
            else if (!msg.getText().toString().equals("")){
                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = msg.getText().toString();
                    PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(context, "Please enter mobile number or message", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}