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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;

/**
 * Created by KyungTack on 2015-11-03.
 */
public class SignupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupactivity);

        Button signupBtn = (Button)findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText idText = (EditText)findViewById(R.id.signupIdText);
                String id = idText.getText().toString();
                EditText passText = (EditText)findViewById(R.id.signupPassText);
                String pass = passText.getText().toString();
                EditText repassText = (EditText)findViewById(R.id.signupRePassText);
                String repass = repassText.getText().toString();
                EditText nickText = (EditText)findViewById(R.id.signupNickText);
                String nick = nickText.getText().toString();
                Log.v("pass",pass);
                Log.v("repass",repass);
                if(repass.equals(pass))
                {
                    ParseUser user = new ParseUser();
                    user.setUsername(id);
                    user.setPassword(pass);

                    user.put("Nickname", nick);
                    user.put("Like", new JSONArray());

                    ParseObject obj = new ParseObject("Comment");
                    obj.put("username",id);
                    obj.put("Comment",new JSONArray());
                    try {
                        obj.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(SignupActivity.this,"가입한 아이디로 로그인 해주세요",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignupActivity.this, StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                // Hooray! Let them use the app now.
                            } else {
                                Log.v("error",e.toString());
                                Toast.makeText(SignupActivity.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(SignupActivity.this,"비밀번호가 맞지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(SignupActivity.this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
