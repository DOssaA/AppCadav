package com.example.usuario.myapplication;

<<<<<<< HEAD
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;
=======
>>>>>>> 744a79b70425b8304ee4a20678f7b41230138446
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by usuario on 11/03/2015.
 */
public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF660000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;
    //Brush Size
    private float brushSize, lastBrushSize;

    private Bitmap backgroundBitmap = null;

    private boolean erase=false;
    private LienzoActivity lienzoActivity;

    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public void setBackgroundBitmap(Bitmap backgroundBitmap) {
        this.backgroundBitmap = backgroundBitmap;
    }

    private void setupDrawing(){

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;
//get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        //Paint properties
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
<<<<<<< HEAD

=======
        paths.add(drawPath);
>>>>>>> 744a79b70425b8304ee4a20678f7b41230138446
    }
    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    //Called when the Custom View is assigned a size
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    /*
    Se activa cuando el usuario oprime la pantalla para dibujar
     */
    @Override
    protected void onDraw(Canvas canvas) {
<<<<<<< HEAD
        if(getBackgroundBitmap() != null){
            Bitmap bmp= getBackgroundBitmap();
            bmp = cutBottom(bmp);
            canvas.drawBitmap(bmp, 0,0, canvasPaint);
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            // canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);

        }else{
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        }

    }

    /*
    Corta la parte final de un Bitmap
     */
    private Bitmap cutBottom(Bitmap origialBitmap) {
        Bitmap cutBitmap = Bitmap.createBitmap(origialBitmap.getWidth(),
                origialBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(cutBitmap);
        Rect srcRect = new Rect(0, 6*(origialBitmap.getHeight() / 7), origialBitmap.getWidth() ,
                origialBitmap.getHeight());
        Rect desRect = new Rect(0, 0, origialBitmap.getWidth(), origialBitmap.getHeight() / 7);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return cutBitmap;
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    protected void onDrawBitmap(Canvas canvas,Bitmap bmp){
        canvas.drawBitmap(bmp,0,0,canvasPaint);
=======
        for (Path p : paths){
            canvas.drawPath(p, drawPaint);
        }
        //canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
>>>>>>> 744a79b70425b8304ee4a20678f7b41230138446
        canvas.drawPath(drawPath, drawPaint);
    }

    public void setColor(String newColor){
//set color
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();
        if(erase) drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else drawPaint.setXfermode(null);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                undonePaths.clear();
                drawPath.reset();
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
//                drawPath.reset();
//                lienzoActivity.actualizarPila();
                paths.add(drawPath);
                drawPath = new Path();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setBrushSize(float newSize){
//update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setErase(boolean isErase){
//set erase true or false
        erase=isErase;
    }


    public void setPadre(LienzoActivity lienzoActivity) {
        this.lienzoActivity = lienzoActivity;
    }

    public void setFondoAnterior(Bitmap bitmap) {
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        drawCanvas.drawBitmap(bitmap, 0, 0, drawPaint);
        postInvalidate();
    }

    public void onClickUndo () {
        if (paths.size()>0)
        {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
        else
        {

        }
        //toast the user
    }


}
