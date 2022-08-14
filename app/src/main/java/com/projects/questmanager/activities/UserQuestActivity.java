package com.projects.questmanager.activities;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.questmanager.R;
import com.projects.questmanager.utils.MyUtils;
import com.projects.questmanager.utils.PlayerPreferences;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UserQuestActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView userName;
    private TextView newPartyName;
    private EditText entryPass;
    private Button entranceButton;
    private TextView description;
    private TextView location;
    private ImageView showImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quest);
        setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");

        userName = findViewById(R.id.userName);
        newPartyName = findViewById(R.id.partyName);
        entryPass = findViewById(R.id.entryPass);
        entranceButton = findViewById(R.id.joinButton);
        description = findViewById(R.id.questDescription);
        location = findViewById(R.id.questLocation);
        showImage = findViewById(R.id.showImage);

        try {
            Picasso.get().load(PlayerPreferences.currentQuest.getUrlImage()).into(showImage);
        } catch (Exception e) {

        }
//        userName.setText(PlayerPrefs.playerName);
        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        newPartyName.setText(PlayerPreferences.currentQuest.getQuestName());

        entryPass.addTextChangedListener(new TextWatcher() {
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

                    activateButton();
                } else {

                }
            }
        });

        entranceButton.setOnClickListener(v -> joinQuest());

    }

    private void joinQuest() {

        if(!entryPass.getText().toString().equals(PlayerPreferences.currentQuest.getUserPass())){

            entranceButton.setText("Wrong password");
            entryPass.setTextColor(getResources().getColor(R.color.red));
            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("userName", PlayerPreferences.playerName);
        user.put("questID", PlayerPreferences.currentQuest.getQuestID());
        user.put("taskID", "");
        user.put("taskAnswer", "");
//        PlayerPreferences.userID=documentReference.getId().toString();
        Intent intent = new Intent(UserQuestActivity.this, TaskUserActivity.class);
        startActivity(intent);
//try{
//    MyUtils.updateUserInfo(user);
//}
//catch (Exception e) {
//    db.collection("Users")
//            .add(user)
//            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                    PlayerPreferences.userID=documentReference.getId().toString();
//                    Intent intent = new Intent(UserQuestActivity.this, TaskUserActivity.class);
//                    startActivity(intent);
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.w(TAG, "Error adding document", e);
//                }
//            });
////        }
//}


    }

    private void activateButton() {

        entranceButton.setVisibility(View.VISIBLE);
        entranceButton.setTextColor(Color.WHITE);

    }
}
