package com.demo.ilpr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class LikesFragment extends Fragment {
    private static final String TAG = LikesFragment.class.getSimpleName();

    private ProgressBar mProgress;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    private LikesViewModel mModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.likes_fragment, container, false);

        mModel = new ViewModelProvider(getActivity()).get(LikesViewModel.class);

        mProgress = rootView.findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ImagesLoader loader = mModel.getImagesLoader();
        ImagesProvider imagesProvider = mModel.getImagesProvider();
        imagesProvider.init(this::onImagesLoadedCallback, loader);

        mAdapter = new CustomAdapter(imagesProvider);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onImagesLoadedCallback(Void v) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Log.d(TAG, "data loaded");
            mProgress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.invalidate();
        });
    }

}
