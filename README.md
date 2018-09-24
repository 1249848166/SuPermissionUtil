# SuPermissionUtil
## 1.添加依赖
```java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```java
dependencies {
	        implementation 'com.github.1249848166:SuPermissionUtil:1.0'
	}
```
## 2.使用
```java
//声明变量
final int REQUEST_CAMERA = 1;//用来分辨哪一个动作（比如：获取头像（里面可能包含摄像机，存储等多个权限申请））
SuPermissionUtil suPermissionUtil;
ImageView imageView;
//初始化
suPermissionUtil = new SuPermissionUtil();
imageView = findViewById(R.id.img);
//在需要权限的地方调用这个，在最后一个参数的回调中写获得权限后的代码（比如获得相机权限后，打开相机拍照）
findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        suPermissionUtil.requestPermission(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA, new SuPermissionUtil.Callback() {
            @Override
            public void onResult(int requestCode) {
                //打开相机拍照
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
//在activity的权限回调中注册工具
 @Override
 public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     suPermissionUtil.onRequestPermissionsResult(REQUEST_CAMERA);//注册系统权限获取回调
 }
 //在activity返回中获取照片（optional，当你是拍照的时候）
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
```
