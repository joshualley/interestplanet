package com.example.interestplanet.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.interestplanet.R;
import com.example.interestplanet.adapter.CommentListAdapter;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.ArticleComment;
import com.example.interestplanet.model.User;
import com.example.interestplanet.model.UserArticleAttitude;
import com.example.interestplanet.service.ArticleCommentService;
import com.example.interestplanet.service.ImageService;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArticleDetailActivity extends AppCompatActivity {
    private TextView articleTitle;
    private ImageView articleCover;
    private TextView articleContent;
    private ListView commentsList;
    private TextView commentContent;
    private ImageButton sendBtn;
    private ImageButton goodBtn;
    private ImageButton collectionBtn;

    private User currentUser;
    private Article currentArticle;
    private List<ArticleComment> mComments;
    private UserArticleAttitude attitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        articleTitle = findViewById(R.id.article_title);
        articleCover = findViewById(R.id.article_cover);
        articleContent = findViewById(R.id.article_content);

        commentContent = findViewById(R.id.comment_content);
        sendBtn = findViewById(R.id.comment_send_btn);
        goodBtn = findViewById(R.id.good_btn);
        collectionBtn = findViewById(R.id.collection_btn);

        commentsList = findViewById(R.id.comments_list);

        // get the current user' info
        currentUser = (User) StatusStoreService.getInstance().get("user");
        // get the article's info
        Intent intent = getIntent();
        String articleJson = intent.getStringExtra("article");
        currentArticle = new Gson().fromJson(articleJson, Article.class);

        setOnClick();
        initView();
        initArticleAttitude();
    }

    private void initView() {
        if (currentArticle == null) return;
        articleTitle.setText(currentArticle.getTitle());
        articleContent.setText(currentArticle.getContent());
        String coverUrl = currentArticle.getCover();
        if (!coverUrl.isEmpty()) {
            ImageService.loadStorageImageTo(this, coverUrl, articleCover);
        }

        // initialize the comment list
        ServiceRegister.ArticleCommentServiceInstance
                .findByField("articleId", currentArticle.getId())
                .observe(this, comments -> {
                    mComments = comments;
                    CommentListAdapter adapter = new CommentListAdapter(this, this, mComments);
                    commentsList.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(commentsList);
                });
    }
    private void setOnClick() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sendBtn.setOnClickListener(v -> {
            String aComment = commentContent.getText().toString();
            if (aComment.isEmpty()) {
                Toast.makeText(this, "The comment can not be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            // add a comment
            String now = formatter.format(new Date(System.currentTimeMillis()));
            ArticleComment comment = new ArticleComment(currentUser.getId(), currentArticle.getId(), aComment, now);
            ServiceRegister.ArticleCommentServiceInstance.addOrUpdate(comment);
            // refresh the comment list
            comment.setUser(currentUser);
            mComments.add(comment);
            CommentListAdapter adapter = new CommentListAdapter(this, this, mComments);
            commentsList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(commentsList);
        });
        goodBtn.setOnClickListener(v -> {
            attitude.setGood(!attitude.isGood());
            // switch the icon of isGood button
            goodBtn.setImageResource(0);
            if (attitude.isGood()) {
                goodBtn.setImageResource(R.drawable.ic_good_click);
            } else {
                goodBtn.setImageResource(R.drawable.ic_good);
            }
            ServiceRegister.UserArticleAttitudeServiceInstance.addOrUpdate(attitude);
        });
        collectionBtn.setOnClickListener(v -> {
            attitude.setCollected(!attitude.isCollected());
            collectionBtn.setImageResource(0);
            // switch the icon of isCollected button
            if (attitude.isCollected()) {
                collectionBtn.setImageResource(R.drawable.ic_collection_click);
            } else {
                collectionBtn.setImageResource(R.drawable.ic_collection);
            }
            ServiceRegister.UserArticleAttitudeServiceInstance.addOrUpdate(attitude);
        });
    }

    private void initArticleAttitude() {
        String unionId = currentUser.getId() + "_" + currentArticle.getId();
        ServiceRegister.UserArticleAttitudeServiceInstance
                .findByField("userArticleUnionId", unionId, attitudes -> {
                    if (attitudes.size() > 0) {
                        attitude = attitudes.get(0);
                    } else {
                        attitude = new UserArticleAttitude(unionId, false, false);
                    }

                    // switch the icon of isGood button
                    if (attitude.isGood()) {
                        goodBtn.setImageResource(R.drawable.ic_good_click);
                    } else {
                        goodBtn.setImageResource(R.drawable.ic_good);
                    }
                    // switch the icon of isCollected button
                    if (attitude.isCollected()) {
                        collectionBtn.setImageResource(R.drawable.ic_collection_click);
                    } else {
                        collectionBtn.setImageResource(R.drawable.ic_collection);
                    }
                });
    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}