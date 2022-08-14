package com.projects.questmanager.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.questmanager.R;
import com.projects.questmanager.TaskInfo;
import com.projects.questmanager.adapters.TaskManagementAdaptor;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;

import java.util.ArrayList;
import java.util.List;

public class TaskManagementActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView questName;
    private RecyclerView recyclerView;
    private FrameLayout frameLayoutManagement;
    private EditText taskName, taskDesc, taskCorrectAnswer, taskLoc, questID;
    private Button confirmAdding, cancelAdding, newTask;

    private TaskManagementAdaptor adaptor;

    private List<TaskInfo> taskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        updateList();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);

        questName = findViewById(R.id.questName);

        try {
            questName.setText(PlayerPreferences.currentQuest.getQuestName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = findViewById(R.id.recyclerView);
        frameLayoutManagement = findViewById(R.id.frameLayoutManagement);
        taskName = findViewById(R.id.taskName);
        taskDesc = findViewById(R.id.taskDesc);
        taskCorrectAnswer = findViewById(R.id.taskCorrAnswer);
        confirmAdding = findViewById(R.id.confirmAdding);
        cancelAdding = findViewById(R.id.cancelAdding);
        newTask = findViewById(R.id.newTaskButton);
        taskLoc = findViewById(R.id.taskLoc);

        //todo: change edittext to textview
//        questID = findViewById(R.id.questID);
        try{
            questID.setText(PlayerPreferences.currentQuest.getQuestID());
        }
        catch (Exception e){
         e.printStackTrace();
        }
//        String taskName, tascLoc, taskDescription, correctAnswer;
//        Toast.makeText(this, "l:" + taskList.size(), Toast.LENGTH_SHORT).show();
        updateList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptor = new TaskManagementAdaptor(TaskManagementActivity.this, taskList);
        recyclerView.setAdapter(adaptor);

//        updateList();

        newTask.setOnClickListener(v -> addNewTask());
        confirmAdding.setOnClickListener(v -> confirmNewTask());
        cancelAdding.setOnClickListener(v -> cancelNewTask());

    }

    private void cancelNewTask() {
        frameLayoutManagement.setVisibility(View.INVISIBLE);
        newTask.setVisibility(View.VISIBLE);
    }

    private void confirmNewTask() {
        String s1 = taskName.getText().toString();
        String s3 = taskDesc.getText().toString();
        String s4 = taskCorrectAnswer.getText().toString();
        String s2 = taskLoc.getText().toString();
        String s5 = PlayerPreferences.currentQuest.getQuestID();

        taskName.setText("");
        taskDesc.setText("");
        taskCorrectAnswer.setText("");
        taskLoc.setText("");
//        String s5 = questID.getText().toString();
        TaskInfo task = new TaskInfo(s1, s2, s3, s4, s5,"");

//        try {
//            MyUtils.updateTaskInfo(task,PlayerPreferences.currentQuest.getQuestID());
//        }catch (Exception e) {
            db.collection("Tasks")
                    .add(task)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            frameLayoutManagement.setVisibility(View.INVISIBLE);
                            newTask.setVisibility(View.VISIBLE);

                            updateList();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
//        }

    }

    private void updateList() {
        db.collection("Tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            taskList.clear();
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
                                taskList.add(task1);}

                            }

                            adaptor = new TaskManagementAdaptor(TaskManagementActivity.this, taskList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

                            recyclerView.setAdapter(adaptor);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
//        MyUtils.updateTaskList();
//
//      taskList=  PlayerPreferences.taskList;
//        adaptor = new TaskManagementAdaptor(TaskManagementActivity.this, taskList);
//        recyclerView.setAdapter(adaptor);
    }

    private void addNewTask() {
        frameLayoutManagement.setVisibility(View.VISIBLE);
        newTask.setVisibility(View.INVISIBLE);

    }


//
//    Handler handler = new Handler();
//    Runnable runnable;
//    int delay = 1*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.
//
//
//    @Override
//    protected void onResume() {
//        //start handler as activity become visible
//
//        handler.postDelayed( runnable = new Runnable() {
//            public void run() {
//                //do something
//updateList();
//                Log.d(TAG, taskList.size()+"");
//        adaptor = new TaskManagementAdaptor(TaskManagementActivity.this, taskList);
//        recyclerView.setAdapter(adaptor);
//                handler.postDelayed(runnable, delay);
//            }
//        }, delay);
//
//        super.onResume();
//    }
//
//// If onPause() is not included the threads will double up when you
//// reload the activity
//
//    @Override
//    protected void onPause() {
//        handler.removeCallbacks(runnable); //stop handler when activity not visible
//        super.onPause();
//    }
}
