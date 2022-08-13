package com.projects.questmanager;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MyUtils {
    public static void updateQuestInfo(QuestInfo questInfo,String questID) {
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
}
