package com.yangrui.checkselected;

/**
 * 选择条目实体
 * Created by 杨锐 on 16/8/11.
 * QQ 2758113352
 */
public class CheckBean {

    /**
     * 文本
     */
    private String text;

    /**
     * 是否选中
     */
    private boolean check;

    /**
     * 图片类型id
     */
    private int imageResId;

    public CheckBean(String text, boolean check) {
        this.text = text;
        this.check = check;
    }

    public CheckBean(int imageResId,String text, boolean check) {
        this.text = text;
        this.check = check;
        this.imageResId = imageResId;
    }

    public CheckBean() {
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
