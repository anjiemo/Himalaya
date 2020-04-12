package com.smart.himalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smart.himalaya.R;
import com.smart.himalaya.base.BaseApplication;
import com.smart.himalaya.views.MyPopWindow;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.InnerHolder> {

    private List<Track> mData = new ArrayList<>();
    private int playingIndex = 0;
    private MyPopWindow.PlayListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_list, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position);
            }
        });
        //设置数据
        Track track = mData.get(position);
        //找到播放状态的图标
        holder.mPlayingIconView.setVisibility(playingIndex == position ? View.VISIBLE : View.GONE);
        //设置字体颜色
        holder.mTrackTitle.setTextColor(BaseApplication.getAppContext().getResources().
                getColor(playingIndex == position ? R.color.second_color : R.color.play_list_text_color));
        holder.mTrackTitle.setText(track.getTrackTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Track> data) {
        //设置数据，更新列表
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void setCurrentPlayPosition(int position) {
        playingIndex = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(MyPopWindow.PlayListItemClickListener listener) {
        mItemClickListener = listener;
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        private ImageView mPlayingIconView;
        private TextView mTrackTitle;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            //找到播放状态的图标
            mPlayingIconView = itemView.findViewById(R.id.play_icon);
            mTrackTitle = itemView.findViewById(R.id.track_title_tv);
        }
    }
}
