package com.projects.questmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.questmanager.utils.PlayerPreferences;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.R;
import com.projects.questmanager.activities.TaskManagementActivity;
import com.projects.questmanager.activities.TaskUserActivity;
import com.projects.questmanager.activities.UserQuestActivity;
import com.projects.questmanager.activities.CreateQuestActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<QuestInfo> list;
    private Context context;
//    private LayoutInflater inflater;

    public MyAdapter(Context context, List<QuestInfo> partyNameList) {
        this.context = context;
        this.list = partyNameList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.quest_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.partyName.setText(list.get(position).getQuestName());
        holder.location.setText(list.get(position).getQuestLocation());
        holder.descriptionText.setText(list.get(position).getQuestDescription());
        try {
            Uri uri = list.get(position).getUrlImage();
            Picasso.get().load(uri).into(holder.imgBackground);
        } catch (Exception e) {
        }
        holder.descriptionButton.setOnClickListener(v -> holder.openDescription(position));

        if (PlayerPreferences.playerName.equals(list.get(position).getAdminName())) {
            holder.joinButton.setText("EDIT TASKS");
            holder.joinButton.setOnClickListener(v -> holder.editTasks(position));

            holder.descriptionButton.setText("EDIT INFO");
            holder.descriptionButton.setOnClickListener(v -> holder.editInfo(position));

        } else {
            holder.joinButton.setText("JOIN");
            holder.joinButton.setOnClickListener(v -> holder.joining(position));

            holder.descriptionButton.setVisibility(View.GONE);
//            holder.descriptionButton.setText("INFO");
//            holder.descriptionButton.setOnClickListener(v -> holder.info(position));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        TextView partyName;
        TextView location;
        TextView descriptionText;
        ImageView imgBackground;
        Button descriptionButton, joinButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
            partyName = itemView.findViewById(R.id.partyName);
            location = itemView.findViewById(R.id.questLocation);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            imgBackground = itemView.findViewById(R.id.imgBackground);
            descriptionButton = itemView.findViewById(R.id.descriptionButton);
            joinButton = itemView.findViewById(R.id.joinButton);
        }

        public void joining(int position) {
            PlayerPreferences.currentQuest = list.get(position);
            Intent intent = new Intent(context, TaskUserActivity.class);
            context.startActivity(intent);
        }

        public void editTasks(int position) {
            PlayerPreferences.currentQuest = list.get(position);
            Intent intent = new Intent(context, TaskManagementActivity.class);
            context.startActivity(intent);

        }

        public void openDescription(int position) {
        }

        public void editInfo(int position) {
            PlayerPreferences.currentQuest = list.get(position);
            Intent intent = new Intent(context, CreateQuestActivity.class);
            context.startActivity(intent);
        }

        public void info(int position) {
            PlayerPreferences.currentQuest = list.get(position);
            Intent intent = new Intent(context, UserQuestActivity.class);
            context.startActivity(intent);
        }
    }
}
