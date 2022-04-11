package com.zeyaly.extractor.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.vision.text.TextRecognizer;
import com.zeyaly.extractor.DataDetailActivity;
import com.zeyaly.extractor.R;
import com.zeyaly.extractor.adapter.DataAdapter;
import com.zeyaly.extractor.database.DatabaseHandler;
import com.zeyaly.extractor.databinding.DataLayoutBinding;
import com.zeyaly.extractor.model.PhotoModel;
import com.zeyaly.extractor.onItemClickListner.RecyclerTouchListener;

import java.io.Serializable;
import java.util.ArrayList;

public class DataFragment extends Fragment {
    DataLayoutBinding binding;
    DatabaseHandler databaseHandler;
    ArrayList<PhotoModel> photoModelArrayList;
    DataAdapter dataAdapter;
    Boolean isFirst = false;
    Dialog dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View view = inflater.inflate(R.layout.setting_layout, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.data_layout, container, false);
        View view = binding.getRoot();

        initView();
        return view;
    }

    private void initView() {
        databaseHandler = new DatabaseHandler(getActivity());
        dataAdd();
        recyclerViewClickListner();
        isFirst = true;
    }

    private void dataAdd() {
        photoModelArrayList = new ArrayList<>();
        photoModelArrayList.addAll(databaseHandler.all());
        binding.RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        // recent_product_RecyclerView.setLayoutManager(mLayoutManager);
        if (photoModelArrayList.size() > 0) {
            binding.RecyclerView.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);
            dataAdapter = new DataAdapter(getActivity(), photoModelArrayList);
            binding.RecyclerView.setAdapter(dataAdapter);
        } else {
            binding.RecyclerView.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }
    }

    private void recyclerViewClickListner() {
        binding.RecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), binding.RecyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), DataDetailActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST", (Serializable) photoModelArrayList);
                        args.putInt("Postion", position);
                        intent.putExtra("BUNDLE", args);
                        startActivity(intent);

                    }


                    @Override
                    public void onLongClick(View view, int position) {
                        dialogAlert(position);

                    }
                }));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst) {
            dataAdd();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        dataAdd();
    }

    private void dialogAlert(final int position) {
        dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_delete);

        TextView no = dialog.findViewById(R.id.close);
        TextView yes = dialog.findViewById(R.id.save);

        dialog.show();

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DeleteCard(position);

                // ArrayList<Navigation_Drawer_Item_Bean> navigationitemtemp=new ArrayList<>();

            }
        });

    }

    private void DeleteCard(int pos) {
        databaseHandler.remove(photoModelArrayList.get(pos).getId());
        dataAdd();

    }
}
