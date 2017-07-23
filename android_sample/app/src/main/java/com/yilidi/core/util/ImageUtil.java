package com.yilidi.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageUtil {
  public static Bitmap handlOOMException(String imagePath) {
    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inJustDecodeBounds = true;

    BitmapFactory.decodeFile(imagePath, opts);

    opts.inSampleSize = computeSampleSize(opts, -1, 600 * 360);
    // 这里一定要将其设置回false，因为之前我们将其设置成了true
    opts.inJustDecodeBounds = false;
    try {
      Bitmap bmp = BitmapFactory.decodeFile(imagePath, opts);

      return bmp;
    } catch (OutOfMemoryError err) {
      err.printStackTrace();
    }
    return null;
  }

  public static int computeSampleSize(BitmapFactory.Options options,
                                      int minSideLength, int maxNumOfPixels) {
    int initialSize = computeInitialSampleSize(options, minSideLength,
            maxNumOfPixels);

    int roundedSize;
    if (initialSize <= 8) {
      roundedSize = 1;
      while (roundedSize < initialSize) {
        roundedSize <<= 1;
      }
    } else {
      roundedSize = (initialSize + 7) / 8 * 8;
    }

    return roundedSize;
  }

  private static int computeInitialSampleSize(BitmapFactory.Options options,
                                              int minSideLength, int maxNumOfPixels) {
    double w = options.outWidth;
    double h = options.outHeight;

    int lowerBound = (maxNumOfPixels == -1) ? 1 :
            (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
    int upperBound = (minSideLength == -1) ? 128 :
            (int) Math.min(Math.floor(w / minSideLength),
                    Math.floor(h / minSideLength));

    if (upperBound < lowerBound) {
      return lowerBound;
    }

    if ((maxNumOfPixels == -1) &&
            (minSideLength == -1)) {
      return 1;
    } else if (minSideLength == -1) {
      return lowerBound;
    } else {
      return upperBound;
    }
  }

  public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
    float width = bgimage.getWidth();
    float height = bgimage.getHeight();
    Matrix matrix = new Matrix();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    matrix.postScale(scaleWidth, scaleHeight);
    Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
            (int) height, matrix, true);
    return bitmap;
  }

  public static Bitmap comp(Bitmap image) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出	
//			baos.reset();//重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
//		}
    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    //开始读入图片，此时把options.inJustDecodeBounds 设回true了
    newOpts.inJustDecodeBounds = true;
    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;
    //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
    float hh = 600;//这里设置高度为800f
    float ww = 360;//这里设置宽度为480f
    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = 1;//be=1表示不缩放
    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
      be = (int) (newOpts.outWidth / ww);
    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
      be = (int) (newOpts.outHeight / hh);
    }
    if (be <= 0)
      be = 1;
    newOpts.inSampleSize = be;//设置缩放比例
    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    isBm = new ByteArrayInputStream(baos.toByteArray());
    bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
    return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
  }

  public static Bitmap compressImage(Bitmap image) {

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
    int options = 100;
    while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
      baos.reset();//重置baos即清空baos
      image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
      options -= 10;//每次都减少10
    }
    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
    return bitmap;
  }
}
