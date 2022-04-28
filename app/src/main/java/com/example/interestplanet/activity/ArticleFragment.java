package com.example.interestplanet.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.interestplanet.R;
import com.example.interestplanet.adapter.ArticleListAdapter;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.service.ServiceRegister;
import com.google.gson.Gson;

import java.util.List;


public class ArticleFragment extends Fragment {
    private Context mContext;
    private ProgressDialog progress;
    private ListView articleList;
    private EditText searchContent;
    private ImageButton searchBtn;

    // data
    private List<Article> mArticles;

    public ArticleFragment() {}

    public static ArticleFragment newInstance() {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_article, container, false);
        mContext = fragment.getContext();

        // initialize the controls
        articleList = fragment.findViewById(R.id.article_list);
        searchContent = fragment.findViewById(R.id.searchContent);
        searchBtn = fragment.findViewById(R.id.searchBtn);

        setOnClick();
        search();

        return fragment;
    }

    private void setOnClick() {
        // set the click event of searching
        searchBtn.setOnClickListener(v -> search());
        // set the click event of the article list's item
        articleList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (mArticles == null) return;
            String articleJson = new Gson().toJson(mArticles.get(position));
            Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
            intent.putExtra("article", articleJson);
            startActivity(intent);
        });
    }

    private void search() {
        // show the progress dialog
        progress = new ProgressDialog(mContext);
        progress.setMessage("Loading...");
        progress.setMax(100);
        progress.setProgress(0);
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.show();
        progress.setProgress(10);
        String content = searchContent.getText().toString();
        ServiceRegister.ArticleServiceInstance
                .findLikeField("title", content)
                .observe(this, articles -> {
                    mArticles = articles;
                    progress.setProgress(60);
                    ArticleListAdapter adapter = new ArticleListAdapter(mContext, mArticles);
                    articleList.setAdapter(adapter);
                    progress.setProgress(100);
                    progress.dismiss();
                });
    }
}