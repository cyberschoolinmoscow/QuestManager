package com.projects.questmanager.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.projects.questmanager.R;
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
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_quest);
        setTitle("\t\t\t\t\t\t\t\t\t\t\tCreate new party");

        menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> OpenMenu());
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
        Intent intent = new Intent(UserQuestActivity.this, TaskUserActivity.class);
        startActivity(intent);
    }

    private void activateButton() {

        entranceButton.setVisibility(View.VISIBLE);
        entranceButton.setTextColor(Color.WHITE);

    }

    private void OpenMenu() {
        PopupMenu popupMenu = new PopupMenu(UserQuestActivity.this, menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getTitle().toString()) {
                    case "My quests":
                        Intent intentMyQuests = new Intent(UserQuestActivity.this, MyQuestActivity.class);
                        startActivity(intentMyQuests);
                        break;

                    case "Create quest":
                        Intent intentCreate = new Intent(UserQuestActivity.this, ImageSelectActivity.class);
                        startActivity(intentCreate);
                        break;
                    case "All quests":
                        Intent intentMain = new Intent(UserQuestActivity.this, MainMenuActivity.class);
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
