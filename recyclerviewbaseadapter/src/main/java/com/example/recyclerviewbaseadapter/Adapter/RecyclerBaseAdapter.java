package com.example.recyclerviewbaseadapter.Adapter;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.recyclerviewbaseadapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by allen on 16/5/21.
 */
public abstract class RecyclerBaseAdapter<D, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected final List<D> mData = new ArrayList<>();

    protected OnItemClickListener mListener;

    public static final int VIEW_PROGRESS = 0;  //代表Progress类型
    public static final int VIEW_Item = 1;   //代表普通item

    public static final long NET_TIMEOUT = 5000;

    private long netTimeout = NET_TIMEOUT;  //默认超时5秒   这里主要是当数据还没有加载完成,而已经滑倒底部显示没有数据的情况

    protected Activity mContext;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;  //判断当前是否执行加载操作,而避免重复加载,导致重复数据
    private OnLoadMoreListener onLoadMoreListener;
    private int defaultLoadItem = 5;  //默认预加载为5 可设置

    private View mProgressBarView;  //加载更多的view
    private View mNotMoreDataView;  //没有更多数据的view


    protected RecyclerBaseAdapter(Activity context, RecyclerView recyclerView) {
        this.mContext = context;

        handleLoadMore(recyclerView);
    }

    private void handleLoadMore(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager ||
                recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView,
                                       int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();

                    handleLoadMoreNotData(lastVisibleItem);

                    if (mProgressBarView != null) {
                        mProgressBarView.setVisibility(View.VISIBLE);
                        mNotMoreDataView.setVisibility(View.GONE);
                    }

                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold + defaultLoadItem)) {

                        netTimeout = NET_TIMEOUT;

                        loading = true;
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    /**
     * 处理加载更多没有数据的情况
     *
     * @param lastVisibleItem
     */
    private void handleLoadMoreNotData(int lastVisibleItem) {
        if (getItemViewType(lastVisibleItem) == VIEW_PROGRESS
                && mProgressBarView.getVisibility() == View.VISIBLE) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(netTimeout);
                        setRefreshCompleted();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 设置网络延迟,默认为5000毫秒
     *
     * @param time
     */
    public void setNetTimeout(long time) {
        this.netTimeout = time;
    }

    /**
     * 当次加载完成,可以再次进行下拉刷新
     */
    public void setLoadMoreCompleted() {
        netTimeout = 0;
        loading = false;
    }

    /**
     * 提示
     *
     * @param toastInfo
     */
    protected void toast(String toastInfo) {
//        Snackbar.make(mProgressBarView, toastInfo, Snackbar.LENGTH_SHORT).show();
        Log.d("tag", "tag");
        Toast.makeText(mContext, toastInfo, Toast.LENGTH_SHORT).show();
    }

    /**
     * 注册加载更多
     *
     * @param onLoadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 当刷新完成后调用以隐藏progressbar
     */
    public void setRefreshCompleted() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressBarView != null) {
                    mProgressBarView.setVisibility(View.GONE);
                    mNotMoreDataView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        D item = getItem(position);
        if (item != null) {
            bindDataToView(holder, item);
            setupItemViewClickListener(holder, item, position);
        }
    }

    /**
     * 处理item的点击事件
     *
     * @param holder
     * @param item
     * @param position
     */
    private void setupItemViewClickListener(V holder, final D item, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(position, item);
                }
            }
        });
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_PROGRESS:
                View progressView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, null);
                mProgressBarView = progressView.findViewById(R.id.progressBar1);
                mNotMoreDataView = progressView.findViewById(R.id.not_more_data_tv);
                return createViewHolder(progressView);

            case VIEW_Item:
                View view = createView(parent);
                return createViewHolder(view);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (mData.size() == position) {
            return VIEW_PROGRESS;
        } else {
            return VIEW_Item;
        }
    }

    protected D getItem(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    public void addData(List<D> items) {
        items.removeAll(mData);
        mData.addAll(items);

        notifyDataSetChanged();
    }

    /**
     * 注册item点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener<D> listener) {
        this.mListener = listener;
    }

    /**
     * 数据绑定的操作
     *
     * @param holder
     * @param item
     */
    protected abstract void bindDataToView(V holder, D item);

    /**
     * 创建item展示的view
     *
     * @param parent
     */

    protected abstract View createView(ViewGroup parent);

    protected abstract V createViewHolder(View view);

}
