package com.example.educor_app.Chat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.educor_app.R;

public class StartActivity extends AppCompatActivity {

    Button login, register;
    TextView chat_title_tv;
    Typeface MR, MRR;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String Classnames = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");


        //check if user is null
        if (firebaseUser != null){
            Intent intent = new Intent(StartActivity.this, MainActivity_chat.class);
            intent.putExtra("name",Classnames);
            intent.putExtra("key",key);
            intent.putExtra("Teacher",Teacher);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        String Classnames = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");



        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        chat_title_tv = findViewById(R.id.chat_title_tv);

        login.setTypeface(MR);
        register.setTypeface(MR);
        chat_title_tv.setTypeface(MR);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this, LoginActivity_chat.class);
                intent.putExtra("name",Classnames);
                intent.putExtra("key",key);
                intent.putExtra("Teacher",Teacher);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StartActivity.this, RegisterActivity_chat.class);
                intent.putExtra("name",Classnames);
                intent.putExtra("key",key);
                intent.putExtra("Teacher",Teacher);
            }
        });
    }
}
