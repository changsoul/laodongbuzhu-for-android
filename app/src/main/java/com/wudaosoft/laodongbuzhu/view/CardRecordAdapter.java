package com.wudaosoft.laodongbuzhu.view;

import android.content.Context;
import android.view.ViewGroup;

import com.wudaosoft.laodongbuzhu.model.ApplyRecord;

import cn.lemon.view.adapter.BaseViewHolder;
import cn.lemon.view.adapter.RecyclerAdapter;


public class CardRecordAdapter extends RecyclerAdapter<ApplyRecord> {

    public CardRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<ApplyRecord> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new CardRecordHolder(parent);
    }
}