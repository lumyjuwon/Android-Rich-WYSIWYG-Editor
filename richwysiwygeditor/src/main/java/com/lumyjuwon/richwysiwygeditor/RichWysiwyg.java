package com.lumyjuwon.richwysiwygeditor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.lumyjuwon.richwysiwygeditor.RichEditor.RichEditor;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.ImgPicker;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.Keyboard;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.TextColor;
import com.lumyjuwon.richwysiwygeditor.WysiwygUtils.Youtube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private ArrayList<WriteCustomButton> popupButtons;
    private ArrayList<WriteCustomButton> Buttons;
    private LayoutInflater layoutInflater;

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
                    closePopupWindow();
                    button.switchCheckedState();
                }
                else {
                    closePopupWindow();
                    content.clearFocusEditor();
                    if(button.getId() == R.id.write_textColor)
                        showColorPopupWindow(view);
                    else if(button.getId() == R.id.write_textBgColor)
                        showBgColorPopupWindow(view);
                    else if(button.getId() == R.id.write_textAlign)
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

                closePopupWindow();
                clearPopupButton();
                content.clearAndFocusEditor();
                if(button.getId() == R.id.write_textBold)
                    content.setBold();
                else if(button.getId() == R.id.write_textItalic)
                    content.setItalic();
                else if(button.getId() == R.id.write_textUnderLine)
                    content.setUnderline();
                else if(button.getId() == R.id.write_textStrike)
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
                Buttons = new ArrayList<>(Arrays.asList(textColorButton, textBgColorButton, textBoldButton, textItalicButton, textUnderlineButton, textStrikeButton));
                for(RichEditor.Type type : types){
                    if(type.name().contains("FONT_COLOR")){
                        textColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), TextColor.getColor(type.name())));
                        if(textColorButton.getCheckedState())
                            textColorButton.switchCheckedState();
                        Buttons.remove(textColorButton);
                    }
                    else if(type.name().contains("BACKGROUND_COLOR")){
                        textBgColorButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), TextColor.getColor(type.name())));
                        if(textBgColorButton.getCheckedState())
                            textBgColorButton.switchCheckedState();
                        Buttons.remove(textBgColorButton);
                    }
                    else{
                        switch(type){
                            case BOLD:
                                textBoldButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textBoldButton.getCheckedState())
                                    textBoldButton.switchCheckedState();
                                Buttons.remove(textBoldButton);
                                break;
                            case ITALIC:
                                textItalicButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textItalicButton.getCheckedState())
                                    textItalicButton.switchCheckedState();
                                Buttons.remove(textItalicButton);
                                break;
                            case UNDERLINE:
                                textUnderlineButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textUnderlineButton.getCheckedState())
                                    textUnderlineButton.switchCheckedState();
                                Buttons.remove(textUnderlineButton);
                                break;
                            case STRIKETHROUGH:
                                textStrikeButton.setColorFilter(ContextCompat.getColor(getContext().getApplicationContext(), R.color.sky_blue));
                                if(!textStrikeButton.getCheckedState())
                                    textStrikeButton.switchCheckedState();
                                Buttons.remove(textStrikeButton);
                                break;
                            default:
                        }
                    }
                }
                for(WriteCustomButton button : Buttons){
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
                closePopupWindow();
            }
        });

        // 등록 버튼
        confirmButton = findViewById(R.id.write_confirmButton);
        confirmButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                closePopupWindow();
                // 백엔드
            }
        });

        // Text Size 버튼
        ImageButton textSizeButton = findViewById(R.id.write_textSize);
        textSizeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view){
                closePopupWindow();
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

        // Image Insert 버튼
        insertImageButton = findViewById(R.id.write_imageInsert);
        insertImageButton.setOnClickListener(new OnClickListener(){
            @Override public void onClick(View v) {
                ImgPicker.start(v);
            }
        });

        // embed youtube link를 클릭했을 경우 youtube app으로 실행
        content.setYoutubeLoadLinkListener(new RichEditor.YoutubeLoadLinkListener() {
            @Override
            public void onReceivedEvent(String videoId) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoId));
                getContext().startActivity(webIntent);
            }
        });

        // Video Insert 버튼
        ImageButton videoInsertButton = findViewById(R.id.write_videoInsert);
        videoInsertButton.setOnClickListener(new OnClickListener(){
            @Override public void onClick(View v) {
                closePopupWindow();
                clearPopupButton();
                Youtube.showYoutubeDialog(layoutInflater, content, v);
            }
        });

        popupButtons = new ArrayList<>(Arrays.asList(textColorButton, textBgColorButton, textAlignButton));

    }

    // 글 사이즈 조절 설정 Window
    private void showSizePopupWindow(View view) {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_text_size, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        ImageButton textAlignLeftButton = popupView.findViewById(R.id.text_alignLeft);
        textAlignLeftButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                closePopupWindow();
                content.setAlignLeft();
            }
        });

        ImageButton textAlignCenterButton = popupView.findViewById(R.id.text_alignCenter);
        textAlignCenterButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                closePopupWindow();
                content.setAlignCenter();
            }
        });

        ImageButton textAlignRightButton = popupView.findViewById(R.id.text_alignRight);
        textAlignRightButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                closePopupWindow();
                content.setAlignRight();
            }
        });
    }

    // 글 색상 설정 Window
    private void showColorPopupWindow(View view) {
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_text_color, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        for (Integer key : TextColor.colorMap.keySet()){
            final int value = TextColor.colorMap.get(key);
            Button popupButton = popupView.findViewById(key);
            popupButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view){
                    closePopupWindow();
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
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_text_color, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        for (Integer key : TextColor.colorMap.keySet()){
            final int value = TextColor.colorMap.get(key);
            Button popupButton = popupView.findViewById(key);
            popupButton.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View view){
                    closePopupWindow();
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
        layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup_text_align, null);
        mPopupWindow = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(-1); // 생성 애니메이션 -1, 생성 애니메이션 사용 안 함 0
        mPopupWindow.showAsDropDown(view, 0, +15);

        ImageButton textAlignLeftButton = popupView.findViewById(R.id.text_alignLeft);
        textAlignLeftButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view){
                closePopupWindow();
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
                closePopupWindow();
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
                closePopupWindow();
                content.setAlignRight();
                textAlignButton.switchCheckedState();
                Keyboard.showKeyboard(view);
                content.focusEditor();
            }
        });
    }

    // 열려있는 Window 닫음
    private void closePopupWindow(){
        if(mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    // 버튼 클릭 후 popup 버튼이 아닌 것을 클릭했을 때 기존 popup 버튼 false로 초기화
    private void clearPopupButton(){
        for(WriteCustomButton popupbutton : popupButtons){
            popupbutton.setCheckedState(false);
        }
    }

    public Button getCancelButton(){
        return cancelButton;
    }

    public Button getConfirmButton(){
        return confirmButton;
    }

    public EditText getHeadlineEditText(){
        return headline;
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