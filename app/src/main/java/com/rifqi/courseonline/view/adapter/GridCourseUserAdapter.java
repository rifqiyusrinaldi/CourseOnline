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
import com.rifqi.courseonline.model.entities.view.VCourse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridCourseUserAdapter extends RecyclerView.Adapter<GridCourseUserAdapter.GridViewHolder> {
    private List<VCourse> listCourse;
    private RoomDB database;
    private Activity context;
    private OnItemClickCallBack onItemClickCallback;
    private List<RatingData> ratingData = new ArrayList<RatingData>();
    private String imageFileName;
    private String imageFileNameMentor;
    RequestOptions roPlaceholder;
    public GridCourseUserAdapter(Context context, List<VCourse> listData){
        this.context = (Activity) context;
        this.listCourse = listData;
        notifyDataSetChanged();}
    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_course, parent, false);
        return new GridViewHolder(view);
    }
    public void setOnItemClickCallback(OnItemClickCallBack onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onBindViewHolder(@NonNull final GridViewHolder holder, int position) {
        VCourse course = listCourse.get(position);
        MentorData mentorData = database.mentorDao().getMentor(course.getIdMentor());
        database = RoomDB.getInstance(context);
        holder.namaKursus.setText(course.getNama());
        holder.hargaKursus.setText(course.getHarga());
        holder.typeKursus.setText(course.getType());
        holder.kategoriKursus.setText(course.getKategori());
        holder.ratingKursus.setText(ratingAvg(course.getID()));
        imageFileName = course.getGambar();
        String target = FileHelper.FILE_SCHEMA + FileHelper.getCourseThumbPath(context);
        target += imageFileName;
        Uri targetUri = Uri.parse(target);
        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(roPlaceholder)
                .load(targetUri)
                .centerCrop()
                .into(holder.gambarKursus);
        imageFileNameMentor = mentorData.getGambar();
        String targetMentor = FileHelper.FILE_SCHEMA + FileHelper.getProfileThumbPath(context);
        targetMentor += imageFileNameMentor;
        Uri targetUriMentor = Uri.parse(targetMentor);
        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(roPlaceholder)
                .load(targetUriMentor)
                .centerCrop()
                .into(holder.mentorImg);
        holder.itemView.setOnClickListener(v -> {
            onItemClickCallback.onItemClicked(course);
        });
    }

    @Override
    public int getItemCount() {
        return listCourse.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView namaKursus;
        TextView hargaKursus;
        TextView kategoriKursus;
        TextView typeKursus, mentorJob;
        TextView ratingKursus, mentorName;
        ImageView gambarKursus;
        CircleImageView mentorImg;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKursus = itemView.findViewById(R.id.tv_nama_kursus);
            hargaKursus = itemView.findViewById(R.id.tv_harga_khursus);
            ratingKursus = itemView.findViewById(R.id.tv_rating);
            typeKursus = itemView.findViewById(R.id.tv_type_kursus);
            kategoriKursus = itemView.findViewById(R.id.tv_kategori_kursus);
            gambarKursus = itemView.findViewById(R.id.img_kursus);
            mentorJob = itemView.findViewById(R.id.tv_pemateri_master_khursus);
            mentorName = itemView.findViewById(R.id.tv_pemateri_khursus);
            mentorImg = itemView.findViewById(R.id.iv_pemateri);
            roPlaceholder = new RequestOptions().placeholder(R.drawable.ic_union);
        }
    }
    public interface OnItemClickCallBack {
        void onItemClicked(VCourse data);
    }
    private String ratingAvg(int id_course) {
        float rateAvg = 0, total = 0;
        database = RoomDB.getInstance(context);
        ratingData = database.ratingDao().courseUserRating(id_course);
        for(int i = 0; i < ratingData.size(); i++ ){
            total += ratingData.get(i).getRating();
        }
        rateAvg = total/ratingData.size();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return decimalFormat.format(rateAvg) ;
    }
}
