package com.example.chat.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.Adapter.UserAdapter;
import com.example.chat.ModelClass.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView RecyclerViewUser;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> userArrayList;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhXa();

        RecyclerViewUser=findViewById(R.id.RecyclerViewUser);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        userArrayList=new ArrayList<>();

        DatabaseReference reference=database.getReference().child("user");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){

                    Users users=dataSnapshot.getValue(Users.class);
                    userArrayList.add(users);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        RecyclerViewUser.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(this, userArrayList);
        RecyclerViewUser.setAdapter(adapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Xin Chào!")
                        .setMessage("Bạn có chắc chắn muốn thoát?")
                        .setPositiveButton("Có", null)
                        .setNegativeButton("Không", null)
                        .show();

                Button btn=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            }
        });

        if(auth.getCurrentUser()==null)
        {
            startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
        }

    }

    public void anhXa(){
        RecyclerViewUser=findViewById(R.id.RecyclerViewUser);
        logout=findViewById((R.id.img_logout));
    }
}