package com.projects.questmanager.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.projects.questmanager.adapters.TaskUserAdaptor;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;

import java.util.ArrayList;
import java.util.List;

public class TaskUserActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView questName;
    private RecyclerView recyclerView;
    private FrameLayout frameLayoutManagement;
    private EditText taskName, taskDesc, taskCorrectAnswer, taskLoc, questID;
    private Button confirmAdding, cancelAdding, newTask;
    private ImageButton menuButton;

    private TaskUserAdaptor adaptor;

    private List<TaskInfo> taskList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_user);
        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());
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
//            confirmAdding = findViewById(R.id.confirmAdding);
//            cancelAdding = findViewById(R.id.cancelAdding);
//            newTask = findViewById(R.id.newTaskButton);
        taskLoc = findViewById(R.id.taskLoc);

        //todo: change edittext to textview
//            questID = findViewById(R.id.questID);
        try {
            questID.setText(PlayerPreferences.currentQuest.getQuestID());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String taskName, tascLoc, taskDescription, correctAnswer;
//        Toast.makeText(this, "l:" + taskList.size(), Toast.LENGTH_SHORT).show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adaptor = new TaskUserAdaptor(TaskUserActivity.this, taskList);
        recyclerView.setAdapter(adaptor);
        updateList();

//            newTask.setOnClickListener(v -> addNewTask());
//            confirmAdding.setOnClickListener(v -> confirmNewTask());
//            cancelAdding.setOnClickListener(v -> cancelNewTask());

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
        TaskInfo task = new TaskInfo(s1, s2, s3, s4, s5, "");

        try {
            MyUtils.updateTaskInfo(task, PlayerPreferences.taskID);
        } catch (Exception e) {
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
        }

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
                                String s1 = document.getData().get("taskName").toString();
                                String s3 = document.getData().get("taskDescription").toString();
                                String s4 = document.getData().get("correctAnswer").toString();
                                String s2 = document.getData().get("taskLoc").toString();
                                String s5 = document.getData().get("questID").toString();
                                TaskInfo task1 = new TaskInfo(s1, s2, s3, s4, s5, document.getId().toString());
                                MyUtils.updateTaskInfo(task1, task1.getTaskID());
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (task1.getQuestID().equals(PlayerPreferences.currentQuest.getQuestID())) {
                                    taskList.add(task1);
                                }

                            }

                            adaptor = new TaskUserAdaptor(TaskUserActivity.this, taskList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

                            recyclerView.setAdapter(adaptor);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void addNewTask() {
        frameLayoutManagement.setVisibility(View.VISIBLE);
        newTask.setVisibility(View.INVISIBLE);

    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(TaskUserActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "My quests":
                        Intent intentMyQuests = new Intent(TaskUserActivity.this, MyQuestActivity.class);
                        startActivity(intentMyQuests);
                        break;

                    case "Create quest":
                        Intent intentCreate = new Intent(TaskUserActivity.this, ImageSelectActivity.class);
//                        Intent intentCreate = new Intent(MainMenuActivity.this, CreateQuestActivity.class);
                        startActivity(intentCreate);
                        break;
                    case "All quests":
                        Intent intentMain = new Intent(TaskUserActivity.this, MainMenuActivity.class);
                        startActivity(intentMain);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });

        popupMenu.show();
    }
}
