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
import me.nandunb.newsreporter.models.Draft;
import me.nandunb.newsreporter.models.Post;

/**
 * Created by nandunb on 4/11/18.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DraftViewHolder> {

    private Context context;
    private List<Draft> draftList;

    public DraftAdapter(Context context, List<Draft> draftList){
        this.context = context;
        this.draftList = draftList;
    }

    @NonNull
    @Override
    public DraftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draft_card_layout, parent, false);

        return new DraftViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DraftAdapter.DraftViewHolder holder, int position) {
        Draft draft = draftList.get(position);
        holder.datetime.setText(draft.getDateTime());
        holder.caption.setText(draft.getCaption());

        Glide.with(context).load(draft.getPhotoUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return draftList.size();
    }

    public class DraftViewHolder extends RecyclerView.ViewHolder {
        public TextView caption, datetime;
        public ImageView image;

        public DraftViewHolder(View view) {
            super(view);
            caption = (TextView) view.findViewById(R.id.draft_txtCardCaption);
            image = (ImageView) view.findViewById(R.id.draft_card_image);
            datetime = (TextView) view.findViewById(R.id.draft_card_date);
        }
    }

}
