package com.demo.ilpr;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.demo.ilpr.detection.BlazeFace;
import com.demo.ilpr.detection.MlKitDetector;
import com.demo.ilpr.detection.MyFaceDetector;
import com.github.instagram4j.instagram4j.IGClient;

public class LikesViewModel extends AndroidViewModel {
    private ImagesProvider mImagesProvider;
    private IGClient mIgClient;
    private ImagesLoader mImagesLoader;

    public LikesViewModel(@NonNull Application application) {
        super(application);

        Context context = application.getApplicationContext();
//        MyFaceDetector detector = BlazeFace.create(context.getAssets());
        MyFaceDetector detector = new MlKitDetector();
        mImagesLoader = new ImagesLoader(context, Runtime.getRuntime().availableProcessors(), detector);
    }

    public void setImagesProvider(ImagesProvider provider) {
        mImagesProvider = provider;
    }

    public ImagesProvider getImagesProvider() {
        return mImagesProvider;
    }

    public ImagesLoader getImagesLoader() {
        return mImagesLoader;
    }

    public void setImagesLoader(ImagesLoader imagesLoader) {
        this.mImagesLoader = imagesLoader;
    }

    public IGClient getIgClient() {
        return mIgClient;
    }

    public void setIgClient(IGClient client) {
        this.mIgClient = client;
    }
}
