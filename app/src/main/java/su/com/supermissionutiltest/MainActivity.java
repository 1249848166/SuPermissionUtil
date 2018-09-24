package su.com.supermissionutiltest;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import su.com.supermissionutil.SuPermissionUtil;

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CAMERA = 1;//用来分辨哪一个动作（比如：获取头像（里面可能包含摄像机，存储等多个权限申请））
    SuPermissionUtil suPermissionUtil;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        suPermissionUtil = new SuPermissionUtil();

        imageView = findViewById(R.id.img);
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suPermissionUtil.requestPermission(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA, new SuPermissionUtil.Callback() {
                    @Override
                    public void onResult(int requestCode) {
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            startActivityForResult(intent, REQUEST_CAMERA);
                        } else {
                            Toast.makeText(MainActivity.this, "没有存储卡", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        suPermissionUtil.onRequestPermissionsResult(REQUEST_CAMERA);//注册系统权限获取回调
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (data != null) {
                    Bitmap bitmap = null;
                    if (data.getData() != null)
                        bitmap = BitmapFactory.decodeFile(data.getData().getPath());
                    if (bitmap == null && data.getExtras() != null) {
                        bitmap = (Bitmap) data.getExtras().get("data");
                    }
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

    }
}
