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
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

import tcd.training.com.trainingproject.R;

public class CustomGalleryActivity extends AppCompatActivity {

    private ArrayList<Boolean> mThumbnailSelections = new ArrayList<>();
    private int mCount;
    private ArrayList<String> mArrPath = new ArrayList<>();
    private ArrayList<Integer> mIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gallery);

        initializeUiComponents();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        int image_column_index = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
        mCount = imageCursor.getCount();
        for (int i = 0; i < mCount; i++) {
            imageCursor.moveToPosition(i);
            mIds.add(imageCursor.getInt(image_column_index));
            int dataColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            mArrPath.add(imageCursor.getString(dataColumnIndex));
        }

        imageCursor.close();
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }

    private void initializeUiComponents() {
        GridView imagesGridView = findViewById(R.id.gv_images);
        ImageAdapter imageAdapter = new ImageAdapter();
        imagesGridView.setAdapter(imageAdapter);


        Button selectButton = findViewById(R.id.btn_select);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int len = mThumbnailSelections.size();
                int count = 0;
                String selectImages = "";
                for (int i = 0; i < len; i++) {
                    if (mThumbnailSelections.get(i)) {
                        count++;
                        selectImages = selectImages + mArrPath.get(i) + "|";
                    }
                }
                if (count == 0) {
                    Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("SelectedImages", selectImages);
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.data), selectImages);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void setBitmap(final ImageView imageView, final int id) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(
                        getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
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
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.custom_gallery_item, null);
                holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
                holder.pickCheckbox = (CheckBox) convertView.findViewById(R.id.cb_pick);

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
                    if (mThumbnailSelections.get(id)) {
                        cb.setChecked(false);
                        mThumbnailSelections.set(id, false);
                    } else {
                        cb.setChecked(true);
                        mThumbnailSelections.set(id, true);
                    }
                }
            });
            holder.thumbnailImageView.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int id = holder.pickCheckbox.getId();
                    if (mThumbnailSelections.get(id)) {
                        holder.pickCheckbox.setChecked(false);
                        mThumbnailSelections.set(id, false);
                    } else {
                        holder.pickCheckbox.setChecked(true);
                        mThumbnailSelections.set(id, true);
                    }
                }
            });
            try {
                setBitmap(holder.thumbnailImageView, mIds.get(position));
            } catch (Throwable e) {
            }
            holder.pickCheckbox.setChecked(mThumbnailSelections.get(position));
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
