package com.mobileprinter.Presenter.EditScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ImageEditorScreen;
import com.mobileprinter.Presenter.EditScreen.States.BaseState;
import com.mobileprinter.Presenter.EditScreen.States.ImageCutter;
import com.mobileprinter.Presenter.EditScreen.States.ImageEditor;
import com.mobileprinter.Presenter.EditScreen.States.Owner;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Влад on 08.11.2016.
 */

public class ImageEditorPresenterImpl implements ImageEditorPresenter, Owner {

    enum State{
        Editor, Cutter, None
    }
    public ImageEditorPresenterImpl(ImageEditorScreen screen, Context context) {
        this.screen = screen;
        this.context = context;

        states = new HashMap<>();

        ImageCutter cutter = new ImageCutter(screen, this);
        ImageEditor editor = new ImageEditor(screen, this);

        states.put(
                State.Cutter,
                cutter
        );
        states.put(
                State.Editor,
                editor
        );
    }

    private ImageEditorScreen screen;
    private Context context;
    private HashMap<State, BaseState> states;
    private BaseState currentState;
    private State currentStateType;
    private Bitmap current;

    @Override
    public void loadImage(Uri uri) {

        Bitmap b = null;

        try {
            b = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            current = b;

            reset();

            screen.setImage(b);


            for(BaseState state : states.values()){
                state.update(current);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editButtonClick() {
        if(currentStateType == State.Editor){
            return;
        }

        reset();

        switchState(State.Editor);
    }
    @Override
    public void cutButtonClick() {
        if(currentStateType == State.Cutter){
            return;
        }

        reset();

        switchState(State.Cutter);
    }
    @Override
    public void printButtonClick() {
        reset();
    }


    @Override
    public void acceptButtonClick() {
        if(currentState != null) {

            Bitmap newB = currentState.apply(current);

            current.recycle();

            current = newB;

            screen.setImage(newB);

            for(BaseState state : states.values()){
                state.update(current);
            }

            currentState.acceptButtonClick();
        }
    }
    @Override
    public void cancelButtonClick() {
        if(currentState != null) {
            currentState.cancelButtonClick();
        }
    }
    @Override
    public void brightnessButtonClick() {
        if(currentState != null) {
            currentState.brightnessButtonClick();
        }
    }
    @Override
    public void contrastButtonClick() {
        if(currentState != null) {
            currentState.contrastButtonClick();
        }
    }
    @Override
    public void saturationButtonClick() {
        if(currentState != null) {
            currentState.saturationButtonClick();
        }
    }
    @Override
    public void seekBarChanged(int progress) {
        if(currentState != null) {
            currentState.seekBarChanged(progress);
        }
    }
    @Override
    public void touch(int x, int y) {
        if(currentState != null) {
            currentState.touch(x, y);
        }
    }
    @Override
    public void release(int x, int y) {
        if(currentState != null) {
            currentState.release(x, y);
        }
    }
    @Override
    public void move(Point to) {
        if(currentState != null) {
            currentState.move(to);
        }
    }

    private void reset(){

        screen.hideOptions();
        screen.showSeekBar(false);

        screen.setImage(current);

        currentState = null;
        currentStateType = State.None;
    }

    private Point transform(Point screenPoint){

        return new Point();
    }
    @Override
    public void close() {
        reset();
    }

    private void switchState(State state){
        currentStateType = state;
        currentState = states.get(state);

        currentState.start();
    }

}
