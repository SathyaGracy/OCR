package com.zeyaly.extractor.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jaimenejaim on 23/08/17.
 */

public class PhotoModel implements Serializable {
    private int id;
    private byte[] byteBuffer;
    private Date created_at;
    private Uri uri;
    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTextValue() {
        return TextValue;
    }

    public void setTextValue(String textValue) {
        TextValue = textValue;
    }

    private String TextValue;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public byte[] getByteBuffer() {
        return byteBuffer;
    }
    public void setByteBuffer(byte[] byteBuffer) {
        this.byteBuffer = byteBuffer;
    }
    public Date getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    public Uri getUri() {
        return uri;
    }
    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
