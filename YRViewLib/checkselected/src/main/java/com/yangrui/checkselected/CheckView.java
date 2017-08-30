package com.yangrui.checkselected;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

/**
 * 水平多选控件
 * Created by 杨锐 on 16/8/11.
 * QQ 2758113352
 */
public class CheckView extends HorizontalScrollView {

    /**
     * 选中列表
     */
    private List<CheckBean> checkBeanList;

    /**
     * 容器
     */
    private LinearLayout container;

    /**
     * 是否长条目
     */
    private int cvType;

    /**
     * 条目宽度
     */
    private int itemWidth;

    /**
     * 条目高度
     */
    private int itemHeight;


    public CheckView(Context context) {
        super(context);
        initView();
    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        cvType = attrsArray.getInteger(R.styleable.CheckView_cvType,1);
        initView();
    }

    public CheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.CheckView);
        cvType = attrsArray.getInteger(R.styleable.CheckView_cvType,1);
        initView();
    }

    /**
     * 构建视图
     */
    public void initView(){
        itemHeight=ScreenUtils.dip2px(getContext(),43);
        if(cvType==2){
            itemWidth=ScreenUtils.dip2px(getContext(),100);
        }else{
            itemWidth=ScreenUtils.dip2px(getContext(),43);
        }
        container=new LinearLayout(getContext());
        setHorizontalScrollBarEnabled(false);
        addView(container);
    }

    /**
     * 设置数据
     * @param checkBeanList
     */
    public void setDataList(final List<CheckBean> checkBeanList){
        this.checkBeanList=checkBeanList;
        int margin=ScreenUtils.dip2px(getContext(),2f);
        int headMargin=ScreenUtils.dip2px(getContext(),7f);
        for (int i=0;i<checkBeanList.size();i++){
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(itemWidth,itemHeight);
            if(i==0){
                layoutParams.setMargins(headMargin, 0, margin, 0);
            }else if(i==checkBeanList.size()-1){
                layoutParams.setMargins(margin, 0, headMargin, 0);
            }else {
                layoutParams.setMargins(margin, 0, margin, 0);
            }
            View itemView;

            if(cvType==1||cvType==2) {
                itemView=new TextView(getContext());
                ((TextView) itemView).setGravity(Gravity.CENTER);
                ((TextView) itemView).setText(checkBeanList.get(i).getText());
            }else{
                itemView=new ImageView(getContext());
                ((ImageView) itemView).setImageResource(checkBeanList.get(i).getImageResId());
                itemView.setPadding(20,20,20,20);
            }
            itemView.setBackgroundResource(cvType==2?R.drawable.selector_check_long_bg:R.drawable.selector_check_bg);
            itemView.setSelected(checkBeanList.get(i).isCheck());
            itemView.setLayoutParams(layoutParams);
            final int finalI = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkBeanList.get(finalI).isCheck()) {
                        setSelected(finalI,false);
                    }else{
                        setSelected(finalI,true);
                    }
                }
            });
            container.addView(itemView);
        }
    }

    /**
     * 设置是否选中
     * @param index
     * @param check
     */
    public void setSelected(int index,boolean check){
        checkBeanList.get(index).setCheck(check);
        container.getChildAt(index).setSelected(check);
    }

    public List<CheckBean> getCheckBeanList() {
        return checkBeanList;
    }

    /**
     * 清空重置
     */
    public void reset(){
        for(int i=0;i<checkBeanList.size();i++){
            setSelected(i,false);
        }
    }

    /**
     * 获取选中文本
     */
    public List<String> getSelectedList(){
        List<String> selectList=new ArrayList<>();
        for(int i=0;i<checkBeanList.size();i++){
            if(checkBeanList.get(i).isCheck())
                selectList.add(checkBeanList.get(i).getText());
        }
        return selectList;
    }
}
