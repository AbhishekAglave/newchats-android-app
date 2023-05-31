package com.abhishek.newchats.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.newchats.R;

import java.util.ArrayList;

public class RecentRecyclerListAdapter extends RecyclerView.Adapter<RecentRecyclerListAdapter.ViewHolder> {

    Context context;
    ArrayList<RecentItemModel> data;

    public RecentRecyclerListAdapter(Context context, ArrayList<RecentItemModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.profilePicHolder.setImageResource(data.get(position).img);
        holder.numberHolder.setText(data.get(position).number);
        holder.msgHolder.setText(data.get(position).msg);
        holder.recentListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data.get(position).img==R.drawable.ic_baseline_check_box_outline_blank_24 || data.get(position).img==R.drawable.ic_outline_check_box_24){
                    FragmentRecent.selectUnSelect(position);
                }else{
                    FragmentRecent.openChat(position);
                }

            }
        });
        holder.recentListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                PopupMenu popupMenu = new PopupMenu(context, v, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Copy")){
                            FragmentRecent.copyPhone(position);
                        }else if(menuItem.getTitle().equals("Dial")){
                            FragmentRecent.dialPhone(position);
                        }else if(menuItem.getTitle().equals("Select")){
                            FragmentRecent.selectionMode(position);
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numberHolder, msgHolder;
        ImageView profilePicHolder;
        LinearLayout recentListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            numberHolder = itemView.findViewById(R.id.number_holder);
            msgHolder = itemView.findViewById(R.id.msg_holder);
            profilePicHolder = itemView.findViewById(R.id.profile_pic_holder);
            recentListItem = itemView.findViewById(R.id.recent_list_item);
        }
    }
}
