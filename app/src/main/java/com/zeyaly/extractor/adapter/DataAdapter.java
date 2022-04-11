package com.zeyaly.extractor.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zeyaly.extractor.R;
import com.zeyaly.extractor.database.DatabaseHandler;
import com.zeyaly.extractor.databinding.DataAdapterBinding;
import com.zeyaly.extractor.model.PhotoModel;

import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    public ArrayList<PhotoModel> mDataset;
    private Activity mContext;
    int row_index = 0;
    DataAdapterBinding binding = null;
    Dialog dialog;
    DatabaseHandler databaseHandler;

    public DataAdapter(Activity mContext, ArrayList<PhotoModel> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;

        databaseHandler = new DatabaseHandler(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.data_adapter, parent, false);

        return new ViewHolder(binding);

     /*   View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row_layout, parent, false);
        //   itemView.getLayoutParams().width = (int) (getScreenWidth() / 3);*/
        // return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        binding.text.setText(mDataset.get(position).getTitle());
        binding.textContent.setText(mDataset.get(position).getTextValue());
        binding.CloseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAlert(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(DataAdapterBinding binding) {
            super(binding.getRoot());


        }
    }

    private void dialogAlert(final int position) {
        dialog = new Dialog(mContext);
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
        databaseHandler.remove(mDataset.get(pos).getId());
    }

}



