package com.wudaosoft.laodongbuzhu.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wudaosoft.laodongbuzhu.R;
import com.wudaosoft.laodongbuzhu.model.ApplyRecord;

import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;

/**
 * Created on 2018/3/21 00:01.
 *
 * @author Changsoul.Wu
 */

public class HomeFragment extends BaseFragement{

    private RefreshRecyclerView mRecyclerView;
    private CardRecordAdapter mAdapter;
    private Handler mHandler;
    private int page = 1;
    private Context mContext;

    @Override
    protected void initView(View view) {
        mHandler = new Handler();

        mContext = getContext();

        mAdapter = new CardRecordAdapter(mContext);

        //添加Header
        final TextView textView = new TextView(mContext);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(48)));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setText("重庆邮电大学");
        mAdapter.setHeader(textView);
        //添加footer
        final TextView footer = new TextView(mContext);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(48)));
        footer.setTextSize(16);
        footer.setGravity(Gravity.CENTER);
        footer.setText("-- Footer --");
        mAdapter.setFooter(footer);

        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
            }
        });

        mRecyclerView.setLoadMoreAction(new Action() {
            @Override
            public void onAction() {
                getData(false);
                page++;
            }
        });

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.showSwipeRefresh();
                getData(true);
            }
        });


    }

    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefresh) {
                    page = 1;
                    mAdapter.clear();
                    mAdapter.addAll(getVirtualData());
                    mRecyclerView.dismissSwipeRefresh();
                    mRecyclerView.getRecyclerView().scrollToPosition(0);
                } else {
                    mAdapter.addAll(getVirtualData());
                    if (page >= 3) {
                        mRecyclerView.showNoMore();
                    }
                }
            }
        }, 1500);
    }

    public ApplyRecord[] getVirtualData() {
        return new ApplyRecord[]{
                new ApplyRecord("876545648974654564", "未提交", "2630", "2018-02-09", "美容师"),
                new ApplyRecord("876545648974654564", "未提交", "2630", "2018-02-09", "美容师")
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.multi_adapter) {
//            startActivity(new Intent(this, CustomMultiTypeActivity.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void getDataFromServer() {
        Toast.makeText(mContext, "HomeFragment页面请求数据了", Toast.LENGTH_SHORT).show();
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
