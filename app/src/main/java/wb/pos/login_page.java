package wb.pos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class login_page extends AppCompatActivity {
TextInputEditText email,password;
Button sign_in;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email=findViewById(R.id.email_txt);
        password=findViewById(R.id.password_txt);
        sign_in=findViewById(R.id.sign_in);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("email",null)!=null&&prefs.getString("password",null)!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Email is Required");
                }else if(password.getText().toString().isEmpty()){
                    password.setError("Password is Required");
                }else if(password.getText().toString().length()<6){
                    password.setError("Password too short");
                }else{
                    network_operations.sign_in(email.getText().toString(),password.getText().toString(),login_page.this);
                }
            }
        });
    }
}
