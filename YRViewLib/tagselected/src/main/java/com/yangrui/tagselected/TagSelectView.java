package com.yangrui.tagselected;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * 滑动单选控件
 * Created by 杨锐 on 16/7/10.
 * QQ 2758113352
 */
public class TagSelectView extends HorizontalScrollView {

    /**
     * 根布局
     */
    private LinearLayout rootView;

    /**
     * 文本内容
     */
    private List<String> tagList=new ArrayList<>();

    /**
     * 左右间距
     */
    private int marginLeftRight= ScreenUtils.dip2px(getContext(),5);

    /**
     * 标签中心位置列表
     */
    private List<Integer> positionList=new ArrayList<>();

    /**
     * 宽度列表
     */
    private List<Integer> widthList=new ArrayList<>();

    /**
     * 选中的下表
     */
    private int selectIndex;

    /**
     * 容器宽度
     */
    private int totalWidth=0;

    /**
     * 滑动校准动画
     */
    private ValueAnimator valueAnimator;

    /**
     * 是否滑动中标志
     */
    private boolean fly;

    /**
     * 偏移量变量
     */
    private int scaleTouchSlop;

    /**
     * 偏移量变量
     */
    private float preX;

    /**
     * 选中改变事件
     */
    private OnSelectedChangeListener onSelectedChangeListener;

    public TagSelectView(Context context) {
        super(context);
        initView();
    }

    public TagSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        scaleTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        rootView= (LinearLayout) View.inflate(getContext(), R.layout.tag_layout, null);
        setHorizontalScrollBarEnabled(false);
        addView(rootView);
    }

    /**
     * 添加标签
     * @param text
     */
    private void addTag(String text, final int index){
        tagList.add(text);
        TextView tagView= (TextView) View.inflate(getContext(), R.layout.item_tag_text, null);
        tagView.setText(text);
        tagView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!fly)
                    setSelectIndex(index);
            }
        });
        //标签边距
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(-1,-1);
        layoutParams.setMargins(marginLeftRight,0,marginLeftRight,0);
        tagView.setLayoutParams(layoutParams);
        int width=ViewUtils.getViewWidth(tagView);
        widthList.add(width+marginLeftRight*2);
        rootView.addView(tagView);
    }

    /**
     * 添加标签
     * @param list
     */
    public void setTagList(List<String> list){
        for (int i=0;i<list.size();i++){
            addTag(list.get(i),i);
        }
        if(list!=null&&list.size()>0) {
            totalWidth = ViewUtils.getViewWidth(this);
            int startPosition=(totalWidth-widthList.get(0))/2;
            positionList.add(totalWidth/2);
            rootView.setPadding(startPosition, 0, (totalWidth  - widthList.get(widthList.size()-1))/2, 0);
            setSelectIndex(0);
        }
        for (int i=1;i<list.size();i++){
            positionList.add(positionList.get(positionList.size()-1)+widthList.get(i-1)/2+widthList.get(i)/2);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(fly){
            return true;
        }
        if(ev.getAction()==MotionEvent.ACTION_UP){
            if(positionList.size()>0) {
                int selectedPosition = positionList.get(0);
                int selectedIndex=0;
                for (int i = 0; i < positionList.size(); i++) {
                    if (Math.abs(getScrollX()+totalWidth/2 -selectedPosition) > Math.abs(getScrollX()+totalWidth/2 - positionList.get(i))) {
                        selectedPosition = positionList.get(i);
                        selectedIndex=i;
                    }
                }
                setSelectIndex(selectedIndex);
                return true;
            }else{
                return super.onTouchEvent(ev);
            }
        }else {
            return super.onTouchEvent(ev);
        }
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    /**
     * 设置选中某标签
     * @param selectIndex
     */
    public void setSelectIndex(int selectIndex) {
        rootView.getChildAt(this.selectIndex).setSelected(false);
        this.selectIndex = selectIndex;
        valueAnimator=ValueAnimator.ofInt(getScrollX(),positionList.get(selectIndex)-totalWidth/2);
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int postion = (int) animation.getAnimatedValue();
                scrollTo(postion,0);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                fly=true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fly=false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                fly=false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                fly=true;
            }
        });
        valueAnimator.start();
        rootView.getChildAt(selectIndex).setSelected(true);

        if(onSelectedChangeListener!=null){
            onSelectedChangeListener.onSelected(selectIndex,tagList.get(selectIndex));
        }
    }

    /**
     * 获取文本在标签中的位置
     */
    public void setSelectText(String text) {
        int x=-1;
        for(int i=0;i<tagList.size();i++){
            if(tagList.get(i).equals(text)){
                x=i;
                break;
            }
        }
        if(x!=-1){
            setSelectIndex(x);
        }
    }

    /**
     * 获取选中文本
     */
    public String getSelectedText(){
        if(tagList==null||tagList.size()==0){
            return null;
        }else {
            return tagList.get(selectIndex);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                preX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float instanceX = Math.abs(moveX - preX);
                Log.i("refresh...","move: instanceX:" + instanceX + "=(moveX:" + moveX + " - preX:" + preX + ") , scaleTouchSlop:" + scaleTouchSlop);

                // 容差值大概是24，再加上60
                if(instanceX > scaleTouchSlop + 60){
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 选中改变事件
     */
    public interface OnSelectedChangeListener{
        void onSelected(int index, String text);
    }

    public OnSelectedChangeListener getOnSelectedChangeListener() {
        return onSelectedChangeListener;
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.onSelectedChangeListener = onSelectedChangeListener;
    }
}
