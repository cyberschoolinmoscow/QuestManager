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
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskUserAdaptor extends RecyclerView.Adapter<TaskUserAdaptor.MyViewHolder> {

    private List<TaskInfo> list;
    private Context context;
//    private LayoutInflater inflater;

    public TaskUserAdaptor(Context context, List<TaskInfo> itemList) {
        this.context = context;
        this.list = itemList;
    }

    @NonNull
    @Override
    public TaskUserAdaptor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskUserAdaptor.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_task_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskUserAdaptor.MyViewHolder holder, int position) {
        holder.taskName.setText(list.get(position).getTaskName());
        holder.taskLoc.setText(list.get(position).getTaskLoc());
        holder.taskDescription.setText(list.get(position).getTaskDescription());
        //todo:hint only
        holder.correctAnswer.setText(list.get(position).getCorrectAnswer());

        holder.checkButton.setOnClickListener(v->checkAnswer(position,holder));
//        holder.deleteButton.setOnClickListener(v->deleteTask());
    }

    private void checkAnswer(int position, MyViewHolder holder) {
        String userAnswer=holder.correctAnswer.getText().toString();
        if (userAnswer.equals(list.get(position).getCorrectAnswer())){
           holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.checkButton.setVisibility(View.GONE);
        }else {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        Map<String, Object> user = new HashMap<>();
        user.put("userName", PlayerPreferences.playerName);
        user.put("questID", PlayerPreferences.currentQuest.getQuestID());
        user.put("taskAnswer",userAnswer);
        user.put("taskID",list.get(position).getTaskID());
        MyUtils.addUserInfo(user);
//        MyUtils.updateUserInfo(user);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        TextView taskName, taskLoc, taskDescription;
        EditText correctAnswer;
        Button checkButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.layout);
//            linearLayout.setBackgroundResource(R.drawable.abc);
            taskName = itemView.findViewById(R.id.taskName);
            taskLoc = itemView.findViewById(R.id.taskLoc);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            correctAnswer = itemView.findViewById(R.id.correctAnswer);
            checkButton = itemView.findViewById(R.id.checkButton);
//            deleteButton = itemView.findViewById(R.id.deleteButton);


        }
    }
}



