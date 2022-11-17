package com.example.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button btnDN;
    TextView txtdk;
    EditText edtEmail, edtPassword;

    FirebaseAuth auth;

    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhXa();

        auth=FirebaseAuth.getInstance();

        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edtEmail.getText().toString();
                String pass=edtPassword.getText().toString();

                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(pass)){
                    thongBao("Vui lòng nhập đủ thông tin");
                }else if(pass.length()<6){
                    edtPassword.setError("Mật khẩu không đúng");
                    thongBao("Vui lòng kiểm tra độ dài mật khẩu");
                }else {
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            }else {
                                thongBao("Vui lòng kiểm tra lại Email và mật khẩu!");
                            }
                        }
                    });
                }
            }
        });

        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }
    public void anhXa(){
        btnDN= findViewById(R.id.btnDN);
        txtdk=findViewById(R.id.txtdk);
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
    }

    public  void thongBao(String a){
        Toast.makeText(LoginActivity.this, a,Toast.LENGTH_SHORT ).show();
    }
}