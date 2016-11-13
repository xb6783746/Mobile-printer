package com.mobileprinter.View.DitherScreen;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.mobileprinter.Interfaces.DitherScreen;
import com.mobileprinter.Interfaces.DitherScreenPresenter;
import com.mobileprinter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DitherView extends Fragment implements DitherScreen{


    public DitherView() {
        // Required empty public constructor
    }


    private DitherScreenPresenter presenter;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dither_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initControls(View view){

        Button fsb = (Button)view.findViewById(R.id.floydSteinbergDitheringButton);
        fsb.setOnClickListener((x) -> floydSteinbergDithering());

        Button atkb = (Button)view.findViewById(R.id.atkinsonDitheringButton);
        atkb.setOnClickListener((x) -> atkinsonDithering());

        Button stcb = (Button)view.findViewById(R.id.stuckiDitheringButton);
        stcb.setOnClickListener((x) -> stuckiDithering());

        Button print = (Button)view.findViewById(R.id.printDitheredButton);
        print.setOnClickListener((x) -> print());

        imageView = (ImageView)view.findViewById(R.id.ditherImageView);

    }


    private void floydSteinbergDithering(){
        presenter.floydSteinbergDithering();
    }
    private void atkinsonDithering(){
        presenter.atkinsonDithering();
    }
    private void stuckiDithering(){
        presenter.stuckiDithering();
    }
    private void print(){
        presenter.print();
    }

    @Override
    public void setImage(Bitmap b) {

        imageView.setImageBitmap(b);
    }
}
