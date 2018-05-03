package com.project.bt;

import com.unity3d.player.*;
import com.vuforia.Rectangle;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;


public class UnityPlayerActivity extends Activity
{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

   Bitmap mbitmap;
   ImageView imageView;
    Button aboutUs;
    Button instruction;
    Button notification;
    Button captureBtn;

     private LinearLayout rootContent;
    private Button fullPageScreenshot, customPageScreenshot;
   private TextView hiddenText;

    public void screenShot(View view) {
        mbitmap = getBitmapOFRootView(aboutUs);
        imageView.setImageBitmap(mbitmap);
        createImage(mbitmap);
    }

    public Bitmap getBitmapOFRootView(View v) {
        View rootview = v.getRootView();
        rootview.setDrawingCacheEnabled(true);
        Bitmap bitmap1 = rootview.getDrawingCache();
        return bitmap1;
    }

    public void createImage(Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        File file = new File(Environment.getExternalStorageDirectory() +
                "/capturedscreenandroid.jpg");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(bytes.toByteArray());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void takeScreenshot() {
        Bitmap b = null;


                //If Screenshot type is CUSTOM

                //fullPageScreenshot.setVisibility(View.INVISIBLE);//set the visibility to INVISIBLE of first button
                instruction.setVisibility(View.INVISIBLE);
                notification.setVisibility(View.INVISIBLE);//set the visibility to VISIBLE of hidden text
                aboutUs.setVisibility(View.INVISIBLE);//set the visibility to VISIBLE of hidden text

                b = ScreenshotUtils.getScreenShot(this.mUnityPlayer.getView());

                //After taking screenshot reset the button and view again
                instruction.setVisibility(View.VISIBLE);
                notification.setVisibility(View.VISIBLE);//set the visibility to VISIBLE of hidden text
                aboutUs.setVisibility(View.VISIBLE);
                //NOTE:  You need to use visibility INVISIBLE instead of GONE to remove the view from frame else it wont consider the view in frame and you will not get screenshot as you required.


        //If bitmap is not null
      if (b != null) {
            showScreenShotImage(b);//show bitmap over imageview

            File saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            File file = ScreenshotUtils.store(b, "screenshot" + ".jpg", saveFile);//save the screenshot to selected path
            shareScreenshot(file);//finally share screenshot
        } else
            //If bitmap is null show toast message
            Toast.makeText(this, R.string.action_settings, Toast.LENGTH_SHORT).show();

    }

    private void shareScreenshot(File file) {
        Uri uri = Uri.fromFile(file);//Convert file path into Uri for sharing
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_STREAM, uri);//pass uri here
        startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
    }

    private void showScreenShotImage(Bitmap b) {
        imageView.setImageBitmap(b);
    }

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayer(this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();
    }

    void buttonSet(){

        //------ notification button ------

        notification = new Button(this);
        notification.setText("Notification");
        notification.setTextSize(7);
        notification.setTextColor(Color.WHITE);
        notification.setX(0);
        notification.setY(20);
        notification.setBackgroundColor(Color.TRANSPARENT);
        mUnityPlayer.addView(notification,170,120);
        Drawable notificationDrawable =this.getResources(). getDrawable( R.drawable.ic_notifications);
        notification.setCompoundDrawablesWithIntrinsicBounds( null, notificationDrawable, null, null );

        notification.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intentNot = new Intent(getApplicationContext(), Notification_reciever.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intentNot, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long startTime = 6 * 360 * 1000;
                alarmManager.setInexactRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() +
                        startTime, 360 * 1000, pendingIntent);
            }
        });


        //------ instruction button ------


        instruction = new Button(this);
        instruction.setText("Instruction");
        instruction.setTextSize(7);
        instruction.setTextColor(Color.WHITE);
        instruction.setX(0);
        instruction.setY(120);
        instruction.setBackgroundColor(Color.TRANSPARENT);
        mUnityPlayer.addView(instruction,170,120);
        Drawable instructionDrawable =this.getResources(). getDrawable( R.drawable.ic_instruction);
        instruction.setCompoundDrawablesWithIntrinsicBounds( null, instructionDrawable, null, null );

        instruction.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                setContentView(R.layout.activity_instruction);
            }
        });


        //------ about us button ------


        aboutUs = new Button(this);
        aboutUs.setText("About Us");
        aboutUs.setTextSize(7);
        aboutUs.setTextColor(Color.WHITE);
        aboutUs.setX(0);
        aboutUs.setY(220);
        aboutUs.setBackgroundColor(Color.TRANSPARENT);
        mUnityPlayer.addView(aboutUs,170,120);
        Drawable aboutUsDrawable =this.getResources(). getDrawable( R.drawable.ic_aboutus);
        aboutUs.setCompoundDrawablesWithIntrinsicBounds( null, aboutUsDrawable, null, null );

        aboutUs.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                //screenShot(mUnityPlayer.getView());
                //takeScreenshot();
                setContentView(R.layout.activity_about_us);
            }
        });


        //------ capture screen button ------

     /*  captureBtn = new Button(this);
        captureBtn.setX(0);
        captureBtn.setY(120);
        captureBtn.setBackgroundColor(Color.TRANSPARENT);
        mUnityPlayer.addView(captureBtn,170,120);
        Drawable captureBtnDrawable =this.getResources(). getDrawable( R.drawable.cam_capture_btn);
        captureBtn.setCompoundDrawablesWithIntrinsicBounds( null, captureBtnDrawable, null, null );


        captureBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                //screenShot(mUnityPlayer.getView());
                //takeScreenshot();
                setContentView(R.layout.activity_about_us);
            }
        });*/

    }


    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mUnityPlayer.quit();

    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.quit();
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
        buttonSet();
    }

    @Override protected void onStop()
    {
        super.onStop();
        mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
