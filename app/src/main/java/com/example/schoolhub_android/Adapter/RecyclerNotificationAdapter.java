package com.example.schoolhub_android.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolhub_android.Common.Common;
import com.example.schoolhub_android.Database.DatabaseClient;
import com.example.schoolhub_android.Database.Entity.Notification;
import com.example.schoolhub_android.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerNotificationAdapter extends RecyclerView.Adapter<RecyclerNotificationAdapter.ViewHolder>{

    List<Notification> l_notifications;
    Context mContext;
    boolean isExpanded, isRead;

    SharedPreferences sharedPreferences;
    Set<String> s_isRead = new HashSet<>();
    Set<String> set = new HashSet<>();
    SharedPreferences.Editor editor;

    public RecyclerNotificationAdapter(List<Notification> l_notifications, Context mContext) {
        this.l_notifications = l_notifications;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listnotification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv_title.setText(l_notifications.get(position).getTitle());
        holder.tv_date.setText(l_notifications.get(position).getDate());
        holder.tv_body.setText(l_notifications.get(position).getBody());

        isExpanded = l_notifications.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        isRead = l_notifications.get(position).isRead();
        holder.constraintLayout.setBackgroundColor(isRead ? Color.parseColor("#ffc107") : Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return l_notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent_layout;
        ConstraintLayout constraintLayout, expandableLayout;
        TextView tv_title, tv_date, tv_body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout = itemView.findViewById(R.id.parent_layout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_body = itemView.findViewById(R.id.tv_body);

            sharedPreferences = mContext.getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE);
            s_isRead = sharedPreferences.getStringSet("s_isRead", set);

            tv_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification notification = l_notifications.get(getAdapterPosition());
                    notification.setExpanded(!notification.isExpanded());
                    if(!notification.isRead()) {
                        notification.setRead(true);
                        DatabaseClient.getInstance(mContext).getAppDatabase()
                                .notificationDao()
                                .updateRead(notification.getKey());
                        set.addAll(s_isRead);
                        set.add(notification.getKey());
                        editor = mContext.getSharedPreferences(Common.MY_SCHOOL, MODE_PRIVATE).edit();
                        editor.putStringSet("s_isRead", set);
                        editor.apply();
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}