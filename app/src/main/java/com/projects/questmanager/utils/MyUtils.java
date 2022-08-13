package com.projects.questmanager.utils;

import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.TaskInfo;

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
}
