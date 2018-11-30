package com.udacity.ronanlima.capstoneproject.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.ronanlima.capstoneproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rlima on 19/11/18.
 */

public class DecorationFragment extends Fragment {

    @BindView(R.id.textView)
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decoration, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }
}
