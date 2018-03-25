package com.wudaosoft.laodongbuzhu.view;

import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wudaosoft.laodongbuzhu.R;
import com.wudaosoft.laodongbuzhu.model.ApplyRecord;

import cn.lemon.view.adapter.BaseViewHolder;

/**
 * Created on 2018/3/24 01:24.
 *
 * @author Changsoul.Wu
 */

public class CardRecordHolder extends BaseViewHolder<ApplyRecord> {

    private TextView applyNo;
    private TextView applyStatus;
    private TextView applyAmount;
    private TextView applyDate;
    private TextView applyJob;

    public CardRecordHolder(ViewGroup parent) {
        super(parent, R.layout.holder_apply_record);
    }

    @Override
    public void setData(final ApplyRecord object) {
        super.setData(object);
        applyNo.setText(object.getApplyNo());
        applyStatus.setText(object.getApplyStatus());
        applyAmount.setText(object.getApplyAmount());
        applyDate.setText(object.getApplyDate());
        applyJob.setText(object.getApplyJob());
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        applyNo = findViewById(R.id.apply_no);
        applyStatus = findViewById(R.id.apply_status);
        applyAmount = findViewById(R.id.apply_amount);
        applyDate = findViewById(R.id.apply_date);
        applyJob = findViewById(R.id.apply_job);
    }

    @Override
    public void onItemViewClick(ApplyRecord object) {
        super.onItemViewClick(object);
        //点击事件
        Log.i("CardRecordHolder","onItemViewClick");
    }
}