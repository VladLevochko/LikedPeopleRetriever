package com.demo.ilpr.detection;

import android.graphics.Bitmap;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MlKitDetector extends MyFaceDetector {

    private FaceDetector mDetector;

    public MlKitDetector() {
        FaceDetectorOptions options =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
        mDetector = FaceDetection.getClient(options);
    }

    @Override
    public List<Rect> detectFaces(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<List<Face>> result = mDetector.process(image);
        try {
            Tasks.await(result);
            List<Face> faces = result.getResult();
            return faces.stream().map(Face::getBoundingBox).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
