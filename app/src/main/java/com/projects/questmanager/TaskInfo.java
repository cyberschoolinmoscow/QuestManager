package com.projects.questmanager;

public class TaskInfo {
    String taskName;
    String taskLoc;
    String taskDescription;
    String correctAnswer;
    private String questID;

    public TaskInfo(String taskName, String taskLoc, String taskDescription, String correctAnswer, String questID) {
        this.taskName=taskName;
        this.taskLoc = taskLoc;
        this.taskDescription=taskDescription;
        this.correctAnswer=correctAnswer;
        this.questID=questID;

    }

    public String getTaskName() {
        return this.taskName;
    }

    public String getTaskLoc() {
        return this.taskLoc;
    }

    public String getTaskDescription() {
        return this.taskDescription;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public String getQuestID() {
        return questID;
    }
}
