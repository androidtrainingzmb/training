package tcd.training.com.trainingproject.PersistentStorage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import tcd.training.com.trainingproject.R;

/**
 * Created by ADMIN on 10/07/2017.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NumberViewHolder> {

    private static final String TAG = NoteListAdapter.class.getSimpleName();

    final private ListItemClickListener mOnClickListener;
    private ArrayList<Note> mNotesList;

    public interface ListItemClickListener {
        void onListItemClick(int item);
    }

    public NoteListAdapter(ListItemClickListener listItemClickListener) {
        mOnClickListener = listItemClickListener;
        mNotesList = new ArrayList<>();
    }

    public void clear() {
        mNotesList.clear();
    }

    public void addNoteToList(Note note) {
        mNotesList.add(note);
    }

    public Note getItem(int index) {
        return mNotesList.get(index);
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.list_note_item, viewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Note note = mNotesList.get(position);
        Context context = holder.noteTitleTextView.getContext();
        holder.noteTitleTextView.setText(note.getTitle());
        holder.storageTypeTextView.setText(note.getStorageType());
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }


    class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView noteTitleTextView;
        TextView storageTypeTextView;

        public NumberViewHolder(View itemView) {
            super(itemView);

            noteTitleTextView = (TextView) itemView.findViewById(R.id.tv_note_title);
            storageTypeTextView = (TextView) itemView.findViewById(R.id.tv_storage_type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }
}

