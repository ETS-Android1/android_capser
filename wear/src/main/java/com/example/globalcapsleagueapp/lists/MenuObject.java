package com.example.globalcapsleagueapp.lists;

import android.view.View;

public class MenuObject {

    public MenuObject(MenuObjectType type, String text, View.OnClickListener onClickListener) {
        this.type = type;
        this.text = text;
        this.onClickListener = onClickListener;
    }

    public MenuObject(MenuObjectType type, String text,int drawable, View.OnClickListener onClickListener) {
        this.type = type;
        this.text = text;
        this.onClickListener = onClickListener;
        this.drawable=drawable;
    }
    public MenuObject(MenuObjectType type, String text,int drawable) {
        this.type = type;
        this.text = text;
        this.drawable=drawable;
    }

    public MenuObject(MenuObjectType type, String text) {
        this.type = type;
        this.text = text;
    }

    private View.OnClickListener onClickListener;
    private MenuObjectType type;
    private String text;
    private int drawable;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MenuObjectType getType() {
        return type;
    }

    public void setType(MenuObjectType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public enum MenuObjectType{
        HEADING,ELEMENT
    }
}
