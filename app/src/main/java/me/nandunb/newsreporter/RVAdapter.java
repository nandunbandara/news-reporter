package me.nandunb.newsreporter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.nandunb.newsreporter.helpers.DownloadImageTask;
import me.nandunb.newsreporter.models.Post;

/**
 * Created by nandunb on 4/1/18.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PostViewHolder> {

    List<Post> posts;

    RVAdapter(List<Post> posts){
        this.posts = posts;
    }

    @Override
    public int getItemCount(){
        return posts.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_layout, parent, false);
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.displayName.setText(posts.get(position).getDisplayName());
        holder.createdOn.setText(posts.get(position).getCreatedOn().toString());
        holder.caption.setText(posts.get(position).getCaption());

        new DownloadImageTask(holder.imgPreview).execute(posts.get(position).getPhotoUrl());
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView imgPreview;
        TextView displayName;
        TextView createdOn;
        TextView caption;

        PostViewHolder(View itemView){
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            imgPreview = (ImageView) itemView.findViewById(R.id.imgPreview);
            displayName = (TextView) itemView.findViewById(R.id.txtDisplayName);
            createdOn = (TextView) itemView.findViewById(R.id.txtCreatedOn);
            caption = (TextView) itemView.findViewById(R.id.txtCaption);

        }

    }

}
