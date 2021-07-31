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
import com.rifqi.courseonline.model.entities.MentorData;
import com.rifqi.courseonline.model.entities.RatingData;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMentorAdapter extends RecyclerView.Adapter<ListMentorAdapter.ListViewHolder> {
    private List<MentorData> mentorList;
    private List<RatingData> ratingData = new ArrayList<RatingData>();
    private RoomDB database;
    private Activity context;
    private OnItemClickCallBack onItemClickCallback;
    private String imageFileName;
    RequestOptions roPlaceholder;

    public ListMentorAdapter(Context context, List<MentorData> listData) {
        this.mentorList = listData;
        this.context = (Activity) context;
        notifyDataSetChanged();
    }
    public void updateData(List<MentorData> listData){
        this.mentorList = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListMentorAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
        return new ListViewHolder(view);
    }
    public void setOnItemClickCallback(OnItemClickCallBack onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMentorAdapter.ListViewHolder holder, int position) {
        final MentorData mentorData = mentorList.get(position);
        database = RoomDB.getInstance(context);
        holder.tvName.setText(mentorData.getNama());
        holder.tvJK.setText(mentorData.getPekerjaan());
        holder.tvRating.setText(ratingAvg(mentorData.getID()));
        imageFileName = mentorData.getGambar();
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
                MentorData d = mentorList.get(holder.getAdapterPosition());
                database.mentorDao().delete(d);
                int position = holder.getAdapterPosition();
                mentorList.remove(position);
                notifyItemChanged(position);
                notifyItemRangeChanged(position,mentorList.size());
            }
        });
        holder.itemView.setOnClickListener(v -> {
            onItemClickCallback.onItemClicked(mentorData);
        });
    }

    private String ratingAvg(int id_mentor) {
        float rateAvg = 0, total = 0;
        database = RoomDB.getInstance(context);
        ratingData = database.ratingDao().getMentorRating(id_mentor);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += ratingData.get(i).getRating();
        }
        rateAvg = total/ratingData.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rateAvg) ;
    }

    @Override
    public int getItemCount() {
        return mentorList.size();
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
    public interface OnItemClickCallBack {
        void onItemClicked(MentorData mentorData);
    }
}
