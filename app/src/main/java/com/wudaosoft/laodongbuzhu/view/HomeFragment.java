package com.wudaosoft.laodongbuzhu.view;

import android.widget.Toast;

import com.wudaosoft.laodongbuzhu.R;

/**
 * Created on 2018/3/21 00:01.
 *
 * @author Changsoul.Wu
 */

public class HomeFragment extends BaseFragement{

    @Override
    protected void initView() {

    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }
    @Override
    protected void getDataFromServer() {
        Toast.makeText(mContext, "HomeFragment页面请求数据了", Toast.LENGTH_SHORT).show();
    }
}
