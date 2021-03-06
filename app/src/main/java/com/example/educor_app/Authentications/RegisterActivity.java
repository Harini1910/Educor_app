package com.example.educor_app.Authentications;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.Classroom.Classrooms;
import com.example.educor_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    public Button register;
    public TextView signin_button;
    EditText Name,Email,Password;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //new
    EditText name,email,phone_number,institution;
    TextView qualification;
    Button submit;
    Spinner spinner;
    String Qualification;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public static String Mail;
    public static String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference("signup");

        signin_button=findViewById(R.id.sign_in_button);
        Name=findViewById(R.id.username);
        Email=findViewById(R.id.email);
        Password=findViewById(R.id.password);
        phone_number=findViewById(R.id.Phone_number);
        institution=findViewById(R.id.institution);
        spinner=findViewById(R.id.Spinner);

        final LottieAnimationView lottie_smily=findViewById(R.id.login_smily_anim);
        lottie_smily.setSpeed(1);
        lottie_smily.playAnimation();


        List<String> categories=new ArrayList<>();
        categories.add("Qualification");
        categories.add("Student");
        categories.add("Teacher");

        ArrayAdapter<String> dataAdapter;
        dataAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Qualification")){
                    //do nothing
                    Qualification=null;
                }
                else{
                    //on selecting sppiner item
                    String item=parent.getItemAtPosition(position).toString();
                    //anything u want do here
                    Qualification=spinner.getSelectedItem().toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //TODO AUTOGENERATED METHOD DO HERE
            }
        });

        register=findViewById(R.id.Register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkConnection();
            }
        });

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= manager.getActiveNetworkInfo();
        if (null==activeNetwork){
            Intent intent=new Intent(RegisterActivity.this,network_indicator.class);
            intent.putExtra("from","RegisterActivity");
            startActivity(intent);
            finish();
        }else{

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

            if(Name.length()==0){
                Name.setError("Enter Name!");
            }else if(Email.length()==0){
                Email.setError("Enter Email!");
            }else if(phone_number.length()==0){
                phone_number.setError("Enter Phone Number!");
            }else if(institution.length()==0){
                institution.setError("Enter Institution!");
            } else if(Qualification==null){
                Toast.makeText(RegisterActivity.this, " select Qualification",
                        Toast.LENGTH_SHORT).show();
            }else{

                final String Names= Name.getText().toString();
                final String Emails=Email.getText().toString();
                final String Phone_number=phone_number.getText().toString();
                final String Institution=institution.getText().toString();
                final String password=Password.getText().toString();
                final signup info=new signup(Names,Emails,Phone_number,Institution,Qualification,user.getUid());

                mAuth.createUserWithEmailAndPassword(Emails, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                FirebaseDatabase.getInstance().getReference("signup").child(mAuth.getCurrentUser().getUid())
                        .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    register.setVisibility(View.INVISIBLE);
                                    final LottieAnimationView lottiePreloader=findViewById(R.id.preloader_button);

                                    lottiePreloader.setVisibility(View.VISIBLE);
                                    lottiePreloader.setSpeed(1);
                                    lottiePreloader.playAnimation();

                                    Thread timer = new Thread() {
                                        public void run() {
                                            try {
                                                sleep(3000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } finally {
                                                Intent intent = new Intent(RegisterActivity.this, Classrooms.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    };
                                    timer.start();

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //code ends
            }
          /*  */
        }
    }
}