package com.lumyjuwon.richwysiwygeditor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.os.Environment;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.lumyjuwon.richwysiwygeditor.RichEditor.RichEditor;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.Keyboard;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.TextColor;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.Youtube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class RichWysiwyg extends LinearLayout {

    private EditText headline;
    private RichEditor content;
    private View popupView;
    private PopupWindow mPopupWindow;
    private Button cancelButton;
    private Button confirmButton;
    private ImageButton insertImageButton;
    private WriteCustomButton textColorButton;
    private WriteCustomButton textBgColorButton;
    private WriteCustomButton textBoldButton;
    private WriteCustomButton textItalicButton;
    private WriteCustomButton textUnderlineButton;
    private WriteCustomButton textStrikeButton;
    private WriteCustomButton textAlignButton;
    private ArrayList<WriteCustomButton> buttonArrayList;
    private ArrayList<Image> images = new ArrayList<>();
    private ImagePicker imagePicker;

    public RichWysiwyg(Context context) {
        super(context);
        init();
    }

    public RichWysiwyg(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichWysiwyg(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    class PopupButtonListener implements OnClickListener{
        @Override
        public void onClick(View view){
            if(view instanceof WriteCustomButton){
                WriteCustomButton button = (WriteCustomButton) view;

                if(button.getCheckedState()) {
                    clearPopupWindow();
                    button.switchCheckedState();
                }
                else {
                    clearPopupWindow();
                    content.clearFocusEditor();
                    if(view.getId() == R.id.write_textColor)
                        showColorPopupWindow(view);
                    else if(view.getId() == R.id.write_textBgColor)
                        showBgColorPopupWindow(view);
                    else if(view.getId() == R.id.write_textAlign)
                        showAlignPopupWindow(view);
                    clearPopupButton();
                    button.switchCheckedState();
                }
            }
        }
    }

    class DecorationButtonListener implements OnClickListener{
        @Override
        public void onClick(View view){
            if(view instanceof WriteCustomButton) {
                WriteCustomButton button = (WriteCustomButton) view;

                clearPopupWindow();
                clearPopupButton();
                content.clearAndFocusEditor();
                if(view.getId() == R.id.write_textBold)
                    content.setBold();
                else if(view.getId() == R.id.write_textItalic)
                    content.setItalic();
                else if(view.getId() == R.id.write_textUnderLine)
                    content.setUnderline();
                else if(view.getId() == R.id.write_textStrike)
                    content.setStrikeThrough();
                if(button.getCheckedState()) {
                    button.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.black));
                    button.switchCheckedState();
                }
                else {
                    button.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                    button.switchCheckedState();
                }

            }
        }
    }


    private void init(){
        inflate(getContext(), R.layout.activity_write, this);

        // Html WebView
        headline = findViewById(R.id.write_headline);
        content = findViewById(R.id.write_content);
        content.setLayerType(View.LAYER_TYPE_HARDWARE, null); // sdk 19 이상은 ChromeWebView를 사용해서 ChromeWebView로 설정

        // 커서 및 입력시 TEXT 상태 알려줌
        content.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                ArrayList<WriteCustomButton> button_objects = new ArrayList<>(Arrays.asList(textColorButton, textBgColorButton, textBoldButton, textItalicButton, textUnderlineButton, textStrikeButton));
                for(RichEditor.Type type : types){
                    if(type.name().contains("FONT_COLOR")){
                        textColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), TextColor.getColor(type.name())));
                        if(textColorButton.getCheckedState())
                            textColorButton.switchCheckedState();
                        button_objects.remove(textColorButton);
                    }
                    else if(type.name().contains("BACKGROUND_COLOR")){
                        textBgColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), TextColor.getColor(type.name())));
                        if(textBgColorButton.getCheckedState())
                            textBgColorButton.switchCheckedState();
                        button_objects.remove(textBgColorButton);
                    }
                    else{
                        switch(type){
                            case BOLD:
                                textBoldButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textBoldButton.getCheckedState())
                                    textBoldButton.switchCheckedState();
                                button_objects.remove(textBoldButton);
                                break;
                            case ITALIC:
                                textItalicButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textItalicButton.getCheckedState())
                                    textItalicButton.switchCheckedState();
                                button_objects.remove(textItalicButton);
                                break;
                            case UNDERLINE:
                                textUnderlineButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textUnderlineButton.getCheckedState())
                                    textUnderlineButton.switchCheckedState();
                                button_objects.remove(textUnderlineButton);
                                break;
                            case STRIKETHROUGH:
                                textStrikeButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textStrikeButton.getCheckedState())
                                    textStrikeButton.switchCheckedState();
                                button_objects.remove(textStrikeButton);
                                break;
                            default:
                        }
                    }
                }
                for(WriteCustomButton button : button_objects){
                    button.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.black));
                    button.setCheckedState(false);
                }
            }
        });

        // 취소 버튼
        cancelButton = findViewById(R.id.write_cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPopupWindow();
            }
        });

        // 등록 버튼
        confirmButton = findViewById(R.id.write_confirmButton);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearPopupWindow();
                // 백엔드
            }
        });

        // Text Size 버튼
        ImageButton textSizeButton = findViewById(R.id.write_textSize);
        textSizeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                clearPopupWindow();
            }
        });

        PopupButtonListener popupButtonListener = new PopupButtonListener();
        DecorationButtonListener decorationButtonListener = new DecorationButtonListener();

        // Text Color 버튼
        textColorButton = findViewById(R.id.write_textColor);
        textColorButton.setOnClickListener(popupButtonListener);

        // Text Bg Color 버튼
        textBgColorButton = findViewById(R.id.write_textBgColor);
        textBgColorButton.setOnClickListener(popupButtonListener);

        // Align 버튼
        textAlignButton = findViewById(R.id.write_textAlign);
        textAlignButton.setOnClickListener(popupButtonListener);

        // Bold 버튼
        textBoldButton = findViewById(R.id.write_textBold);
        textBoldButton.setOnClickListener(decorationButtonListener);

        // Italic 버튼
        textItalicButton = findViewById(R.id.write_textItalic);
        textItalicButton.setOnClickListener(decorationButtonListener);

        // Underline 버튼
        textUnderlineButton = findViewById(R.id.write_textUnderLine);
        textUnderlineButton.setOnClickListener(decorationButtonListener);

        // Strike Through 버튼
        textStrikeButton = findViewById(R.id.write_textStrike);
        textStrikeButton.setOnClickListener(decorationButtonListener);

        // 버튼 리스트
        buttonArrayList = new ArrayList<>(Arrays.asList(textColorButton, textBgColorButton, textBoldButton, textItalicButton, textUnderlineButton, textStrikeButton));

        // Image Insert 버튼
        insertImageButton = findViewById(R.id.write_imageInsert);
        insertImageButton.setOnClickListener(new OnClickListener(){
            @Override public void onClick(View v) {
                start();
            }
        });

        // embed youtube link를 클릭했을 경우 youtube app으로 실행
        content.setYoutubeLoadLinkListener(new RichEditor.YoutubeLoadLinkListener() {
            @Override
            public void onReceivedEvent(String mVideoId) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + mVideoId));
                getContext().startActivity(webIntent);
            }
        });

        // Video Insert 버튼
        ImageButton videoInsertButton = findViewById(R.id.write_videoInsert);
        videoInsertButton.setOnClickListener(new OnClickListener(){
            @Override public void onClick(View v) {
                showYoutubeDialog();
            }
        });

    }

    private ImagePicker getImagePicker() {
        imagePicker = ImagePicker.create((Activity) getContext());

        return imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()); // can be full path
    }

    private void start() {
        getImagePicker().start(); // start image picker activity with request code
    }

    // 글 사이즈 조절 설정 Window
    private void showSizePopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_text_size, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        ImageButton textAlignLeftButton = popupView.findViewById(R.id.text_alignLeft);
        textAlignLeftButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignLeft();
            }
        });

        ImageButton textAlignCenterButton = popupView.findViewById(R.id.text_alignCenter);
        textAlignCenterButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignCenter();
            }
        });

        ImageButton textAlignRightButton = popupView.findViewById(R.id.text_alignRight);
        textAlignRightButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignRight();
            }
        });
    }

    // 글 색상 설정 Window
    private void showColorPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_text_color, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        Map<Integer,Integer> colorMap = new HashMap<Integer, Integer>(){{
            put(R.id.color_white, R.color.white); put(R.id.color_black, R.color.black); put(R.id.color_maroon, R.color.maroon); put(R.id.color_red, R.color.red); put(R.id.color_lime, R.color.lime);
            put(R.id.color_magenta, R.color.magenta); put(R.id.color_pink, R.color.pink); put(R.id.color_orange, R.color.orange); put(R.id.color_yellow, R.color.yellow);
            put(R.id.color_aqua, R.color.aqua); put(R.id.color_blue, R.color.blue); put(R.id.color_sky_blue, R.color.sky_blue); put(R.id.color_pale_cyan, R.color.pale_cyan);
            put(R.id.color_green, R.color.green);
        }};

        for (Integer key : colorMap.keySet()){
            final int value = colorMap.get(key);
            Button popupButton = popupView.findViewById(key);
            popupButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view){
                    clearPopupWindow();
                    content.setTextColor(ContextCompat.getColor(getContext().getApplicationContext(), value));
                    if(value != R.color.white)
                        textColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), value));
                    else
                        textColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.black));
                    textColorButton.switchCheckedState();
                    Keyboard.showKeyboard(view);
                }
            });
        }
    }

    // 글 배경 색상 설정 Window
    private void showBgColorPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_text_color, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        Map<Integer,Integer> colorMap = new HashMap<Integer, Integer>(){{
            put(R.id.color_white, R.color.white); put(R.id.color_black, R.color.black); put(R.id.color_maroon, R.color.maroon); put(R.id.color_red, R.color.red); put(R.id.color_lime, R.color.lime);
            put(R.id.color_magenta, R.color.magenta); put(R.id.color_pink, R.color.pink); put(R.id.color_orange, R.color.orange); put(R.id.color_yellow, R.color.yellow);
            put(R.id.color_aqua, R.color.aqua); put(R.id.color_blue, R.color.blue); put(R.id.color_sky_blue, R.color.sky_blue); put(R.id.color_pale_cyan, R.color.pale_cyan);
            put(R.id.color_green, R.color.green);
        }};

        for (Integer key : colorMap.keySet()){
            final int value = colorMap.get(key);
            Button popupButton = popupView.findViewById(key);
            popupButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view){
                    clearPopupWindow();
                    content.setTextBackgroundColor(ContextCompat.getColor(getContext().getApplicationContext(), value));
                    if(value != R.color.white)
                        textBgColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), value));
                    else
                        textBgColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.black));
                    textBgColorButton.switchCheckedState();
                    Keyboard.showKeyboard(view);
                }
            });
        }
    }

    // 글 정렬 설정 Window
    private void showAlignPopupWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup_text_align, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        ImageButton textAlignLeftButton = popupView.findViewById(R.id.text_alignLeft);
        textAlignLeftButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignLeft();
                textAlignButton.switchCheckedState();
                Keyboard.showKeyboard(view);
                content.focusEditor();
            }
        });

        ImageButton textAlignCenterButton = popupView.findViewById(R.id.text_alignCenter);
        textAlignCenterButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignCenter();
                textAlignButton.switchCheckedState();
                Keyboard.showKeyboard(view);
                content.focusEditor();
            }
        });

        ImageButton textAlignRightButton = popupView.findViewById(R.id.text_alignRight);
        textAlignRightButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                clearPopupWindow();
                content.setAlignRight();
                textAlignButton.switchCheckedState();
                Keyboard.showKeyboard(view);
                content.focusEditor();
            }
        });
    }

    // 열려 있는 Window 닫음
    private void clearPopupWindow(){
        if(mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    // 버튼 클릭 후 popup 버튼이 아닌 것을 클릭했을 때 기존 popup 버튼 false로 초기화
    private void clearPopupButton(){
        ArrayList<WriteCustomButton> popupButtons = new ArrayList<>(Arrays.asList(textColorButton, textBgColorButton, textAlignButton));
        for(WriteCustomButton popupbutton : popupButtons){
            popupbutton.setCheckedState(false);
        }
    }

    private void showYoutubeDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_youtube, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = promptView.findViewById(R.id.userInputDialog);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("완료", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String videoid = Youtube.getVideoId(editText.getText().toString());
                        if(videoid.equals("error")){
                            Toast.makeText(getContext() ,"유효하지 않은 URL 입니다.", Toast.LENGTH_LONG).show();
                        }
                        else{
                            content.insertYoutubeVideo(videoid);
                        }
                        Keyboard.closeKeyboard(editText);
                    }
                })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Keyboard.closeKeyboard(editText);
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        // back key를 눌렀을 때 dialog 닫음
        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel();
                    return true;
                }
                return false;
            }
        });
        // youtube insert 누른 후 show Keyboard 및 focus of content 지우고 in editText of dialog에 focus 줌
        Keyboard.showSoftKeyboard(alert);
        content.clearFocus();
        editText.requestFocus();
        alert.show();

    }

    public Button getCancelButton(){
        return cancelButton;
    }

    public Button getConfirmButton(){
        return confirmButton;
    }

    public ImageButton getInsertImageButton() {
        return insertImageButton;
    }

    public RichEditor getContent(){
        return content;
    }

    public String getHtml(){
        return content.getHtml();
    }

}