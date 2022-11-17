package com.example.chat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Activity.ChatActivity;
import com.example.chat.ModelClass.Users;
import com.example.chat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {
    Context homeActivity;
    ArrayList<Users> userArrayList;

    public UserAdapter(Context homeActivity, ArrayList<Users> userArrayList) {
        this.homeActivity=homeActivity;
        this.userArrayList=userArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Users users=userArrayList.get(position);
        if(users==null){
            return;
        }

        holder.user_name.setText(users.name);
        holder.user_status.setText(users.status);
        Picasso.get().load(users.imageUri).into(holder.user_profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(homeActivity, ChatActivity.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("ReciverImage", users.getImageUri());
                intent.putExtra("uid", users.getUid());
                homeActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(userArrayList!=null){
            return userArrayList.size();
        }
        return 0;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView user_profile;
        TextView user_name, user_status;

        public Viewholder(@NonNull View itemView)
        {
            super(itemView);
            user_profile=itemView.findViewById(R.id.user_image);
            user_name=itemView.findViewById(R.id.user_name);
            user_status=itemView.findViewById(R.id.user_status);
        }
    }
}
