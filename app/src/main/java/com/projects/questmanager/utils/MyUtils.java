package com.projects.questmanager.utils;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.TaskInfo;
import com.projects.questmanager.activities.TaskManagementActivity;
import com.projects.questmanager.activities.TaskUserActivity;
import com.projects.questmanager.activities.UserQuestActivity;
import com.projects.questmanager.adapters.TaskManagementAdaptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyUtils {
    public static void updateQuestInfo(QuestInfo questInfo, String questID) {
        Map<String,Object> m=new HashMap<>();
        m.put("questName",questInfo.getQuestName());
        m.put("adminName",questInfo.getAdminName());
        m.put("adminPass",questInfo.getAdminPass());
        m.put("userPass",questInfo.getUserPass());
        m.put("urlImage",questInfo.getUrlImage());
        m.put("isConfirmedByHQ",questInfo.getIsConfirmedByHQ());
        m.put("questDescription",questInfo.getQuestDescription());
        m.put("usersLimit",questInfo.getUsersLimit());
        m.put("questLocation",questInfo.getQuestLocation());
        m.put("questID",questID);
        FirebaseFirestore.getInstance().collection("Quests")
                .document(questID)
                .update(m);
    }

    public static void updateTaskInfo(TaskInfo taskInfo, String taskID) {
        TaskInfo t=new TaskInfo("","","","","","");
        Map<String,Object> m=new HashMap<>();
        m.put("taskName",taskInfo.getTaskName());
        m.put("taskLoc",taskInfo.getTaskLoc());
        m.put("taskDescription",taskInfo.getTaskDescription());
        m.put("correctAnswer",taskInfo.getCorrectAnswer());
        m.put("questID",taskInfo.getQuestID());
        m.put("taskID",taskID);

        FirebaseFirestore.getInstance().collection("Tasks")
                .document(taskID)
                .update(m);
    }

    public static void updateUserInfo(Map<String, Object> user) {
        FirebaseFirestore.getInstance().collection("Users")
                .document(PlayerPreferences.userID)
                .update(user);
    }

    public static void addUserInfo(Map<String, Object> user) {
        FirebaseFirestore.getInstance().collection("Users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        PlayerPreferences.userID=documentReference.getId().toString();
//                        Intent intent = new Intent(UserQuestActivity.this, TaskUserActivity.class);
//                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static void deleteTaskInfo(String taskID) {
        FirebaseFirestore.getInstance().collection("Tasks")
                .document(taskID)
                .delete();
    }

    public static void updateTaskList() {
        List<TaskInfo> taskList=new ArrayList<>();
        FirebaseFirestore.getInstance().collection("Tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            taskList.clear();
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



//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");


                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
PlayerPreferences.taskList= taskList;
    }
}
