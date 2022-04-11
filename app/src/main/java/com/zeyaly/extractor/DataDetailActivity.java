package com.zeyaly.extractor;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.zeyaly.extractor.database.DatabaseHandler;
import com.zeyaly.extractor.databinding.ContentLayoutBinding;
import com.zeyaly.extractor.model.PhotoModel;
import com.zeyaly.extractor.utils.TransistionAnimation;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DataDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ContentLayoutBinding binding;
    DatabaseHandler databaseHandler;
    ArrayList<PhotoModel> photoModelArray;
    int Pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.content_layout);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        TransistionAnimation transistionAnimation = new TransistionAnimation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(transistionAnimation.enterTransition());
            getWindow().setSharedElementReturnTransition(transistionAnimation.returnTransition());
        }
        if (getIntent().getExtras() != null) {
            Bundle args = getIntent().getBundleExtra("BUNDLE");
            photoModelArray = (ArrayList<PhotoModel>) args.getSerializable("ARRAYLIST");
            Pos = args.getInt("Postion");
            // photoModelArray = (ArrayList<PhotoModel>) getIntent().getExtras().getSerializable("List");


            System.out.println("plan id--------->" + photoModelArray.get(Pos).getTextValue() + "product--------->" + photoModelArray.get(Pos).getTitle());
        }
        intView();

    }

    private void intView() {
        binding.title.setText(photoModelArray.get(Pos).getTitle());
        binding.textContent.setText(photoModelArray.get(Pos).getTextValue());
        binding.cameraImg.setImageBitmap(getImage(photoModelArray.get(Pos).getByteBuffer()));
        databaseHandler = new DatabaseHandler(this);
        binding.textContent.setEnabled(false);
        binding.editLayout.setOnClickListener(this);
        binding.shareLayout.setOnClickListener(this);
        binding.saveLayout.setOnClickListener(this);
        binding.copyLayout.setOnClickListener(this);
        binding.backArrow.setOnClickListener(this);
        binding.copy.setOnClickListener(this);
        binding.nex.setOnClickListener(this);
        binding.textContent.setEnabled(true);
        binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle_blue));
        binding.editImg.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareLayout:
                shareIntent("ZeyalyNotes", binding.textContent.getText().toString());
                break;
            case R.id.backArrow:
                finish();
                break;
            case R.id.editLayout:
                if (binding.textContent.isEnabled()) {
                    binding.textContent.setEnabled(false);
                    binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle));
                    binding.editImg.setColorFilter(ContextCompat.getColor(this, R.color.txt_medium_color), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    binding.textContent.setEnabled(true);
                    binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                    binding.editImg.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                break;
            case R.id.saveLayout:
                PhotoModel photoModel = new PhotoModel();
                photoModel.setTitle(binding.title.getText().toString());
                photoModel.setTextValue(binding.textContent.getText().toString());
                // photoModelArray.setByteBuffer(getBytes(bitmap));
                databaseHandler.update(photoModel, photoModelArray.get(Pos).getId());
                Toast.makeText(this, "Saved Sucessfully", Toast.LENGTH_SHORT).show();
                break;

            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", binding.textContent.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied Sucessfully", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void shareIntent(String sFileName, String sBody) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sFileName);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sBody);
        startActivity(Intent.createChooser(sharingIntent, ""));
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
