package com.smart.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.himalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailListAdapter extends RecyclerView.Adapter<DetailListAdapter.InnerHolder> {

    private List<Track> mDetailData = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mUpdateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");

    @NonNull
    @Override
    public DetailListAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailListAdapter.InnerHolder holder, int position) {
        //找到控件，设置数据

        Track track = mDetailData.get(position);

        //顺序Id
        holder.mOrderText.setText((position + 1) + "");
        //标题
        holder.mDetailItemTitle.setText(track.getTrackTitle());
        //播放次数
        holder.mDetailItemPlayCount.setText(track.getPlayCount() + "");
        //时长
        long durationMil = track.getDuration() * 1000L;
        String duration = mDurationFormat.format(durationMil);
        holder.mDetailItemDuration.setText(duration);
        //更新日期
        String updateTimeText = mUpdateFormat.format(track.getUpdatedAt());
        holder.mDetailItemUpdateTime.setText(updateTimeText);

    }

    @Override

    public int getItemCount() {
        if (mDetailData != null) {
            return mDetailData.size();
        }
        return 0;
    }

    public void setData(List<Track> tracks) {
        //清除原来的数据
        mDetailData.clear();
        //添加新的数据
        mDetailData.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private TextView mOrderText;
        private TextView mDetailItemTitle;
        private TextView mDetailItemPlayCount;
        private TextView mDetailItemDuration;
        private TextView mDetailItemUpdateTime;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mOrderText = (TextView) itemView.findViewById(R.id.order_text);
            mDetailItemTitle = (TextView) itemView.findViewById(R.id.detail_item_title);
            mDetailItemPlayCount = (TextView) itemView.findViewById(R.id.detail_item_play_count);
            mDetailItemDuration = (TextView) itemView.findViewById(R.id.detail_item_duration);
            mDetailItemUpdateTime = (TextView) itemView.findViewById(R.id.detail_item_update_time);

        }
    }
}
