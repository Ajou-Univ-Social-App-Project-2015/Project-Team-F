package com.sap.team_f.cook;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by KyungTack on 2015-10-29.
 */
public class StartActivity extends Activity {

    public static boolean isInit = false;
    public static ParseUser currentUser = null;
    public static String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(isInit==false)
        {
            Parse.initialize(this, "tpbdNdyQ4hOngQCvtcMN3wzScHZGZXzWODMZvMbE", "olnEhtF4zxa1uk9YloYkA03vW60yuseqXVTM8oJU");
            isInit = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.startactivity);

        Button memBtn = (Button)findViewById(R.id.memberBtn);
        memBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StartActivity.this, "로그인 해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Button nonmemBtn = (Button)findViewById(R.id.nonmemberBtn);
        nonmemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                currentUser=null;
                startActivity(intent);
                finish();
            }
        });

        Button signinBtn = (Button)findViewById(R.id.signinBtn);
            signinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText idText = (EditText) findViewById(R.id.startIdText);
                    String id = idText.getText().toString();
                    EditText passText = (EditText) findViewById(R.id.startPassText);
                    final String pass = passText.getText().toString();
                    ParseUser.logInInBackground(id, pass, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                currentUser = user;
                                Toast.makeText(StartActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                intent.putExtra("isMember", true);
                                password=pass;
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                // Hooray! The user is logged in.
                            } else {
                                Toast.makeText(StartActivity.this, "아이디나 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                // Signup failed. Look at the ParseException to see what happened.
                            }
                        }
                    });
            }
        });



        Button signupBtn = (Button)findViewById(R.id.startSignupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
