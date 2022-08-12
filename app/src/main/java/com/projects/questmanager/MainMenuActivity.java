package com.projects.questmanager;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton menuButton;
    private RecyclerView recyclerView;

    private List<String> partyNameList=new ArrayList<>();
    private MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        setTitle("");
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        menuButton=findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v->OpenMenu());

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        partyNameList.add("a a a");
        partyNameList.add("j o p a a a");
        partyNameList.add("biba");
        partyNameList.add("Dina");

        adapter = new MyAdapter(MainMenuActivity.this, partyNameList);

//        Log.println(Log.DEBUG,"mytag",partyNameList.size()+"");

        recyclerView.setAdapter(adapter);


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