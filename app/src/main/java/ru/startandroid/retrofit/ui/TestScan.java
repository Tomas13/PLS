package ru.startandroid.retrofit.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.baozi.Zxing.CaptureActivity;
import com.baozi.Zxing.ZXingConstants;


import ru.startandroid.retrofit.R;

public class TestScan extends AppCompatActivity{

    TextView textView;

    private static final String TAG = TestScan.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);

        textView = (TextView) findViewById(R.id.tvsca);

        Intent intent = new Intent(this, CaptureActivity.class);
        //是不是显示历史记录按钮
        intent.putExtra(ZXingConstants.ScanIsShowHistory,true);
        startActivityForResult(intent, ZXingConstants.ScanRequestCode);

//        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//        setContentView(mScannerView);                // Set the scanner view as the content view
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case ZXingConstants.ScanRequestCode:
                if(resultCode == ZXingConstants.ScanRequestCode){
                    /**
                     * 拿到解析完成的字符串
                     */
                    String result = data.getStringExtra(ZXingConstants.ScanResult);
                    textView.setText(result);
                }else if(resultCode == ZXingConstants.ScanHistoryResultCode){
                    /**
                     * 历史记录
                     */
                    String resultHistory = data.getStringExtra(ZXingConstants.ScanHistoryResult);
                    if(!TextUtils.isEmpty(resultHistory)){
                        //自己实现历史页面
//                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                    }
                }
                break;
        }
    }
}
