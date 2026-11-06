package com.example.iphonenaturelock;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.*;
import android.view.*;
import android.widget.Toast;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraCharacteristics;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    private boolean torchOn = false;
    private CameraManager cameraManager;
    private String cameraId;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        cameraManager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                Boolean flash = cameraManager.getCameraCharacteristics(id)
                    .get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                if (Boolean.TRUE.equals(flash)) { cameraId = id; break; }
            }
        } catch (Exception ignored) {}
        setContentView(new LockView(this));
    }

    private void toggleTorch() {
        if (cameraId == null) {
            Toast.makeText(this, "Flashlight unavailable", Toast.LENGTH_SHORT).show(); return;
        }
        try { torchOn = !torchOn; cameraManager.setTorchMode(cameraId, torchOn); }
        catch (Exception e) { torchOn = false; Toast.makeText(this, "Flashlight could not start", Toast.LENGTH_SHORT).show(); }
    }

    private void openCamera() {
        try { startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)); }
        catch (Exception e) { Toast.makeText(this, "Camera unavailable", Toast.LENGTH_SHORT).show(); }
    }

    class LockView extends View {
        Paint p = new Paint(3);
        float downX, downY;
        long lastTick = 0;
        LockView(Context c) { super(c); setLayerType(View.LAYER_TYPE_SOFTWARE, null); }

        void text(Canvas c, String s, float x, float y, float size, int color, boolean bold) {
            p.setShader(null); p.setColor(color); p.setTextSize(size);
            p.setTypeface(bold ? Typeface.DEFAULT_BOLD : Typeface.create("sans-serif-light", Typeface.NORMAL));
            p.setTextAlign(Paint.Align.CENTER); c.drawText(s, x, y, p);
        }

        @Override protected void onDraw(Canvas c) {
            super.onDraw(c);
            float w=getWidth(), h=getHeight();
            p.setShader(new LinearGradient(0,0,0,h,
                new int[]{Color.rgb(18,42,62),Color.rgb(72,112,119),Color.rgb(205,170,135),Color.rgb(28,57,54)},
                new float[]{0f,.42f,.72f,1f},Shader.TileMode.CLAMP));
            c.drawRect(0,0,w,h,p); p.setShader(null);
            p.setColor(Color.argb(55,255,225,176)); c.drawCircle(w*.72f,h*.48f,w*.22f,p);
            p.setColor(Color.argb(190,255,225,184)); c.drawCircle(w*.72f,h*.48f,w*.075f,p);

            Path m=new Path(); m.moveTo(0,h*.62f); m.lineTo(w*.19f,h*.43f); m.lineTo(w*.34f,h*.56f);
            m.lineTo(w*.57f,h*.35f); m.lineTo(w*.78f,h*.58f); m.lineTo(w,h*.42f); m.lineTo(w,h); m.lineTo(0,h); m.close();
            p.setColor(Color.rgb(66,91,83)); c.drawPath(m,p);
            Path m2=new Path(); m2.moveTo(0,h*.72f); m2.lineTo(w*.24f,h*.55f); m2.lineTo(w*.43f,h*.69f);
            m2.lineTo(w*.69f,h*.51f); m2.lineTo(w,h*.68f); m2.lineTo(w,h); m2.lineTo(0,h); m2.close();
            p.setColor(Color.rgb(35,69,59)); c.drawPath(m2,p);
            Path hill=new Path(); hill.moveTo(0,h*.79f); hill.cubicTo(w*.3f,h*.67f,w*.55f,h*.91f,w,h*.73f);
            hill.lineTo(w,h); hill.lineTo(0,h); hill.close(); p.setColor(Color.rgb(18,48,40)); c.drawPath(hill,p);

            p.setColor(Color.rgb(12,35,30));
            for(int i=0;i<9;i++){ float x=w*(i/8f), y=h*(.79f+.025f*(i%3));
                c.drawRect(x-2,y-25,x+2,y+12,p);
                Path t=new Path(); t.moveTo(x,y-75); t.lineTo(x-22,y-24); t.lineTo(x+22,y-24); t.close(); c.drawPath(t,p);
                t=new Path(); t.moveTo(x,y-55); t.lineTo(x-18,y-12); t.lineTo(x+18,y-12); t.close(); c.drawPath(t,p);
            }
            int white=Color.WHITE;
            text(c,new SimpleDateFormat("EEEE, d MMMM",Locale.getDefault()).format(new Date()),w/2,h*.105f,18,white,false);
            text(c,new SimpleDateFormat("HH:mm",Locale.getDefault()).format(new Date()),w/2,h*.245f,Math.min(w*.25f,112),white,false);
            text(c,"Nature is calling",w/2,h*.30f,17,Color.argb(230,255,255,255),false);
            float cy=h*.91f, r=27; p.setColor(Color.argb(85,0,0,0));
            c.drawCircle(w*.19f,cy,r,p); c.drawCircle(w*.81f,cy,r,p);
            text(c,torchOn?"ON":"FLASH",w*.19f,cy+5,11,white,true);
            text(c,"CAM",w*.81f,cy+5,12,white,true);
            text(c,"Tap shortcuts • Swipe up to exit",w/2,h*.975f,12,Color.argb(220,255,255,255),false);
            if(System.currentTimeMillis()-lastTick>1000){lastTick=System.currentTimeMillis();postInvalidateDelayed(1000);}
        }

        @Override public boolean onTouchEvent(MotionEvent e) {
            if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}
            if(e.getAction()==MotionEvent.ACTION_UP){
                float w=getWidth(),h=getHeight(),x=e.getX(),y=e.getY();
                if(downY>h*.86f && y>h*.86f){if(x<w*.48f)toggleTorch();else openCamera();invalidate();}
                else if(downY-y>100)finish();
                return true;
            }
            return true;
        }
    }
}
