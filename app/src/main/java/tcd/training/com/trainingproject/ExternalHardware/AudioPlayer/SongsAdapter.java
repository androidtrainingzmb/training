package tcd.training.com.trainingproject.ExternalHardware.AudioPlayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tcd.training.com.trainingproject.R;

/**
 * Created by cpu10661-local on 10/08/2017.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {

    private ArrayList<Song> mSongsList;
    private OnItemClickListener mItemClickListener;

    public SongsAdapter(ArrayList<Song> mSongsList) {
        this.mSongsList = mSongsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Song song = mSongsList.get(position);
        holder.mSongNameTextView.setText(song.getName());
        holder.mSongArtistTextView.setText(song.getArtist());
    }

    @Override
    public int getItemCount() {
        return mSongsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mSongNameTextView;
        private TextView mSongArtistTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mSongNameTextView = itemView.findViewById(R.id.tv_song_name);
            mSongArtistTextView = itemView.findViewById(R.id.tv_song_artist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
