package com.example.educor_app.Notifications;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.educor_app.Authentications.Dashboard;
import com.example.educor_app.Authentications.ProfileActivity;
import com.example.educor_app.Chat.StartActivity;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    ArrayList<Notification_data> list;
    Notification_adapter adapter;
    RecyclerView recyclerView;
    private FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    public String imgurl;
    private ActionBar actionBar;
    Window window;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Notification");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        String Classname = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent lntentss=new Intent(Notification.this, ProfileActivity.class);
                        lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classname);
                        lntentss.putExtra("Teacher", Teacher);
                        startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        Intent intent=new Intent(Notification.this, Dashboard.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        Intent lntent=new Intent(Notification.this, StartActivity.class);
                        lntent.putExtra("name",Classname);
                        lntent.putExtra("Teacher", Teacher);
                        lntent.putExtra("key", key);
                        startActivity(lntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        Intent lntents=new Intent(Notification.this, Event_page.class);
                        lntents.putExtra("name",Classname);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        return true;
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.myRecyclers);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<Notification_data>();
        user = FirebaseAuth.getInstance().getCurrentUser();

       reference = FirebaseDatabase.getInstance().getReference().child("Notification").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Notification_data rm = dataSnapshot1.getValue(Notification_data.class);
                    assert rm != null;
                    imgurl=rm.getImgUrl();
                    list.add(rm);
                }
                adapter = new Notification_adapter(Notification.this, list,imgurl,Classname,key,Teacher);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Notification.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}