package com.example.vil.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;
import org.jetbrains.annotations.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by vil on 2017-05-24.
 */

public class MyCanvas extends View {
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;
    Boolean checked = true;
    float[] matrixarray = {
            2f, 0f, 0f, 0f, -25f,
            0f, 2f, 0f, 0f, -25f,
            0f, 0f, 2f, 0f, -25f,
            0f, 0f, 0f, 1f, 0f,
    };

    int oldX = -1; int oldY = -1;

    public MyCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
    }

    public MyCanvas(Context context) {
        super(context);

        this.mPaint = new Paint();
        this.mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.YELLOW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    public void drawStamp(int x, int y){
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

       mCanvas.drawBitmap(img, x, y, mPaint);



    }

    public void erase(){
        mBitmap.eraseColor(Color.WHITE);
        invalidate();
    }

    public void save(String dir){
//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.DD hh:mm:ss");
//        String str = sdf.format(date);
        try {
            FileOutputStream fos = new FileOutputStream(new File(dir,"canvas.jpg"));
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Toast.makeText(getContext(), "저장 성공", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "저장 실패", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String dir){
        try {
            erase();
            Bitmap bm = BitmapFactory.decodeFile(dir+"canvas.jpg");
            bm = Bitmap.createScaledBitmap(bm, bm.getWidth()/2, bm.getHeight()/2, false);
            mCanvas.drawBitmap(bm, mCanvas.getWidth()/2-bm.getWidth()/2, mCanvas.getHeight()/2 - bm.getHeight()/2 , mPaint);
            invalidate();
        } catch (Exception e){
            Toast.makeText(getContext(), "파일이 없습니다", Toast.LENGTH_SHORT).show();
        }


    }

    public void rotate(){
        mCanvas.rotate(30, this.getWidth()/2, this.getHeight()/2);
    }

    public void move(){
        mCanvas.translate(10, 10);
    }

    public void scale(){
        mCanvas.scale(1.5f, 1.5f);
    }

    public void skew(){
        mCanvas.skew(0.2f, 0.0f);
    }

    public void blur(Boolean checked){
        if(checked){
            BlurMaskFilter blur = new BlurMaskFilter(100, BlurMaskFilter.Blur.INNER);
            mPaint.setMaskFilter(blur);
            invalidate();
        }else{
            mPaint.setMaskFilter(null);
        }
    }

    public void color(Boolean checked){
        if(checked){
            ColorMatrix matrix = new ColorMatrix(matrixarray);
            mPaint.setColorFilter(new ColorMatrixColorFilter(matrix));

        }else{
            mPaint.setColorFilter(null);
        }
    }

    public void bigPen(Boolean checked){
        if(checked){
            mPaint.setStrokeWidth(5);
        }else{
            mPaint.setStrokeWidth(3);
        }
    }

    public void setRed(){
        mPaint.setColor(Color.RED);
    }

    public void setBlue(){
        mPaint.setColor(Color.BLUE);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int)event.getX();
        int Y = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!checked){
                    drawStamp(X, Y);
                    invalidate();
                }
                oldX = X; oldY = Y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(oldX != -1){
                    if(checked){
                        mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                        invalidate();
                    }
                }
                oldX = X; oldY = Y;
                break;
            case MotionEvent.ACTION_UP:
                if(oldX != -1){
                    if(checked){
                        mCanvas.drawLine(oldX, oldY, X, Y, mPaint);
                        invalidate();
                    }
                }
                oldX = -1;
                oldY = -1;
                break;
        }



        return true;
    }
}
