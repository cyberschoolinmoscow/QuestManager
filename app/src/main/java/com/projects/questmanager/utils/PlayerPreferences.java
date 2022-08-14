package com.projects.questmanager.utils;

import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.TaskInfo;

import java.util.List;

public class PlayerPreferences {
    public static String questName;
    public static String playerName;
    //жизненный цикл объектов в Java
    public static String urlLink;
    public static String adminName;
    public static String userPass;
    public static String adminPass;
    public static String usersLimit;
    public static String questDescription;
    public static String questLocation;
    public static QuestInfo currentQuest;

    public static String userID;

    public static String taskID;
    public static List<TaskInfo> taskList;
}
