package com.projects.questmanager.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.questmanager.R;
import com.projects.questmanager.TaskInfo;
import com.projects.questmanager.activities.TaskManagementActivity;
import com.projects.questmanager.utils.MyUtils;
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
        holder.editButton.setOnClickListener(v->editTask(position,holder));
        holder.deleteButton.setOnClickListener(v->deleteTask(position));
//        holder.button.setOnClickListener(v->deleteTask(position));
    }

    private void deleteTask(int position) {
        PlayerPreferences.taskID=list.get(position).getTaskID();
        MyUtils.deleteTaskInfo(PlayerPreferences.taskID);
        Toast.makeText(context,"deleteTask",Toast.LENGTH_LONG).show();
//        list.remove(position);
//      MyUtils.updateTaskList();
//      list=PlayerPreferences.taskList;

        FirebaseFirestore.getInstance().collection("Tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  getResult
                                String s1 =  document.getData().get("taskName").toString();
                                String s3 = document.getData().get("taskDescription").toString();
                                String s4 = document.getData().get("correctAnswer").toString();
                                String s2 = document.getData().get("taskLoc").toString();
                                String s5 = document.getData().get("questID").toString();
                                String s6 =       document.getId().toString();
//                           document.getData().get("taskID").toString();
                                TaskInfo task1 = new TaskInfo(s1, s2, s3, s4, s5,s6);
                                MyUtils.updateTaskInfo(task1,s6);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(task1.getQuestID().equals(PlayerPreferences.currentQuest.getQuestID())){
                                    list.add(task1);}

                            }

//                            adaptor = new TaskManagementAdaptor(TaskManagementActivity.this, taskList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

//                            recyclerView.setAdapter(adaptor);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void editTask(int position, MyViewHolder holder) {

        PlayerPreferences.taskID=list.get(position).getTaskID();
       String name= holder.taskName.getText().toString();
      String loc=  holder.taskLoc.getText().toString();
      String desc=  holder.taskDescription.getText().toString();
      String ans=  holder.correctAnswer.getText().toString();
        TaskInfo taskInfo=new TaskInfo(name,loc,desc,ans,list.get(position).getQuestID(),list.get(position).getTaskID());

        MyUtils.updateTaskInfo(taskInfo,PlayerPreferences.taskID);
        Toast.makeText(context,"editTask",Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        EditText taskName, taskLoc, taskDescription;
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
            editButton = itemView.findViewById(R.id.editBtn);
            deleteButton = itemView.findViewById(R.id.deleteBtn);
//            button = itemView.findViewById(R.id.button);


        }
    }
}


