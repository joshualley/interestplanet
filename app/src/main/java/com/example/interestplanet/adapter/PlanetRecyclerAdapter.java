package com.example.interestplanet.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.interestplanet.R;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.service.ImageService;

import java.util.List;

public class PlanetRecyclerAdapter extends RecyclerView.Adapter<PlanetRecyclerAdapter.ViewHolder> {

    public interface OnItemClickListener {
        public void OnItemClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView name;
        public ImageView cover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.planet_item_layout);
            name = itemView.findViewById(R.id.planet_name);
            cover = itemView.findViewById(R.id.planet_cover);
        }
    }

    private Activity mActivity;
    private List<Planet> data;
    private OnItemClickListener mOnItemClickListener;

    public Planet getItem(int position) {
        return data.get(position);
    }

    public void changeData(List<Planet> planets) {
        if (planets != null) {
            int previousSize = data.size();
            data.clear();
            notifyItemRangeRemoved(0, previousSize);
            data.addAll(planets);
            notifyItemRangeInserted(0, planets.size());
        }
    }

    public PlanetRecyclerAdapter(Activity activity, List<Planet> data) {
        this.mActivity = activity;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_planet_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Planet planet = data.get(position);
        holder.name.setText(planet.getName());
        String coverUrl = planet.getCover();
        if (coverUrl.isEmpty()) {
            holder.cover.setImageResource(R.drawable.planet);
        } else {
            ImageService.loadStorageImageTo(mActivity, coverUrl, holder.cover);
        }
        if (mOnItemClickListener != null) {
            holder.root.setOnClickListener(v -> {
                mOnItemClickListener.OnItemClick(holder.root, position);
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
