package com.example.opencv2;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "MainActivity";
    CameraBridgeViewBase mCameraBridgeViewBase;
    int counter = 0;

    BaseLoaderCallback mBaseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);

            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    mCameraBridgeViewBase.enableView();
                    break;
                default:
                    super.onManagerConnected(status);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mCameraBridgeViewBase = findViewById(R.id.cameraView);
        mCameraBridgeViewBase.setVisibility(View.VISIBLE);
        mCameraBridgeViewBase.setCvCameraViewListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }


    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat frame = inputFrame.rgba();
        if (counter % 2 == 0) {
//            Core.flip(frame, frame, 1);
//            Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2GRAY);
        }

        counter++;
        return frame;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "There is a problem", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mBaseLoaderCallback);
        } else {
            mBaseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraBridgeViewBase != null) {
            mCameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraBridgeViewBase != null) {
            mCameraBridgeViewBase.disableView();
        }
    }


    public static Mat rotate(Mat src, double angle)
    {
        Mat dst = new Mat();
        if(angle == 180 || angle == -180) {
            Core.flip(src, dst, -1);
        } else if(angle == 90 || angle == -270) {
            Core.flip(src.t(), dst, 1);
        } else if(angle == 270 || angle == -90) {
            Core.flip(src.t(), dst, 0);
        }

        return dst;
    }

}