package com.smart.himalaya.adapters;

import android.annotation.SuppressLint;
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

@SuppressLint("SimpleDateFormat")
public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.InnerHolder> {

    private List<Track> mDetailData = new ArrayList<>();
    //格式化时间
    private SimpleDateFormat mUpdateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mDurationFormat = new SimpleDateFormat("mm:ss");
    private ItemClickListener mItemClickListener = null;
    private ItemLongClickListener mItemLongClickListener = null;

    public void setData(List<Track> tracks) {
        //清除原来的数据
        mDetailData.clear();
        //添加新的数据
        mDetailData.addAll(tracks);
        //更新UI
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDetailData.size();
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //设置数据
        Track track = mDetailData.get(position);
        holder.mOrderTv.setText(String.valueOf(position + 1));
        holder.mTitleTv.setText(track.getTrackTitle());
        holder.mPlayCountTv.setText(String.valueOf(track.getPlayCount()));
        int durationMil = track.getDuration() * 1000;
        String duration = mDurationFormat.format(durationMil);
        holder.mDurationTv.setText(duration);
        String updateTimeText = mUpdateFormat.format(track.getUpdatedAt());
        holder.mUpdateDateTv.setText(updateTimeText);
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(mDetailData,position);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClick(track);
            }
            return true;
        });
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(List<Track> detailData, int position);
    }

    public void setItemLongClickListener(ItemLongClickListener itemLongClickListener) {
        mItemLongClickListener = itemLongClickListener;
    }

    public interface ItemLongClickListener{
        void onItemLongClick(Track track);
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private TextView mOrderTv;
        private TextView mTitleTv;
        private TextView mPlayCountTv;
        private TextView mDurationTv;
        private TextView mUpdateDateTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            mOrderTv = itemView.findViewById(R.id.order_text);
            mTitleTv = itemView.findViewById(R.id.detail_item_title);
            mPlayCountTv = itemView.findViewById(R.id.detail_item_play_count);
            mDurationTv = itemView.findViewById(R.id.detail_item_duration);
            mUpdateDateTv = itemView.findViewById(R.id.detail_item_update_time);
        }
    }
}
