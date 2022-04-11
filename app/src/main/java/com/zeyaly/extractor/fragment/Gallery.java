package com.zeyaly.extractor.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.zeyaly.extractor.BuildConfig;
import com.zeyaly.extractor.R;
import com.zeyaly.extractor.database.DatabaseHandler;
import com.zeyaly.extractor.databinding.CameraLayoutBinding;
import com.zeyaly.extractor.databinding.GalleryLayoutBinding;
import com.zeyaly.extractor.databinding.WelcomescreenBinding;
import com.zeyaly.extractor.model.PhotoModel;
import com.zeyaly.extractor.session.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;

public class Gallery extends Fragment implements View.OnClickListener {

    public Gallery() {

    }

    private static final int SELECT_FILE = 12;
    private static final String LOG_TAG = "Text API";
    Boolean isFirst = false;
    GalleryLayoutBinding binding;
    private Uri imageUri;
    private TextRecognizer detector;
    DatabaseHandler databaseHandler;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    Bitmap bitmap = null;
    Session session;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // View view = inflater.inflate(R.layout.setting_layout, container, false);
        binding = DataBindingUtil.inflate(
                inflater, R.layout.gallery_layout, container, false);
        View view = binding.getRoot();
        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));

            binding.textContent.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        detector = new TextRecognizer.Builder(getContext()).build();
        initView();
        return view;
    }


    private void initView() {
        session = new Session(getActivity());
        databaseHandler = new DatabaseHandler(getContext());
        binding.textContent.setEnabled(false);
        binding.nextLayout.setOnClickListener(this);
        binding.editLayout.setOnClickListener(this);
        binding.shareLayout.setOnClickListener(this);
        binding.saveLayout.setOnClickListener(this);
        binding.copyLayouts.setOnClickListener(this);
        binding.nex.setOnClickListener(this);
        binding.backArrow.setOnClickListener(this);
        isFirst = true;
        binding.textContent.setEnabled(true);
        binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle_blue));
        binding.editImg.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private void galleryIntent() {
        binding.textContent.setText("");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareLayout:
                shareIntent("ZeyalyNotes", binding.textContent.getText().toString());
                break;
            case R.id.editLayout:
                if (binding.textContent.isEnabled()) {
                    binding.textContent.setEnabled(false);
                    binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle));
                    binding.editImg.setColorFilter(ContextCompat.getColor(getActivity(), R.color.txt_medium_color), android.graphics.PorterDuff.Mode.SRC_IN);
                } else {
                    binding.textContent.setEnabled(true);
                    binding.editLayout.setBackground(getResources().getDrawable(R.drawable.circle_blue));
                    binding.editImg.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                }
                break;
            case R.id.saveLayout:
                // int value=Integer.parseInt(session.getKEYPoints());
                //  if(value>0) {
                PhotoModel photoModel = new PhotoModel();
                photoModel.setTitle(binding.title.getText().toString());
                photoModel.setTextValue(binding.textContent.getText().toString());
                photoModel.setByteBuffer(getBytes(getResizedBitmap(bitmap, 500)));
                databaseHandler.add(photoModel);
                Toast.makeText(getActivity(), "Saved Sucessfully", Toast.LENGTH_SHORT).show();
               /* }else {
                    Toast.makeText(getActivity(), "Watch video and Earn Points", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.backArrow:
                binding.beforeLayout.setVisibility(View.VISIBLE);
                binding.afterLayout.setVisibility(View.GONE);
                binding.title.setText("");
                break;
            case R.id.copyLayouts:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", binding.textContent.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Copied Sucessfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nextLayout:
                int value = Integer.parseInt(session.getKEYPoints());
                if (value > 0) {
                    galleryIntent();
                } else {
                    Toast.makeText(getActivity(), "Watch video and Earn Points", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {

                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                        binding.cameraImg.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> textBlocks = detector.detect(frame);
                    String blocks = "";
                    String lines = "";
                    String words = "";
                    for (int index = 0; index < textBlocks.size(); index++) {
                        //extract scanned text blocks here
                        TextBlock tBlock = textBlocks.valueAt(index);
                        blocks = blocks + tBlock.getValue() + "\n" + "\n";
                        for (Text line : tBlock.getComponents()) {
                            //extract scanned text lines here
                            lines = lines + line.getValue() + "\n";
                            for (Text element : line.getComponents()) {
                                //extract scanned text words here
                                words = words + element.getValue() + ", ";
                            }
                        }
                    }
                    if (textBlocks.size() == 0) {
                        binding.textContent.setText("Scan Failed: Found nothing to scan");
                    } else {
                        int valuse = Integer.parseInt(session.getKEYPoints()) - 10;
                        session.setKEYPoints(valuse + "");
                        binding.beforeLayout.setVisibility(View.GONE);
                        binding.afterLayout.setVisibility(View.VISIBLE);
                       /* binding.textContent.setText(binding.textContent.getText() + "Blocks: " + "\n");
                        binding.textContent.setText(binding.textContent.getText() + blocks + "\n");
                        binding.textContent.setText(binding.textContent.getText() + "---------" + "\n");*/
                        // binding.textContent.setText(binding.textContent.getText() + "Lines: " + "\n\n");
                        binding.textContent.setText(binding.textContent.getText() + lines + "\n");
                        binding.textContent.setText(binding.textContent.getText() + "---------" + "\n");
                       /* binding.textContent.setText(binding.textContent.getText() + "Words: " + "\n");
                        binding.textContent.setText(binding.textContent.getText() + words + "\n");
                        binding.textContent.setText(binding.textContent.getText() + "---------" + "\n");*/
                        //  generateNoteOnSD(this,"ZeyalyNotes",lines);
                    }

                }
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
                Log.e(LOG_TAG, e.toString());
            }

        }
    }

    private void shareIntent(String sFileName, String sBody) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, sFileName);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sBody);
        startActivity(Intent.createChooser(sharingIntent, ""));
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirst) {
            binding.title.setText("");
            binding.beforeLayout.setVisibility(View.VISIBLE);
            binding.afterLayout.setVisibility(View.GONE);

        }

    }
}

