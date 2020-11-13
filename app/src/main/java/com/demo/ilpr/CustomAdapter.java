package com.demo.ilpr;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_view);
        }

        public void setImage(Bitmap bitmap) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    private static final String TAG = CustomAdapter.class.getSimpleName();

    private ImagesProvider mImagesProvider;

    public CustomAdapter(ImagesProvider imagesProvider) {
        mImagesProvider = imagesProvider;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage(mImagesProvider.get(position));
    }

    @Override
    public int getItemCount() {
        int count = mImagesProvider.getImagesNumber();
        Log.d(TAG, "dataset size " + count);
        return count;
    }

}
