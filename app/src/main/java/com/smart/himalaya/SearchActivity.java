package com.smart.himalaya;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.himalaya.base.BaseActivity;
import com.smart.himalaya.interfaces.ISearchCallback;
import com.smart.himalaya.presenters.SearchPresenter;
import com.smart.himalaya.utils.LogUtil;
import com.smart.himalaya.views.FlowTextLayout;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.QueryResult;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends BaseActivity implements ISearchCallback {

    private static final String TAG = "SearchActivity";
    private ImageView mBackBtn;
    private EditText mInputBox;
    private TextView mSearchBtn;
    private FrameLayout mResultContainer;
    private SearchPresenter mSearchPresenter;
    private FlowTextLayout mFlowTextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initEvent();
        initPresenter();
    }

    private void initPresenter() {
        //注册UI更新的接口
        mSearchPresenter = SearchPresenter.getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //去拿热词
        mSearchPresenter.getHotWord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchPresenter != null) {
            //干掉UI更新的接口
            mSearchPresenter.unRegisterViewCallback(this);
            mSearchPresenter = null;
        }
    }

    private void initEvent() {
        mBackBtn.setOnClickListener(v -> finish());
        mSearchBtn.setOnClickListener(v -> {
            //去调用搜索的逻辑
        });
        mInputBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                LogUtil.d(TAG, "content --- > " + s);
//                LogUtil.d(TAG, "content --- > " + start);
//                LogUtil.d(TAG, "content --- > " + before);
//                LogUtil.d(TAG, "content --- > " + count);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mFlowTextLayout.setClickListener(text -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show());
    }

    private void initView() {
        mBackBtn = findViewById(R.id.search_back);
        mInputBox = findViewById(R.id.search_input);
        mSearchBtn = findViewById(R.id.search_btn);
        mResultContainer = findViewById(R.id.search_container);
        mFlowTextLayout = findViewById(R.id.flow_text_layout);
    }

    @Override
    public void onSearchResultLoaded(List<Album> result) {

    }

    @Override
    public void onHotWordLoaded(List<HotWord> hotWordList) {
        LogUtil.d(TAG, "hotWordList --- > " + hotWordList.size());
        List<String> hotWords = new ArrayList<>();
//        hotWords.clear();
        for (HotWord hotWord : hotWordList) {
            String searchWord = hotWord.getSearchword();
            hotWords.add(searchWord);
        }
        Collections.sort(hotWords);
        //更新UI。
        mFlowTextLayout.setTextContents(hotWords);
    }

    @Override
    public void onLoadMoreResult(List<Album> result, boolean isOkay) {

    }

    @Override
    public void onRecommendWordLoaded(List<QueryResult> keyWordList) {

    }
}
