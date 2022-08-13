package com.projects.questmanager;

import android.net.Uri;

public class QuestInfo {
    private String questName;
    private String adminName;
    private String adminPass;
    private String userPass;
    private String urlImage;
    private String isConfirmedByHQ;
    private String questDescription;
    private String usersLimit;
    private String questLocation;
    private String questID;

    public QuestInfo(String questName, String adminName, String adminPass, String userPass, String urlImage, String isConfirmedByHQ, String questDescription, String usersLimit, String questLocation, String questID) {
        this.questName = questName;
        this.adminName = adminName;
        this.adminPass = adminPass;
        this.userPass = userPass;
        this.urlImage = urlImage;
        this.isConfirmedByHQ = isConfirmedByHQ;
        this.questDescription = questDescription;
        this.usersLimit = usersLimit;
        this.questLocation = questLocation;
        this.questID = questID;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public void setAdminPass(String adminPass) {
        this.adminPass = adminPass;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public Uri getUrlImage() {
        try {
            if (urlImage.equals(null)) {
                return Uri.parse("https://firebasestorage.googleapis.com/v0/b/questmanager-b70bf.appspot.com/o/images%2Fphoto_2022-02-03_14-08-37.jpg?alt=media&token=3c4b543d-66af-445c-af3a-b5d3ad4fb716");
            }
            return Uri.parse(urlImage);
        } catch (Exception
                e) {
            return null;
        }

    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getIsConfirmedByHQ() {
        return isConfirmedByHQ;
    }

    public void setIsConfirmedByHQ(String isConfirmedByHQ) {
        this.isConfirmedByHQ = isConfirmedByHQ;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    public String getUsersLimit() {
        return usersLimit;
    }

    public void setUsersLimit(String usersLimit) {
        this.usersLimit = usersLimit;
    }

    public String getQuestLocation() {
        return questLocation;
    }

    public void setQuestLocation(String questLocation) {
        this.questLocation = questLocation;
    }

    public String getQuestID() {
        return questID;
    }

    public void setQuestID(String questID) {
        this.questID = questID;
    }
}
