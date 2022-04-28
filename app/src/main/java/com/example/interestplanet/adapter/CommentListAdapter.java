package com.example.interestplanet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.interestplanet.R;
import com.example.interestplanet.model.ArticleComment;
import com.example.interestplanet.service.ImageService;

import java.util.List;
import java.util.Objects;

public class CommentListAdapter extends BaseAdapter {

    class ViewHolder {
        private ImageView userAvatar;
        private TextView userName;
        private TextView userComment;
    }

    private LayoutInflater inflater;
    private List<ArticleComment> data;
    private Activity mActivity;

    public CommentListAdapter(Activity activity, Context context, List<ArticleComment> data) {
        this.mActivity = activity;
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
            convertView = inflater.inflate(R.layout.layout_comment_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userAvatar = convertView.findViewById(R.id.user_avatar);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.userComment = convertView.findViewById(R.id.user_comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        ArticleComment comment = data.get(position);
        viewHolder.userName.setText(comment.getUser().getUsername());
        viewHolder.userComment.setText(comment.getComment());
        String avatar = comment.getUser().getAvatarUrl();
        if (avatar.isEmpty()) {
            viewHolder.userAvatar.setImageResource(R.drawable.no_image);
        } else {
            ImageService.loadStorageImageTo(mActivity, avatar, viewHolder.userAvatar);
        }
        return convertView;
    }
}
