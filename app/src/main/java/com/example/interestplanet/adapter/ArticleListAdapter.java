package com.example.interestplanet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.interestplanet.R;
import com.example.interestplanet.model.Article;

import java.util.List;

public class ArticleListAdapter extends BaseAdapter {

    class ViewHolder {
        private TextView title;
        private TextView author;
        private TextView date;
        private TextView content;
    }

    private LayoutInflater inflater;
    private List<Article> data;

    public ArticleListAdapter(Context context, List<Article> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_article_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.article_title);
            viewHolder.author = convertView.findViewById(R.id.article_author);
            viewHolder.date = convertView.findViewById(R.id.article_create_date);
            viewHolder.content = convertView.findViewById(R.id.article_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Article article = data.get(position);
        viewHolder.title.setText(article.getTitle());
        viewHolder.author.setText("Author: " + article.getAuthor().getUsername());
        viewHolder.date.setText("Date: " + article.getDate());
        viewHolder.content.setText(article.getContent());
        return convertView;
    }
}
