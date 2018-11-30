package com.udacity.ronanlima.capstoneproject.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

/**
 *
 * Created by rlima on 19/11/18.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectVH> {

    @Getter
    private List<Project> list;
    FirebaseStorage storage;
    private Context mContext;

    public ProjectAdapter() {
        storage = FirebaseStorage.getInstance();
    }

    @NonNull
    @Override
    public ProjectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_project, parent, false);
        return new ProjectVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectVH holder, int position) {
        Project project = getList().get(position);
        holder.tvTitle.setText(project.getNomeProjeto());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop().fallback(R.drawable.ic_dashboard_black_24dp).placeholder(R.drawable.img_project_default);
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(project.getImagemCapa())
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        return getList() == null || getList().isEmpty() ? 0 : getList().size();
    }

    public void setList(List<Project> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ProjectVH extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_poster_project)
        ImageView ivPoster;
        @BindView(R.id.tv_title_project)
        TextView tvTitle;

        public ProjectVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
