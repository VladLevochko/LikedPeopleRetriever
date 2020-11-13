package com.demo.ilpr.detection;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.List;

public abstract class MyFaceDetector {

    public abstract List<Rect> detectFaces(Bitmap image);
}
