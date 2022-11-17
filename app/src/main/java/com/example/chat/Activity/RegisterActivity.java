package com.example.chat.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView txtdk;
    Button btnDK;
    EditText edtName, edtPass, edtRePass, edtEmail;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri imageUri;
    String imageURI;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        anhXa();

        dialog=new ProgressDialog(this);
        dialog.setMessage("đợi CHAT một chút nhé!");
        dialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                                    dialog.show();

                String Name=edtName.getText().toString();
                String Email=edtEmail.getText().toString();
                String Pass=edtPass.getText().toString();
                String RePass=edtRePass.getText().toString();
                String status="Chào tui đang dùng app mình tạo ra nè!";


                if(TextUtils.isEmpty(Name)||TextUtils.isEmpty(Email)
                        ||TextUtils.isEmpty(Pass)||TextUtils.isEmpty(RePass))
                {
                    thongBao("Vui lòng nhập đủ thông tin");
                }else  if(!Pass.equals(RePass)){
                    thongBao("Mật khẩu không trùng khớp");
                }else if(Pass.length()<6){
                    thongBao("Vui lòng kiểm tra lại độ dài mật khẩu");
                }else {

                    auth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference=storage.getReference().child("uplod").child(auth.getUid());

                                if(imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI=uri.toString();
                                                        Users users=new Users(auth.getUid(),Name,Email, imageURI, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                                }else {
                                                                    thongBao("Đăng ký không thành công");
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status="Chào tui đang dung app mình tạo ra nè!";
                                    imageURI="https://firebasestorage.googleapis.com/v0/b/chat-88ea6.appspot.com/o/pro.png?alt=media&token=90598958-d2c3-4caf-a8b8-c8b93fe3fded";
                                    Users users=new Users(auth.getUid(),Name,Email, imageURI, status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                dialog.dismiss();
                                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                            }else {
                                                thongBao("Lỗi tạo mới");
                                            }
                                        }
                                    });
                                }
                            }else {
                                dialog.dismiss();
                                thongBao("Tạo tài khoản không thành công");
                            }
                        }
                    });
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Tack Image"), 10);
            }
        });

        txtdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }
    public void anhXa(){
        btnDK=findViewById(R.id.btnDK);
        edtName=findViewById(R.id.edtreg_Name);
        edtEmail=findViewById(R.id.edtreg_Email);
        edtPass=findViewById(R.id.edtreg_Password);
        edtRePass=findViewById(R.id.edtreg_RePass);
        txtdk=findViewById(R.id.txtdk);
        profile_image=findViewById(R.id.profile);
    }
    public  void thongBao(String a){
        Toast.makeText(RegisterActivity.this, a,Toast.LENGTH_SHORT ).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){
            if(data!=null){
                imageUri=data.getData();
                profile_image.setImageURI(imageUri);
            }
        }
    }
}