package com.example.interestplanet.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.interestplanet.R;
import com.example.interestplanet.adapter.ArticleListAdapter;
import com.example.interestplanet.adapter.PlanetRecyclerAdapter;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.model.User;
import com.example.interestplanet.service.ServiceRegister;
import com.example.interestplanet.service.StatusStoreService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PlanetFragment extends Fragment {
    private Context mContext;
    private ProgressDialog progress;
    private RecyclerView planetRecycler;
    private EditText searchContent;
    private ImageButton searchBtn;
    private ImageButton newBtn;
    private PlanetRecyclerAdapter adapter;

    public static PlanetFragment newInstance() {
        PlanetFragment fragment = new PlanetFragment();
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
        View fragment = inflater.inflate(R.layout.fragment_planet, container, false);
        mContext = fragment.getContext();

        // initialize the controls
        newBtn = fragment.findViewById(R.id.new_btn);
        searchContent = fragment.findViewById(R.id.searchContent);
        searchBtn = fragment.findViewById(R.id.searchBtn);
        planetRecycler = fragment.findViewById(R.id.planet_recycler);
        planetRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));

        adapter = new PlanetRecyclerAdapter(getActivity(), new ArrayList<>());
        setOnClick();
        planetRecycler.setAdapter(adapter);
        search();

        return fragment;
    }

    private void setOnClick() {
        adapter.setOnItemClickListener((view, position) -> {
            Planet p = adapter.getItem(position);
            User user = (User) StatusStoreService.getInstance().get("user");
            if (!p.getMemberIds().contains(user.getId())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Hint");
                builder.setMessage("You haven't joined the planet yet. Are you sure to join?");
                builder.setPositiveButton("Confirm", (dialog, which) -> {
                    p.getMemberIds().add(user.getId());
                    ServiceRegister.PlanetServiceInstance.addOrUpdate(p);
                    // start the PlanetDetailActivity
                    Intent intent = new Intent(getActivity(), PlanetDetailActivity.class);
                    intent.putExtra("planet", new Gson().toJson(p));
                    startActivity(intent);
                    dialog.dismiss();
                });
                builder.setNeutralButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            } else {
                // if the current user has joined the planet, start the PlanetDetailActivity
                Intent intent = new Intent(getActivity(), PlanetDetailActivity.class);
                intent.putExtra("planet", new Gson().toJson(p));
                startActivity(intent);
            }
        });
        // set the click event of searching
        searchBtn.setOnClickListener(v -> search());
        newBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PlanetEditActivity.class);
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
        ServiceRegister.PlanetServiceInstance
                .findLikeField("name", content)
                .observe(this, planets -> {
            progress.setProgress(60);
            adapter.changeData(planets);
            progress.setProgress(100);
            progress.dismiss();
        });
    }
}