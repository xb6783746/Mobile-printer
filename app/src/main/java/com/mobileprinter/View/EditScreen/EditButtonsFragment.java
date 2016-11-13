package com.mobileprinter.View.EditScreen;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobileprinter.R;

import java.util.function.Consumer;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditButtonsFragment extends Fragment implements View.OnClickListener{


    public EditButtonsFragment() {
        // Required empty public constructor
    }


    private Button brightnessButton;
    private Button contrastButton;
    private Button saturationButton;

    private View.OnClickListener brightnessButtonListener;
    private View.OnClickListener contrastButtonListener;
    private View.OnClickListener saturationButtonListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_buttons, container, false);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        brightnessButton = (Button)(view.findViewById(R.id.brightnessButton));
        contrastButton = (Button)(view.findViewById(R.id.contrastButton));
        saturationButton = (Button)(view.findViewById(R.id.saturationButton));

        brightnessButton.setOnClickListener(this);
        contrastButton.setOnClickListener(this);
        saturationButton.setOnClickListener(this);
    }


    public void onBrightnessButtonClick(View.OnClickListener c){

        brightnessButtonListener = c;
    }
    public void onContrastButtonClick(View.OnClickListener c){

        contrastButtonListener = c;
    }
    public void onSaturationButtonClick(View.OnClickListener c){

        saturationButtonListener = c;
    }


    @Override
    public void onClick(View v) {

        View.OnClickListener c = null;

        switch(v.getId()){
            case R.id.brightnessButton:
                c = brightnessButtonListener;
                break;
            case R.id.contrastButton:
                c = contrastButtonListener;
                break;
            case R.id.saturationButton:
                c = saturationButtonListener;
                break;
            default:
                break;
        }

        if(c != null){
            c.onClick(v);
        }

    }
}
