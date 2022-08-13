package com.projects.questmanager;

public class TaskInfo {
    private String taskID;
    String taskName;
    String taskLoc;
    String taskDescription;
    String correctAnswer;
    private String questID;

    public TaskInfo(String taskName, String taskLoc, String taskDescription, String correctAnswer, String questID,String taskID) {
        this.taskName=taskName;
        this.taskLoc = taskLoc;
        this.taskDescription=taskDescription;
        this.correctAnswer=correctAnswer;
        this.questID=questID;
        this.taskID=questID;
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

    public String getTaskID() {
        return taskID;
    }
}
