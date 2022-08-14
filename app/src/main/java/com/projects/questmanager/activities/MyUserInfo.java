package com.projects.questmanager.activities;

public class MyUserInfo {
    private final String userName;
    private final String questID;
    private final String taskAnswer;
    private final String taskID;

    public MyUserInfo(String userName, String questID, String taskAnswer, String taskID) {
        this.userName=userName;
        this.questID=questID;
        this.taskAnswer=taskAnswer;
        this.taskID=taskID;
    }

    public String getQuestID() {
        return questID;
    }
}
