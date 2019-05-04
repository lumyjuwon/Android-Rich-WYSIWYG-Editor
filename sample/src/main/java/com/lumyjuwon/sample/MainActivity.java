package com.lumyjuwon.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lumyjuwon.richwysiwygeditor.RichWysiwyg;

public class MainActivity extends AppCompatActivity {

    private RichWysiwyg wysiwyg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
