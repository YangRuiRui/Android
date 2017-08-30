package com.yangrui.yrviewlib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yangrui.checkselected.CheckBean;
import com.yangrui.checkselected.CheckView;
import com.yangrui.tagselected.TagSelectView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> certificateList = new ArrayList<String>();
        certificateList.add("NTFC1");
        certificateList.add("NTFC2");
        certificateList.add("NTFC3");
        certificateList.add("NTFC4");
        certificateList.add("NTFC5");
        certificateList.add("NTFC6");
        certificateList.add("NTFC7");
        certificateList.add("NTFC8");
        certificateList.add("NTFC9");
        certificateList.add("NTFC10");
        certificateList.add("NTFC11");
        certificateList.add("NTFC12");
        ((TagSelectView)findViewById(R.id.tv_certificate)).setTagList(certificateList);

        List<CheckBean> certList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CheckBean checkBean=new CheckBean();
            checkBean.setText("范例"+i);
            checkBean.setCheck(i%2==2);
            certList.add(checkBean);
        }
        ((CheckView)findViewById(R.id.cv_shape)).setDataList(certList);
    }
}
