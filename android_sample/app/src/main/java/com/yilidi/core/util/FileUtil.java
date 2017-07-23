package com.yilidi.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtil {

  private static String SDPATH;

  public String getSDPATH() {
    return SDPATH;
  }

  public FileUtil() {
    // TODO Auto-generated constructor stub
//		SDPATH="/Hazelnut";
    creatSDDir("");
  }

  public static boolean isSDCardValid() {
    if (Environment.getExternalStorageState()
            .equals(Environment.MEDIA_MOUNTED)) {
      SDPATH = Environment.getExternalStorageDirectory() + "/" + "Hazelnut";
      return true;
    } else {
      return false;
    }
  }

  //	在SD卡上创建文件
  public File creatFile(String fileName) throws IOException {
    File file = new File(SDPATH + fileName);
    file.createNewFile();
    return file;
  }

  //	在sd卡上创建目录
  public File creatSDDir(String dirName) {
    File dir = new File(SDPATH + dirName);
    dir.mkdir();
    return dir;
  }

  //	判断sd卡上是否存在文件
  public boolean isFileExist(String fileName) {
    File file = new File(SDPATH + fileName);
    return file.exists();
  }

  //	将一个inputstream内的数据写到sd卡中
  public File writeToSDFromInput(String path, String fileName, String sbuffer) {
    File file = null;
    OutputStream output = null;
    try {
      File f = creatSDDir("/" + path);
      System.out.println(f.getPath());
      file = creatFile("/" + path + "/" + fileName);
      output = new FileOutputStream(file);
      byte[] bytes = sbuffer.getBytes();
      output.write(bytes);
      output.flush();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public File imageToSDFromInput(String path, String fileName, Bitmap bm) {
    File file = null;
    OutputStream output = null;
    try {
      File f = creatSDDir("/" + path);
      if (!f.exists()) {
        f.mkdirs();
      }
      System.out.println(f.getPath());
      file = creatFile("/" + path + "/" + fileName);
      output = new FileOutputStream(file);
      bm.compress(Bitmap.CompressFormat.JPEG, 100, output);
      output.flush();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        output.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public static String saveMyBitmap(String bitName, Bitmap mBitmap) {

    String AppRootPath = new FileUtil().getSDPATH();
    String AppSmallTmpPicPath = AppRootPath + "/pics/tmp/";
    String smallPicName = AppSmallTmpPicPath + bitName;

    File f = new File(smallPicName);
    try {
      f.createNewFile();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    FileOutputStream fOut = null;
    try {
      fOut = new FileOutputStream(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
    try {
      fOut.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fOut.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return smallPicName;
  }

  public static void deleteFile(File file) {
    if (file.exists()) { // 判断文件是否存在
      if (file.isFile()) { // 判断是否是文件
        file.delete(); // delete()方法 你应该知道 是删除的意思;
      } else if (file.isDirectory()) { // 否则如果它是一个目录
        File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
        for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
          deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
        }
      }
      file.delete();
    } else {
      return;
    }
  }

}