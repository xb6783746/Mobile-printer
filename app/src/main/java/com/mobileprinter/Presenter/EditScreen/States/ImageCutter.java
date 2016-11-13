package com.mobileprinter.Presenter.EditScreen.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.mobileprinter.Interfaces.ImageEditorScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Влад on 11.11.2016.
 */

public class ImageCutter extends BaseState{


    enum Side{
        Left, Right, Top, Bottom, Full, None
    }

    public ImageCutter(ImageEditorScreen screen, Owner owner) {
        super(owner);

        this.screen = screen;

        sides = new Side[]{Side.Left, Side.Right, Side.Top, Side.Bottom};
    }

    private Rect area;
    private Rect fullArea;
    private Boolean move = false;
    private Side side;
    private Point last;
    private Side[] sides;

    @Override
    public void touch(int x, int y){

        Side s = check(x, y);

        if(s != Side.None){
            last = new Point(x, y);
            side = s;
            move = true;
        }

    }
    @Override
    public void release(int x, int y){

        move = false;
    }
    @Override
    public void move(Point to) {

        if(!move || near(to, last, 0.1f)){
            return;
        }

        int deltaX = to.x - last.x;
        int deltaY = to.y - last.y;

        switch (side){
            case Top:
                if(area.top + deltaY >= 0 && area.top + deltaY < area.bottom) {
                    area.top += deltaY;
                }
                break;
            case Left:
                if(area.left + deltaX >= 0 && area.left + deltaX < area.right) {
                    area.left += deltaX;
                }
                break;
            case Right:
                if(area.right + deltaX <= fullArea.right && area.right + deltaX > area.left) {
                    area.right += deltaX;
                }
                break;
            case Bottom:
                if(area.bottom + deltaY <= fullArea.bottom && area.bottom + deltaY > area.top) {
                    area.bottom += deltaY;
                }
                break;
            case Full:
                if(area.top + deltaY < 0 || area.bottom + deltaY > fullArea.bottom) {
                    deltaY = 0;
                }
                if(area.left + deltaX < 0 || area.right + deltaX > fullArea.right) {
                    deltaX = 0;
                }
                area.offset(deltaX, deltaY);
        }

        last = to;
        drawRect();

    }

    @Override
    public void start() {

        screen.showOptionButtons();

        area = new Rect(fullArea);
        drawRect();
    }

    @Override
    public void update(Bitmap b) {
        this.current = b;

        area = new Rect(0, 0, screen.getImageWidth(),  screen.getImageHeight());
        fullArea = new Rect(0, 0, screen.getImageWidth(),  screen.getImageHeight());
    }

    @Override
    public void cancel() {

    }

    @Override
    public Bitmap apply(Bitmap b) {

        float scale = screen.getScale();

        int width = Math.round((area.right-area.left) / scale);
        int height = Math.round((area.bottom-area.top)/scale);

        int x = Math.round(area.left/scale);
        int y = Math.round(area.top/scale);

        return Bitmap.createBitmap(b, x, y, width, height);
    }

    @Override
    public void seekBarChanged(int progress) {

    }

    @Override
    public void acceptButtonClick() {

        owner.close();
    }
    @Override
    public void cancelButtonClick() {

        screen.setImage(current);
        owner.close();
    }

    @Override
    public void brightnessButtonClick() {

    }
    @Override
    public void contrastButtonClick() {

    }
    @Override
    public void saturationButtonClick() {

    }

    private Side check(int x, int y){

        Point p = new Point(x, y);
        Side res = Side.None;

        Boolean ok = false;

        for(int i = 0; i < sides.length && !ok; i++){

            ok = near(p, getSide(sides[i], area), 30f);
            if(ok){
                res = sides[i];
            }
        }

        if(res == Side.None && area.contains(x, y)){
            res = Side.Full;
        }

        return res;
    }
    @NonNull
    private Boolean near(Point p1, Point p2, float area){

        return Math.sqrt( (p1.x-p2.x)*(p1.x-p2.x) + (p1.y - p2.y)*(p1.y - p2.y)) < area;
    }
    private Point getSide(Side side, Rect rect){

        Point p = new Point();

        switch (side){
            case Top:
                p.x = rect.centerX();
                p.y = rect.top;
                break;
            case Left:
                p.x = rect.left;
                p.y = rect.centerY();
                break;
            case Right:
                p.x = rect.right;
                p.y = rect.centerY();
                break;
            case Bottom:
                p.x = rect.centerX();
                p.y = rect.bottom;
                break;
            case Full:
                p.x = rect.centerX();
                p.y = rect.centerY();
        }

        return p;
    }
    private List<Point> allSides(){
        List<Point> res = new ArrayList<>();

        for(Side side : sides){
            res.add(getSide(side, area));
        }

        return res;
    }
    private void drawRect(){
        screen.drawRect(area, allSides());
    }


}
