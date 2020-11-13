package com.demo.ilpr;

import android.graphics.Bitmap;
import android.util.Log;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.media.ImageVersions;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineImageMedia;
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia;
import com.github.instagram4j.instagram4j.requests.feed.FeedSavedRequest;
import com.github.instagram4j.instagram4j.responses.feed.FeedSavedResponse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ImagesProvider {
    private static class LoadingTask extends Thread {
        private IGClient mClient;
        private Consumer<Void> mCallback;
        private ImagesLoader mLoader;
        private LinkedBlockingDeque<Bitmap> mFaces;

        public LoadingTask(IGClient client, ImagesLoader loader,
                           LinkedBlockingDeque<Bitmap> faces, Consumer<Void> callback) {
            mClient = client;
            mCallback = callback;
            mLoader = loader;
            mFaces = faces;
        }

        @Override
        public void run() {
            FeedSavedResponse saved = new FeedSavedRequest().execute(mClient).join();

            List<String> urls = saved.getItems().stream()
                    .map(this::extractUrl)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            for (String url : urls) {
                 mFaces.addAll(mLoader.loadFaces(url));
            }

            mCallback.accept(null);
        }

        private String extractUrl(TimelineMedia media) {
            if (!(media instanceof TimelineImageMedia)) {
                return null;
            }

            String url = null;
            TimelineImageMedia imageMedia = (TimelineImageMedia) media;
            try {
                Field privateField = TimelineImageMedia.class.getDeclaredField("image_versions2");
                privateField.setAccessible(true);
                ImageVersions versions = (ImageVersions) privateField.get(imageMedia);
                url = versions.getCandidates().get(0).getUrl();
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            return url;
        }
    }

    private static final String TAG = ImagesProvider.class.getSimpleName();
    private LinkedBlockingDeque<Bitmap> mBitmaps;
    private List<Bitmap> mFaces;
    private IGClient mClient;

    public ImagesProvider(IGClient client) {
        mClient = client;
        mBitmaps = new LinkedBlockingDeque<>();
        mFaces = new ArrayList<>();
    }

    public void init(Consumer<Void> callback, ImagesLoader loader) {
        LoadingTask task = new LoadingTask(mClient, loader, mBitmaps, Void -> {
            mFaces.addAll(mBitmaps);
            Log.d(TAG, String.format("initialization finished. loaded %d faces", mFaces.size()));
            callback.accept(null);
        });
        task.start();
    }

    public Bitmap get(int position) {
        return mFaces.get(position);
    }

    public int getImagesNumber() {
        return mFaces.size();
    }

}
