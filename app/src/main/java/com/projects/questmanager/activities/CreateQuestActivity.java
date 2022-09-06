package com.projects.questmanager.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;
import com.projects.questmanager.QuestInfo;
import com.projects.questmanager.R;
import com.squareup.picasso.Picasso;

public class CreateQuestActivity extends AppCompatActivity {

    public static boolean isEditing = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView userName;
    private EditText newPartyName;
    private EditText setEntryPass;
    private EditText setAdminPass;
    private EditText setLimit;
    private Button creationConfirm;
    Editable setLimitVal;
    boolean a, b, c;
    int limit;
    private EditText description;
    private EditText location;
    private ImageButton menuButton;

    private ImageView showImage;
    private Button addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");
        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());
        userName = findViewById(R.id.userName);
        newPartyName = findViewById(R.id.newPartyName);
        setEntryPass = findViewById(R.id.setEntryPass);
        setAdminPass = findViewById(R.id.setAdminPass);
        setLimit = findViewById(R.id.setLimit);
        creationConfirm = findViewById(R.id.creationConfirm);
        description = findViewById(R.id.questDescription);
        location = findViewById(R.id.questLocation);
        showImage = findViewById(R.id.showImage);
        addImage = findViewById(R.id.addImage);
        addImage.setOnClickListener(v -> selectImage());
        try {
            Picasso.get().load(PlayerPreferences.currentQuest.getUrlImage()).into(showImage);
        } catch (Exception e) {}
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        creationConfirm.setOnClickListener(v -> CreateParty());

        try {
            if (isEditing) {
                a = true;
                b = true;
                c = true;
                newPartyName.setText(PlayerPreferences.currentQuest.getQuestName());
                setEntryPass.setText(PlayerPreferences.currentQuest.getUserPass());
                setAdminPass.setText(PlayerPreferences.currentQuest.getAdminPass());
                setLimit.setText(PlayerPreferences.currentQuest.getUsersLimit());
                description.setText(PlayerPreferences.currentQuest.getQuestDescription());
                location.setText(PlayerPreferences.currentQuest.getQuestLocation());
                activateButton();
                isEditing = false;
            }
        } catch (Exception e) {
            newPartyName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before,
                                          int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {
                    //avoid triggering event when text is too short
                    if (s.length() >= 3) {
                        a = true;
                        activateButton();
                    } else {
                        a = false;
                    }
                }
            });

            setEntryPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before,
                                          int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {
                    //avoid triggering event when text is too short
                    if (s.length() >= 3) {
                        b = true;
                        activateButton();
                    } else {
                        b = false;
                    }
                }
            });
            setLimit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void onTextChanged(final CharSequence s, int start, int before,
                                          int count) {

                }

                @Override
                public void afterTextChanged(final Editable s) {
                    //avoid triggering event when text is too short
                    setLimitVal = setLimit.getText();
                    try {
                        limit = Integer.parseInt(String.valueOf(setLimitVal));
                    } catch (NumberFormatException e) {
                        limit = 0;
                    }

                    if (limit > 1) {
                        c = true;
                        activateButton();
                    } else {
                        c = false;
                    }
                }
            });
        }
    }

    private void selectImage() {
        PlayerPreferences.questName = newPartyName.getText().toString();
        PlayerPreferences.adminName = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        PlayerPreferences.userPass = setEntryPass.getText().toString();
        PlayerPreferences.adminPass = setAdminPass.getText().toString();
        PlayerPreferences.usersLimit = (setLimit.getText().toString());
        PlayerPreferences.questDescription = description.getText().toString();
        PlayerPreferences.questLocation = location.getText().toString();

        //todo: here must be called updateinfo

        Intent intentSelectImage = new Intent(CreateQuestActivity.this, ImageSelectActivity.class);
        startActivity(intentSelectImage);
    }

    private void CreateParty() {
        String questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription, usersLimit, questLocation;
        questName = newPartyName.getText().toString();
        //todo: create static fields final
        adminName = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userPass = setEntryPass.getText().toString();
        adminPass = setAdminPass.getText().toString();
        usersLimit = (setLimit.getText().toString());
        questDescription = description.getText().toString();
        questLocation = location.getText().toString();
        String questID = "default";
        urlImage = PlayerPreferences.urlLink;

        QuestInfo quest = new QuestInfo(questName, adminName, adminPass, userPass, urlImage, "isConfirmedByHQ", questDescription, usersLimit, questLocation, questID);
        quest.setQuestID(PlayerPreferences.currentQuest.getQuestID());
        PlayerPreferences.currentQuest = quest;

        try {
            MyUtils.updateQuestInfo(quest, PlayerPreferences.currentQuest.getQuestID());
            Intent intentManagement = new Intent(CreateQuestActivity.this, TaskManagementActivity.class);
            startActivity(intentManagement);
        } catch (Exception e) {
// Add a new document with a generated ID
            db.collection("Quests")
                    .add(quest)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            PlayerPreferences.currentQuest = quest;
                            PlayerPreferences.currentQuest.setQuestID(documentReference.getId());
                            MyUtils.updateQuestInfo(quest, PlayerPreferences.currentQuest.getQuestID());
                            Intent intentManagement = new Intent(CreateQuestActivity.this, TaskManagementActivity.class);
                            startActivity(intentManagement);
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

    private void activateButton() {
        if (a && b && c) {
            creationConfirm.setEnabled(true);
            creationConfirm.setTextColor(Color.WHITE);
        } else {
            creationConfirm.setEnabled(false);
            creationConfirm.setTextColor(Color.GRAY);
        }
    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(CreateQuestActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "My quests":
                        Intent intentMyQuests = new Intent(CreateQuestActivity.this, MyQuestActivity.class);
                        startActivity(intentMyQuests);
                        break;

                    case "Create quest":
                        Intent intentCreate = new Intent(CreateQuestActivity.this, ImageSelectActivity.class);
//                        Intent intentCreate = new Intent(MainMenuActivity.this, CreateQuestActivity.class);
                        startActivity(intentCreate);
                        break;
                    case "All quests":
                        Intent intentMain = new Intent(CreateQuestActivity.this, MainMenuActivity.class);
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