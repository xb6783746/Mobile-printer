package com.mobileprinter.View.EditScreen;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mobileprinter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionButtonFragment extends Fragment implements View.OnClickListener{



    public OptionButtonFragment() {
        // Required empty public constructor
    }

    private Button acceptButton;
    private Button cancelButton;

    private View.OnClickListener acceptButtonListener;
    private View.OnClickListener cancelButtonListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option_button, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        acceptButton = (Button)(view.findViewById(R.id.acceptButton));
        cancelButton = (Button)(view.findViewById(R.id.cancelButton));

        acceptButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        int i = 5;
    }

    public void onAcceptButtonClick(View.OnClickListener c){

        acceptButtonListener = c;
    }
    public void onCancelButtonClick(View.OnClickListener c){

        cancelButtonListener = c;
    }

    @Override
    public void onClick(View v) {

        View.OnClickListener c = null;

        switch(v.getId()){
            case R.id.acceptButton:
                c = acceptButtonListener;
                break;
            case R.id.cancelButton:
                c = cancelButtonListener;
                break;
            default:
                break;
        }

        if(c != null){
            c.onClick(v);
        }

    }
}
