package me.nandunb.newsreporter.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import me.nandunb.newsreporter.R;
import me.nandunb.newsreporter.models.Post;

/**
 * Created by nandunb on 4/9/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<Post> postsList;

    public NewsAdapter(Context context, List<Post> postsList){
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.username.setText(post.getDisplayName());
        holder.datetime.setText(post.getCreatedOn().toString());
        holder.likes.setText(String.format("%d",post.getLikes()));
        holder.caption.setText(post.getCaption());

//        Glide.with(context).load(post.getPhotoUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView caption, likes, username, datetime;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            caption = (TextView) view.findViewById(R.id.txtCardCaption);
            likes = (TextView) view.findViewById(R.id.txtLikes);
//            image = (ImageView) view.findViewById(R.id.card_image);
            username = (TextView) view.findViewById(R.id.card_username);
            datetime = (TextView) view.findViewById(R.id.card_date);
        }
    }
}
