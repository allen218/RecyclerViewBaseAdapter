package com.example.mac.recyclerbaseadapter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mac.recyclerbaseadapter.info.DataInfo;
import com.example.recyclerviewbaseadapter.Adapter.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.list_rv)
    RecyclerView mList;

    private List<DataInfo> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData(40);

        loadData(mDatas);
    }

    private void initData(int num) {

        for (int x = 0; x < num; x++) {
            DataInfo dataInfo = new DataInfo();
            dataInfo.setName("allen");
            dataInfo.setEmail("allen@110.com#" + x);
            mDatas.add(dataInfo);
        }
    }


    private TwoTypeAdapter mAdapter;

    private void loadData(List<DataInfo> datas) {

        if (mAdapter == null) {

            LinearLayoutManager manager = new LinearLayoutManager(this);
            mList.setLayoutManager(manager);

            mAdapter = new TwoTypeAdapter(this, mList);
            mAdapter.setOnItemClickListener((position, dataInfo) ->
                    handleItemClick(position, dataInfo));

            mAdapter.setOnLoadMoreListener(onLoadMoreListener);
            mAdapter.addDatas(datas);

            mList.setAdapter(mAdapter);

            mList.setVisibility(View.VISIBLE);

        } else {
            mAdapter.addDatas(datas);
        }

        //每次加载完成后,设置该方法,让其可以继续加载更多
        mAdapter.setLoadMoreCompleted();

    }

    public OnLoadMoreListener onLoadMoreListener = () -> {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                initData(60);
                runOnUiThread((Runnable) () -> loadData(mDatas));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    };

    private void handleItemClick(int position, DataInfo o) {
        Snackbar.make(mList, "您点击了第" + position + "个item", Snackbar.LENGTH_SHORT).show();
    }
}
