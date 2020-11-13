package com.demo.ilpr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

import com.demo.ilpr.detection.BlazeFace;
import com.demo.ilpr.detection.MyFaceDetector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class ImagesLoader {

    private static class LoaderTask implements Runnable {
        private String mUrl;
        private Consumer<Bitmap> mCallback;
        private Bitmap mBitmap;

        public LoaderTask(String url, Consumer<Bitmap> callback) {
            mUrl = url;
            mCallback = callback;
        }

        @Override
        public void run() {
            try (InputStream in = new URL(mUrl).openStream()) {
                mBitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mCallback != null) {
                mCallback.accept(mBitmap);
            }
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }

    private static final String TAG = ImagesLoader.class.getSimpleName();
    private static final int KEEP_ALIVE = 3;

    private ThreadPoolExecutor mExecutor;
    private LinkedBlockingDeque<Runnable> mTasksQueue;
    private MyFaceDetector mDetector;

    public ImagesLoader(Context context, int poolSize, MyFaceDetector detector) {
        mTasksQueue = new LinkedBlockingDeque<>();
        mExecutor = new ThreadPoolExecutor(poolSize, poolSize, KEEP_ALIVE, TimeUnit.SECONDS, mTasksQueue);
        mDetector = detector;
    }

    public void loadImage(String url, Consumer<Bitmap> callback) {
        mExecutor.execute(new LoaderTask(url, callback));
    }

    public List<Bitmap> loadImages(List<String> urls) {
        List<Bitmap> result = new ArrayList<>();
        List<LoaderTask> tasks = new ArrayList<>();
        for (String url : urls) {
            LoaderTask task = new LoaderTask(url, null);
            tasks.add(task);
            mExecutor.submit(task);
        }

        return null;
    }

    public void loadFaces(String url, Consumer<List<Bitmap>> callback) {
        Log.d(TAG, "loading faces for " + url);
//        mExecutor.execute(new LoaderTask(url, bitmap -> extractFaces(bitmap, callback)));
    }

    public List<Bitmap> loadFaces(String url) {
        try {
            LoaderTask task = new LoaderTask(url, null);
            mExecutor.submit(task).get();
            return extractFaces(task.getBitmap());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private List<Bitmap> extractFaces(Bitmap image) {
        List<Rect> faceRects = mDetector.detectFaces(image);
        List<Bitmap> faces = new ArrayList<>();
        for (Rect rect : faceRects) {
            Bitmap face = Bitmap.createBitmap(image, rect.left, rect.top, rect.width(), rect.height());
            faces.add(face);
        }

        Log.d(TAG, String.format("found %d faces", faces.size()));

        return faces;
    }

}
