package com.udacity.ronanlima.capstoneproject.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.ronanlima.capstoneproject.R;
import com.udacity.ronanlima.capstoneproject.data.Project;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;

/**
 * Created by rlima on 19/11/18.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectVH> {

    @Getter
    private List<Project> list;
    private Context mContext;
    private OnProjectClickListener clickListener;

    public ProjectAdapter(OnProjectClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ProjectVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_project, parent, false);
        return new ProjectVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectVH holder, final int position) {
        Project project = getList().get(position);
        holder.tvTitle.setText(project.getNomeProjeto());
        Picasso.get().load(project.getImagemCapa()).into(holder.ivPoster);
        holder.ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClickListener(getItem(holder.getAdapterPosition()), holder.ivPoster, holder.tvTitle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getList() == null || getList().isEmpty() ? 0 : getList().size();
    }

    private Project getItem(int position) {
        return list.get(position);
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

    public interface OnProjectClickListener {
        void onItemClickListener(Project project, View... views);
    }
}
