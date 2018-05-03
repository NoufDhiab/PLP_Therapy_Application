package com.project.bt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Created by Rosa on 23-Apr-18.
 */

public class AboutUs extends Activity {


    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView text1 = (TextView) findViewById(R.id.textView5);
        text1.setMovementMethod(LinkMovementMethod.getInstance());


        Typeface myfont= Typeface.createFromAsset(getAssets(),"fonts/maven_pro.otf");
        text1.setTypeface(myfont);
        TextView text2 = (TextView) findViewById(R.id.textView1);
        text2.setTypeface(myfont);
        TextView text3 = (TextView) findViewById(R.id.textView2);
        text3.setTypeface(myfont);
        TextView text4 = (TextView) findViewById(R.id.textView3);
        text4.setTypeface(myfont);
        TextView text5 = (TextView) findViewById(R.id.textView4);
        text5.setTypeface(myfont);
    }

    @Override
    public void onBackPressed() {



        /*Intent intent = new Intent();
        intent.setClass(this, UnityPlayerActivity.class);
        startActivity(intent);
        *///super.onBackPressed();
    }
}
