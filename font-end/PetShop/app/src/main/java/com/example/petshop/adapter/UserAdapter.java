package com.example.petshop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petshop.R;
import com.example.petshop.interfaces.ItemClickListener;
import com.example.petshop.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
    private List<User> list;

    public UserAdapter(List<User> list){
        this.list = list;
    }

    public void setList(List<User> list) {
        this.list = list;
    }
    private static ItemClickListener onItemClickListener;

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        UserAdapter.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        if(user!=null){
            holder.tv_fullname.setText(user.getFullName());
            holder.tv_phone.setText(user.getPhone());
            if(user.getRole() == 0) {
                holder.tv_role.setText("Admin");
            } else{
                holder.tv_role.setText("User");
            }
        }
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.OnItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public User getItem(int position){
        if(list ==null || position> list.size()){
        return null;
        }
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fullname,tv_phone,tv_role;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fullname = itemView.findViewById(R.id.tv_fullname_user);
            tv_phone = itemView.findViewById(R.id.tv_phone_user);
            tv_role = itemView.findViewById(R.id.tv_role_user);

        }
    }
}
