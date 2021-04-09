package com.vivek.wo.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurUtils {

    /**
     * 高斯模糊
     *
     * @param bitmap  图片对象 尽可能小 可先执行缩放图片操作
     * @param radius  模糊半径  0 < radius <= 25  值越大性能要求越高
     * @param context
     */
    public static void blurByRenderScript(Bitmap bitmap, int radius, Context context) {
        RenderScript rs = RenderScript.create(context);
        Allocation allocation = Allocation.createFromBitmap(rs, bitmap);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, allocation.getElement());
        blur.setInput(allocation);
        blur.setRadius(radius);
        blur.forEach(allocation);
        allocation.copyTo(bitmap);
        rs.destroy();
    }

    /**
     * 裁剪和缩放图片
     *
     * @param source 源对象
     * @param x      裁剪的开始x坐标
     * @param y      裁剪的开始y坐标
     * @param width  裁剪的像素宽度
     * @param height 裁剪的像素高度
     * @param scale  比例倍数
     * @return
     */
    public static Bitmap cropAndScaleBitmap(Bitmap source, int x, int y, int width, int height, int scale) {
        Bitmap bitmap = Bitmap.createBitmap(source, x, y, width, height);
        return scaleBitmap(bitmap, scale);
    }

    /**
     * 缩放图片
     *
     * @param source 比例倍数
     * @param scale
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap source, int scale) {
        Bitmap bitmap = Bitmap.createScaledBitmap(source, source.getWidth() / scale,
                source.getHeight() / scale, false);
        return bitmap;
    }
}
