package com.projects.questmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateQuestActivity extends AppCompatActivity {

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
//    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);
        setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");

        userName = findViewById(R.id.userName);
        newPartyName = findViewById(R.id.newPartyName);
        setEntryPass = findViewById(R.id.setEntryPass);
        setAdminPass = findViewById(R.id.setAdminPass);
        setLimit = findViewById(R.id.setLimit);
        creationConfirm = findViewById(R.id.creationConfirm);
        description = findViewById(R.id.questDescription);
        location = findViewById(R.id.questLocation);
//        userName.setText(PlayerPrefs.playerName);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
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
                }
                catch (NumberFormatException e) {
                    limit = 0;
                }

                if (limit >1) {
                    c = true;
                    activateButton();
                } else {
                    c = false;
                }
            }
        });

        creationConfirm.setOnClickListener(v->CreateParty());
    }
//    FirebaseDatabase database;

    private void CreateParty() {

//
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.partyName);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.admin);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.password);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.adminPassword);
//        Log.println(Log.DEBUG,"myplay",PlayerPrefs.limit.toString());
//
//        database=FirebaseDatabase.getInstance("https://multiplayergame-996aa-default-rtdb.europe-west1.firebasedatabase.app/");
//        roomRef=database.getReference().child("rooms");
//        roomRef.child(PlayerPrefs.partyName).child("admin").setValue(PlayerPrefs.admin);
//        roomRef.child(PlayerPrefs.partyName).child("password").setValue(PlayerPrefs.password);
//        roomRef.child(PlayerPrefs.partyName).child("adminPassword").setValue(PlayerPrefs.adminPassword);
//        roomRef.child(PlayerPrefs.partyName).child("limit").setValue(PlayerPrefs.limit);

String questName, adminName, adminPass, userPass, urlImage, isConfirmedByHQ, questDescription,usersLimit,questLocation;
        questName=newPartyName.getText().toString();
        //todo: create static fields final
        adminName=(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userPass=setEntryPass.getText().toString();
        adminPass=setAdminPass.getText().toString();
        usersLimit=(setLimit.getText().toString());
        questDescription=description.getText().toString();
        questLocation=location.getText().toString();
//
        // Create a new quest with a first and last name
//        Map<String, Object> quest = new HashMap<>();
//        quest.put("questName", questName);
//        quest.put("adminName", adminName);
//        quest.put("adminPass", adminPass);
//        quest.put("userPass", userPass);
//        quest.put("usersLimit", usersLimit);
//        quest.put("urlImage", "1815");
//        quest.put("isConfirmedByHQ", "true");
//        quest.put("questDescription", questDescription);

QuestInfo quest=new QuestInfo(questName, adminName, adminPass,  userPass,  "urlImage",  "isConfirmedByHQ",  questDescription, usersLimit, questLocation);

// Add a new document with a generated ID
        db.collection("Quests")
                .add(quest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

//        Intent intent=new Intent(CreateQuestActivity.this,TaskManagementActivity.class);
//        startActivity(intent);
    }

    private void activateButton() {
        if (a && b && c) {
            creationConfirm.setEnabled(true);
            creationConfirm.setTextColor(Color.WHITE);
        }
        else {
            creationConfirm.setEnabled(false);
            creationConfirm.setTextColor(Color.GRAY);
        }
    }
}