package com.mobileprinter.Presenter.EditScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;

import com.mobileprinter.Interfaces.EditScreenRouter;
import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ImageEditorScreen;
import com.mobileprinter.Interfaces.ScreenHost;
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

    @Override
    public void start() {

    }

    enum State{
        Editor, Cutter, None
    }

    public ImageEditorPresenterImpl(ImageEditorScreen screen, Context context, EditScreenRouter router) {
        this.screen = screen;
        this.context = context;
        this.router = router;

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
    private EditScreenRouter router;

    @Override
    public void loadImage(Bitmap b) {

        current = b;

        //update(b);
    }

    @Override
    public void loadImage(Uri uri) {

        Bitmap b = null;

        try {

            b = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            update(b);

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
        //reset();

        router.openDitheringScreen(current);
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

    @Override
    public void create() {

        if(current == null){
            return;
        }

        update(current);
    }

    private void reset(){

        screen.hideOptions();
        screen.showSeekBar(false);

        screen.setImage(current);

        currentState = null;
        currentStateType = State.None;
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
    private void update(Bitmap b){

        if(current != null && current != b){
            current.recycle();
        }
        current = b;

        reset();

        screen.setImage(b);

        for(BaseState state : states.values()){
            state.update(current);
        }
    }

}
