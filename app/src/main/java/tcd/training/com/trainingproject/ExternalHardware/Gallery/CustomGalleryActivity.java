package tcd.training.com.trainingproject.ExternalHardware.Gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

import tcd.training.com.trainingproject.R;

public class CustomGalleryActivity extends AppCompatActivity {

    private static final int MENU_SELECT = 1;

    private int mCount;
    private boolean[] mThumbnailSelections;
    private String[] mArrPath;
    private int[] mIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gallery);

        initializeUiComponents();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);

        mCount = imageCursor.getCount();
        mThumbnailSelections = new boolean[mCount];
        mArrPath = new String[mCount];
        mIds = new int[mCount];

        int imageIdColumn = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
        for (int i = 0; i < mCount; i++) {
            imageCursor.moveToPosition(i);
            mIds[i] = imageCursor.getInt(imageIdColumn);
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            mArrPath[i] = imageCursor.getString(dataColumnIndex);
        }

        imageCursor.close();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Select")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT:
                // get the selected images
                ArrayList<String> selectedImagesList = new ArrayList<>();
                for (int i = 0; i < mCount; i++) {
                    if (mThumbnailSelections[i]) {
                        selectedImagesList.add(mArrPath[i]);
                    }
                }
                // return to previous activity
                if (selectedImagesList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.data), selectedImagesList);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeUiComponents() {
        GridView imagesGridView = findViewById(R.id.gv_images);
        ImageAdapter imageAdapter = new ImageAdapter();
        imagesGridView.setAdapter(imageAdapter);
    }

    private void setBitmap(final ImageView imageView, final int id) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                imageView.setImageBitmap(result);
            }
        }.execute();
    }

    public class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_gallery_item, null);
                holder.thumbnailImageView = convertView.findViewById(R.id.iv_thumbnail);
                holder.pickCheckbox = convertView.findViewById(R.id.cb_pick);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.pickCheckbox.setId(position);
            holder.thumbnailImageView.setId(position);

            holder.pickCheckbox.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (mThumbnailSelections[id]) {
                        cb.setChecked(false);
                        mThumbnailSelections[id] = false;
                    } else {
                        cb.setChecked(true);
                        mThumbnailSelections[id] = true;
                    }
                }
            });
            holder.thumbnailImageView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int id = holder.pickCheckbox.getId();
                    if (mThumbnailSelections[id]) {
                        holder.pickCheckbox.setChecked(false);
                        mThumbnailSelections[id] = false;
                    } else {
                        holder.pickCheckbox.setChecked(true);
                        mThumbnailSelections[id] = true;
                    }
                }
            });
            try {
                setBitmap(holder.thumbnailImageView, mIds[position]);
            } catch (Throwable e) {
            }
            holder.pickCheckbox.setChecked(mThumbnailSelections[position]);
            holder.id = position;
            return convertView;
        }
    }

    class ViewHolder {
        ImageView thumbnailImageView;
        CheckBox pickCheckbox;
        int id;
    }
}
