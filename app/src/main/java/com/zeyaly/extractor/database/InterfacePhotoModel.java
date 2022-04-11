package com.zeyaly.extractor.database;


import com.zeyaly.extractor.model.PhotoModel;

import java.util.List;

/**
 * Created by jaimenejaim on 23/08/17.
 */

public interface InterfacePhotoModel {

    void add(PhotoModel photoModel);
    PhotoModel get(int index);
    List<PhotoModel> all();
    void remove(int index);
    void update(PhotoModel photoModel,int index);

}
