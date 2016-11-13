package com.mobileprinter.View.EditScreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.mobileprinter.Presenter.EditScreen.ImageEditorPresenterImpl;
import com.mobileprinter.Interfaces.ImageEditorPresenter;
import com.mobileprinter.Interfaces.ImageEditorScreen;
import com.mobileprinter.R;

import java.io.DataOutput;
import java.io.OutputStream;
import java.math.MathContext;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

public class ImageEditorActivity extends Activity implements ImageEditorScreen {


    private final int OPEN_GALLERY = 376;
    private final int lineWidth = 5;
    private final int circleRad = 15;

    private LinearLayout mainButtons;
    private FrameLayout optionLayout;

    private SeekBar seekBar;

    private EditButtonsFragment optionButtonsFragment;
    private OptionButtonFragment acceptCancelButtonsFragment;

    private ImageView imageView;

    private Bitmap current;
    private Bitmap tmp;
    private Point topLeft;
    private Paint paint;
    private Paint circlePaint;

    private int width, height;
    private float scaleX, scaleY;
    private int currentRad;

    private ImageEditorPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);

        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        circlePaint = new Paint();

        initControls();

        presenter = new ImageEditorPresenterImpl(this, this);

        //editButtons.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
    }

    private void initControls() {

        mainButtons = (LinearLayout) findViewById(R.id.mainButtons);
        optionLayout = (FrameLayout) findViewById(R.id.imageEditButtons);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(
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

        optionButtonsFragment = new EditButtonsFragment();

        acceptCancelButtonsFragment = new OptionButtonFragment();

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnTouchListener(new View.OnTouchListener() {
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

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        width = imageView.getWidth();
        height = imageView.getHeight();
    }

    @Override
    public void showEditButtons() {
        //optionLayout.removeAllViews();

        hideOptions();

        getFragmentManager().beginTransaction().add(R.id.imageEditButtons, optionButtonsFragment).commit();

        optionButtonsFragment.onBrightnessButtonClick((x) -> brightnessButtonClick(x));
        optionButtonsFragment.onContrastButtonClick((x) -> contrastButtonClick(x));
        optionButtonsFragment.onSaturationButtonClick((x) -> saturationButtonClick(x));
    }

    @Override
    public void showOptionButtons() {

        //optionLayout.removeAllViews();
        hideOptions();

        getFragmentManager().beginTransaction().add(R.id.imageEditButtons, acceptCancelButtonsFragment).commit();

        acceptCancelButtonsFragment.onAcceptButtonClick((x) -> acceptButtonClick(x));
        acceptCancelButtonsFragment.onCancelButtonClick((x) -> cancelButtonClick(x));
    }

    @Override
    public void hideOptions() {
        //optionLayout.removeAllViews();
        getFragmentManager().beginTransaction().remove(acceptCancelButtonsFragment).remove(optionButtonsFragment).commit();
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
        //presenter.openImageGallery();

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(pickIntent, OPEN_GALLERY);
    }
    public void editButtonClick(View view) {

        //editButtons.setVisibility(View.VISIBLE);
        //View tmp = infla

        //Fragment fragment = new EditButtonsFragment();

        //getFragmentManager().beginTransaction().add(R.id.imageEditButtons, fragment, "123").commit();

        //showAcceptButton();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_GALLERY) {
            presenter.loadImage(data.getData());
        }
    }
//
//    public int[] getBitmapOffset(Boolean includeLayout) {
//        int[] offset = new int[2];
//        float[] values = new float[9];
//
//        Matrix m = imageView.getImageMatrix();
//        m.getValues(values);
//
//        offset[0] = (int) values[5];
//        offset[1] = (int) values[2];
//
//        if (includeLayout) {
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
//
//            int paddingTop = (int) (imageView.getPaddingTop());
//            int paddingLeft = (int) (imageView.getPaddingLeft());
//
//            offset[0] += paddingTop + lp.topMargin;
//            offset[1] += paddingLeft + lp.leftMargin;
//        }
//
//        return offset;
//    }

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

}
