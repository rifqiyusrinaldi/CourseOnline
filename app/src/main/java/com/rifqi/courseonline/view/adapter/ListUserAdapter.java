package com.rifqi.courseonline.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rifqi.courseonline.R;
import com.rifqi.courseonline.helper.FileHelper;
import com.rifqi.courseonline.model.RoomDB;
import com.rifqi.courseonline.model.entities.UserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListViewHolder> {
    private List<UserData> userlist;
    private RoomDB database;
    private Activity context;
    private String imageFileName;
    RequestOptions roPlaceholder;

    public ListUserAdapter(Context context, List<UserData> listData) {
        this.userlist = listData;
        this.context = (Activity) context;
        notifyDataSetChanged();
    }
    public void updateData(List<UserData> listData){
        this.userlist = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListUserAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListUserAdapter.ListViewHolder holder, int position) {
        final UserData userData = userlist.get(position);
        database = RoomDB.getInstance(context);
        holder.tvName.setText(userData.getNama());
        holder.tvJK.setText(userData.getJk());
        holder.tvRating.setVisibility(View.GONE);
        imageFileName = userData.getGambar();
        String target = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(context);
        target += imageFileName;
        Uri targetUri = Uri.parse(target);
        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(roPlaceholder)
                .load(targetUri)
                .centerCrop()
                .into(holder.userImg);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserData d = userlist.get(holder.getAdapterPosition());
                database.userDao().delete(d);
                int position = holder.getAdapterPosition();
                userlist.remove(position);
                notifyItemChanged(position);
                notifyItemRangeChanged(position,userlist.size());
            }
        });
        holder.btnDelete.setOnClickListener(view -> {
            holder.userImg.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvJK, tvRating;
        ImageView btnDelete;
        CircleImageView userImg;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nama_user_list);
            tvJK = itemView.findViewById(R.id.tv_jk_user_list);
            tvRating = itemView.findViewById(R.id.tv_rating);
            btnDelete = itemView.findViewById(R.id.iv_delete_user_list);
            userImg = itemView.findViewById(R.id.iv_user);
            roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);
        }
    }
}
