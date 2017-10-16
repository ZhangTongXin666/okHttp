package utils;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * @author : 老头儿
 * @email : 527672827@qq.com
 * @org : 河北北方学院 移动开发工程部 C508
 * @function : （功能） 图片工具
 */
public class ImageUtils {

    /**
     * 根据InputSream获得图片的实际的宽度和高度
     * @param imageStream
     * @return
     */
    public static ImageSize getImageSize(InputStream imageStream){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    private static class ImageSize{
        int width;
        int height;

        public ImageSize(){}

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static ImageSize getImageViewSize(View view){
        ImageSize imageSize = new ImageSize();
        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);
        return imageSize;
    }

    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null) return 0;
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT){
            width = view.getWidth();//获得实际的宽度
        }
        if (width <= 0 && params != null){
            width = params.width;
        }
        if (width <= 0){
            width = getImageViewFieldValue(view, "mMaxWidth");//获得设置的最大宽度
        }
        if (width <= 0){
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }
        return width;
    }

    private static int getExpectHeight(View view) {
        int height = 0;
        if (view == null) return 0;
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT){
            height = view.getHeight();//获得实际的高度
        }
        if (height <= 0 && params != null){
            height = params.height;
        }
        if (height <= 0){
            height = getImageViewFieldValue(view, "mMaxHeight");//获得设置的最大高度
        }
        if(height <= 0){ // 使用屏幕的高度
            DisplayMetrics displayMetrics = view.getContext().getResources()
                    .getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }
        return height;
    }

    /**
     * 通过反射获取ImageView的某个属性值
     * @param view
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(View view, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            int fieldValue = field.getInt(view);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE){
                value = fieldValue;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return value;
    }

}
