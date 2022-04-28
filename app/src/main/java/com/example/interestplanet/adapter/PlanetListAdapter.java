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
import com.example.interestplanet.R;
import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.service.ImageService;

import java.util.List;

public class PlanetListAdapter extends BaseAdapter {

    class ViewHolder {
        private TextView name;
        private ImageView cover;
    }

    private Activity mActivity;
    private LayoutInflater inflater;
    private List<Planet> data;

    public PlanetListAdapter(Activity activity, Context context, List<Planet> data) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.mActivity = activity;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_planet_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.planet_name);
            viewHolder.cover = convertView.findViewById(R.id.planet_cover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Planet planet = data.get(position);
        viewHolder.name.setText(planet.getName());
        String coverUrl = planet.getCover();
        if (coverUrl.isEmpty()) {
            viewHolder.cover.setImageResource(R.drawable.planet);
        } else {
            ImageService.loadStorageImageTo(mActivity, coverUrl, viewHolder.cover);
        }
        return convertView;
    }
}
