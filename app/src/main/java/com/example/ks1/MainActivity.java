package com.example.ks1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    String TAG="xushitao";


    Bitmap bitmap=null;
    int REQUEST_MEDIA_PROJECTION=2;
    int mScreenDensity=480;

    MediaProjectionManager mMediaProjectionManager;
    ImageReader mImageReader;

    MediaProjection mMediaProjection;
    VirtualDisplay mVirtualDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button=(Button) findViewById(R.id.button_1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent=new Intent("WebViewTest");
//                startActivity(intent);

                mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 2:
                Log.d("xushitao", "onActivityResult: 22222222223333333333");
                if (resultCode == RESULT_OK && data!=null) {
                    ScreenShot(data);
                }

        }
    }


    public Bitmap ScreenShot(Intent data){//其中一些标识符是类的成员变量

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);


        int screenWidth = outMetrics.widthPixels;
        int screenHeight = outMetrics.heightPixels;

        mMediaProjection = mMediaProjectionManager.getMediaProjection(RESULT_OK, data);
        mImageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 3); //ImageFormat.RGB_565
//        mImageReader = ImageReader.newInstance(screenWidth, screenHeight, ImageFormat.FLEX_RGBA_8888, 3); //ImageFormat.RGB_565
        if(mImageReader == null){ // 最后一帧如果被获取过，间隔短的话，后一帧可能还没生成。
            Log.d(TAG, "onActivityResult: mImageReader没有图片1");
            return null;
        }
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",screenWidth, screenWidth,
                mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();//获取下一帧截屏，这里可以控制你是否要单个或者直接录屏
                if (image == null) {
                    Log.d("xushitao", "onImageAvailable: 空");
                } else {

                    int width = image.getWidth();
                    int height = image.getHeight();
//                         Log.d("xushitao", "onActivityResult: width" + width);
//                         Log.d("xushitao", "onActivityResult: height" + height);
                    Log.d("xushitao", "onImageAvailable: 不空");
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0 ].getRowStride();
                    int rowPadding = rowStride - pixelStride * width;
                    bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);

                    ImageView pi=(ImageView)findViewById(R.id.picture);
                    pi.setImageBitmap(bitmap);
                    image.close();
                    Log.d(TAG, "onImageAvailable: 不空1");
                    mImageReader.close();
                    return;
                }
            }
        },null);
        Log.d(TAG, "onActivityResult: end");
        return bitmap;

    }
}