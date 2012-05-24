# Copyright 2012 (C) JIke Inc.
# Author:jiangyongbing<jiangyongbing@jike.com>

namespace cpp appsearch
namespace php appsearch
namespace java appsearch

enum ImgType {
  ICON = 0,
  DETAIL_IMAGE = 1,
}
service ApkTfsService {
  //------------------------------apk interface-------------------------
  // Get application package by apk_key.
  string ReadApk(1:string apk_key);
  
  // write apk to cassandra, return apk key
  string WriteApk(1:string apk_data);

  // check if cass_key exists, if exists, return cass_key.
  // else return empty string
  string IsApkExists(1:string apt_str);

  void WriteApkAsync(1:string apk_key, 2:string apk_data);

  //------------------------------tfs interface-------------------------
  // write image (icon or detailimage) whose data is image_data with url image-url.
  // return the vector<string> tfs-keys
  list<string> WriteImage(1:string image_data,
      2:string image_url,
      3:ImgType img_type);

  // return image data with tfs-key tfs_key.
  string ReadImage(1:string tfs_key);

  // remove image in tfs with tfs-key=tfs_key
  // if success, return true, otherwise false.
  bool Remove(1:string tfs_key);

  // check if image url is exist
  // if exist, return the image tfs-key, else return empty string.
  string IsImageUrlExists(1:string img_url);

  // push image_url and image_tfs-key to duplication db
  bool PushImageTfsKey(1:string img_url, 2:string image_tfskey);
}
