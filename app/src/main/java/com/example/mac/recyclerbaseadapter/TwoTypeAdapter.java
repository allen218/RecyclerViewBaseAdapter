package com.example.mac.recyclerbaseadapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mac.recyclerbaseadapter.info.DataInfo;
import com.example.recyclerviewbaseadapter.Adapter.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by allen on 16/5/22.
 * 两种类型的adapter 一个普通view和一个progressbar view
 */
public class TwoTypeAdapter extends RecyclerBaseAdapter<DataInfo, TwoTypeAdapter.MainViewHolder> {
    protected TwoTypeAdapter(Activity context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    protected void bindDataToView(MainViewHolder holder, DataInfo item) {
        holder.mNameTv.setText(item.getName() + "");
        holder.mEmailTv.setText(item.getEmail() + "");
    }

    @Override
    protected View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
    }

    @Override
    protected MainViewHolder createViewHolder(View view) {
        return new MainViewHolder(view);
    }

    @Override
    protected String getErrorString() {
        return errorString;
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView mHeaderIv; //头像

        TextView mNameTv;  //名字

        TextView mEmailTv;  //邮箱

        public MainViewHolder(View itemView) {
            super(itemView);
            mHeaderIv = (ImageView) itemView.findViewById(R.id.list_item_header_iv);
            mNameTv = (TextView) itemView.findViewById(R.id.list_item_name_tv);
            mEmailTv = (TextView) itemView.findViewById(R.id.list_item_email_tv);
        }
    }

    private String errorString; //错误信息,当正常加载时,需要将其置为空

    public void addErrorInfo(String errorInfo) {
        this.errorString = errorInfo;
    }

    public void addDatas(List<DataInfo> list) {
        errorString = null;
        super.addData(list);
    }
}
