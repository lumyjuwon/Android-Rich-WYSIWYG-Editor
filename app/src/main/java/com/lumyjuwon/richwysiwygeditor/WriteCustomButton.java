package com.lumyjuwon.richwysiwygeditor;

import android.content.Context;
import android.util.AttributeSet;

public class WriteCustomButton extends android.support.v7.widget.AppCompatImageButton {

    private boolean isChecked = false;

    public WriteCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean getCheckedState(){
        return this.isChecked;
    }

    public void switchCheckedState(){
        if(this.isChecked)
            this.isChecked = false;
        else
            this.isChecked = true;
    }

    public void setCheckedState(boolean bool){
        this.isChecked = bool;
    }

}
