package com.projects.questmanager.utils;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.TaskInfo;
import com.projects.questmanager.activities.TaskUserActivity;
import com.projects.questmanager.activities.UserQuestActivity;

import java.util.HashMap;
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
}
