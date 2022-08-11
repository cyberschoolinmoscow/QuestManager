package com.projects.questmanager;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        setTitle("Menu");
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuButton=findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v->OpenMenu());
    }

    private void OpenMenu() {
        PopupMenu popupMenu=new PopupMenu(MainMenuActivity.this,menuButton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Three")) {
                    Toast.makeText(MainMenuActivity.this, "J O P A", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        popupMenu.show();
    }
}