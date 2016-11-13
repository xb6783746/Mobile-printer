package com.mobileprinter.View.EditScreen;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ImageEditorScreen;
import com.mobileprinter.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageEditorView extends Fragment implements ImageEditorScreen{

    public ImageEditorView() {
        // Required empty public constructor
    }


    private final int OPEN_GALLERY = 376;
    private final int lineWidth = 5;
    private final int circleRad = 15;

    private SeekBar seekBar;

    private EditButtonsFragment optionButtonsFragment;
    private OptionButtonFragment acceptCancelButtonsFragment;

    private ImageView imageView;

    private Bitmap current;
    private Bitmap tmp;

    private Point topLeft;

    private Paint paint;
    private Paint circlePaint;

    private float scaleX, scaleY;
    private int currentRad;

    private ImageEditorPresenter presenter;

    public void setPresenter(ImageEditorPresenter presenter){
        this.presenter = presenter;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_editor_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        circlePaint = new Paint();

        initControls(view);

        //presenter = new ImageEditorPresenterImpl(this, this);

        seekBar.setVisibility(View.INVISIBLE);
    }

    private void initControls(View where) {

        seekBar = seekBarInit(where);

        Button openB = (Button)where.findViewById(R.id.openButton);
        openB.setOnClickListener((x) -> openGallery(x));
        Button editB = (Button)where.findViewById(R.id.editButton);
        editB.setOnClickListener((x) -> editButtonClick(x));
        Button cutB = (Button)where.findViewById(R.id.cutButton);
        cutB.setOnClickListener((x) -> cutButtonClick(x));
        Button printB = (Button)where.findViewById(R.id.printButton);
        printB.setOnClickListener((x) -> printButtonClick(x));


        optionButtonsFragment = new EditButtonsFragment();
        optionButtonsFragment.onBrightnessButtonClick((x) -> brightnessButtonClick(x));
        optionButtonsFragment.onContrastButtonClick((x) -> contrastButtonClick(x));
        optionButtonsFragment.onSaturationButtonClick((x) -> saturationButtonClick(x));

        acceptCancelButtonsFragment = new OptionButtonFragment();
        acceptCancelButtonsFragment.onAcceptButtonClick((x) -> acceptButtonClick(x));
        acceptCancelButtonsFragment.onCancelButtonClick((x) -> cancelButtonClick(x));

        imageView = imageViewInit(where);

    }

    @Override
    public void showEditButtons() {
        hideOptions();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.imageEditButtons, optionButtonsFragment)
                .commit();

    }
    @Override
    public void showOptionButtons() {

        hideOptions();

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.imageEditButtons, acceptCancelButtonsFragment)
                .commit();

    }
    @Override
    public void hideOptions() {

        getChildFragmentManager()
                .beginTransaction()
                .remove(acceptCancelButtonsFragment)
                .remove(optionButtonsFragment)
                .commit();
    }

    @Override
    public void showSeekBar(Boolean visible) {

        int v = visible ? View.VISIBLE : View.INVISIBLE;

        seekBar.setVisibility(v);
    }
    @Override
    public void setSeekBar(int progress) {
        seekBar.setProgress(progress);
    }

    @Override
    public void setImage(Bitmap b) {
        if (tmp != null) {
            tmp.recycle();
        }

        current = b;
        tmp = current.copy(Bitmap.Config.ARGB_8888, true);

        imageView.setImageBitmap(b);

        Rect rect = getBitmapPositionInsideImageView(imageView);
        topLeft = new Point(rect.left, rect.top);

        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        scaleX = f[Matrix.MSCALE_X];
        scaleY = f[Matrix.MSCALE_Y];

        paint.setStrokeWidth(Math.round(lineWidth/scaleX));
        currentRad = Math.round(circleRad/scaleX);
    }

    @Override
    public Rect getImageRect() {
        return getBitmapPositionInsideImageView(imageView);
    }

    @Override
    public void drawRect(Rect rect, List<Point> dots) {

        Canvas canvas = new Canvas(tmp);

        canvas.drawBitmap(current, new Matrix(), null);
        canvas.drawRect(toImageCoordinates(rect), paint);

        for (Point point : dots) {

            Point nP = toImageCoordinates(point);

            canvas.drawCircle(
                    nP.x,
                    nP.y,
                    currentRad,
                    circlePaint
            );
        }

        imageView.setImageBitmap(tmp);
    }
    @Override
    public void hideRect() {
        imageView.setImageBitmap(current);
    }

    @Override
    public int getImageWidth() {
        return Math.round(current.getWidth()*scaleX);
    }
    @Override
    public int getImageHeight() {
        return Math.round(current.getHeight()*scaleY);
    }

    @Override
    public float getScale() {
        return scaleX;
    }


    public void openGallery(View view) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, OPEN_GALLERY);
    }
    public void editButtonClick(View view) {

        presenter.editButtonClick();
    }
    public void cutButtonClick(View view) {
        presenter.cutButtonClick();
    }
    public void printButtonClick(View view) {
        presenter.printButtonClick();
    }
    public void acceptButtonClick(View view) {
        presenter.acceptButtonClick();
    }

    public void cancelButtonClick(View view) {
        presenter.cancelButtonClick();
    }
    public void seekBarChanged(int progress) {
        presenter.seekBarChanged(progress);
    }
    public void brightnessButtonClick(View view) {
        presenter.brightnessButtonClick();
    }
    public void contrastButtonClick(View view) {
        presenter.contrastButtonClick();
    }
    public void saturationButtonClick(View view) {
        presenter.saturationButtonClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_GALLERY) {
            presenter.loadImage(data.getData());
        }
    }

    public Rect getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null) {
            //return ret;
            return null;
        }


        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH) / 2;
        int left = (int) (imgViewW - actW) / 2;

        ret[0] = left;
        ret[1] = top;

        //return ret;

        return new Rect(left, top, left + actW, top + actH);
    }

    private Point transformPoint(Point origin) {
        return new Point(
                origin.x - topLeft.x,
                origin.y - topLeft.y
        );
    }
    private Rect transformRect(Rect rect){

        return new Rect(
                rect.left - topLeft.x,
                rect.top - topLeft.y,
                rect.right - topLeft.x,
                rect.bottom - topLeft.y
        );
    }
    private Rect toImageCoordinates(Rect rect){

        return new Rect(
                Math.round(rect.left/scaleX),
                Math.round(rect.top/scaleY),
                Math.round(rect.right/scaleX),
                Math.round(rect.bottom/scaleY)
        );
    }
    private Point toImageCoordinates(Point point){

        return new Point(
                Math.round(point.x/scaleX),
                Math.round(point.y/scaleY)
        );
    }

    private ImageView imageViewInit(View where){
        ImageView res = (ImageView)where.findViewById(R.id.imageView);


        res.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = Math.round(event.getX());
                int y = Math.round(event.getY());

                Point image = transformPoint(new Point(x, y));

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        presenter.touch(image.x, image.y);
                        break;
                    case MotionEvent.ACTION_UP:
                        presenter.release(image.x, image.y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        presenter.move(image);
                        break;

                }

                return true;
            }
        });

        return res;
    }
    private SeekBar seekBarInit(View where){
        SeekBar res = (SeekBar)where.findViewById(R.id.seekBar);

        res.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekBarChanged(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }

        );

        return res;
    }






}
