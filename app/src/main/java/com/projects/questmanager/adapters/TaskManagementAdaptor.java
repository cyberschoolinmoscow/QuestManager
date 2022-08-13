package com.projects.questmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.projects.questmanager.R;
import com.projects.questmanager.TaskInfo;
import com.projects.questmanager.utils.PlayerPreferences;

import java.util.List;


public class TaskManagementAdaptor extends RecyclerView.Adapter<TaskManagementAdaptor.MyViewHolder> {

    private List<TaskInfo> list;
    private Context context;
//    private LayoutInflater inflater;

    public TaskManagementAdaptor(Context context, List<TaskInfo> itemList) {
        this.context = context;
        this.list = itemList;
    }

    @NonNull
    @Override
    public TaskManagementAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskManagementAdaptor.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item_cardwiew, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskManagementAdaptor.MyViewHolder holder, int position) {
        holder.taskName.setText(list.get(position).getTaskName());
        holder.taskLoc.setText(list.get(position).getTaskLoc());
        holder.taskDescription.setText(list.get(position).getTaskDescription());
        holder.correctAnswer.setText(list.get(position).getCorrectAnswer());
        holder.editButton.setOnClickListener(v->editTask(position));
        holder.deleteButton.setOnClickListener(v->deleteTask());
    }

    private void deleteTask() {
    }

    private void editTask(int position) {
        PlayerPreferences.taskID=list.get(position).getTaskID();

        //??????????????
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        TextView taskName, taskLoc, taskDescription;
        EditText correctAnswer;
        Button editButton, deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
//            linearLayout.setBackgroundResource(R.drawable.abc);
            taskName = itemView.findViewById(R.id.taskName);
            taskLoc = itemView.findViewById(R.id.taskLoc);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            correctAnswer = itemView.findViewById(R.id.correctAnswer);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);


        }
    }
}


