package cn.xdf.ucan.troy.lib.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import cn.xdf.ucan.troy.lib.permission.IPermissionResultCallBack2;
import cn.xdf.ucan.troy.lib.permission.IPermissionResultCallback;
import cn.xdf.ucan.troy.lib.permission.Permission;
import cn.xdf.ucan.troy.lib.permission.R;
import cn.xdf.ucan.troy.lib.permission.XDFPermission;


/**
 * @Description:
 * @author: jingzhenglun@xdf.cn
 * @date: 4/29/21
 */
public class SampleActivity extends AppCompatActivity {
    private String[] mCameraPermissions = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_activity_sample);

        findViewById(R.id.activity_rq_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDFPermission.requestPermissions(SampleActivity.this, 0x1, mCameraPermissions);
            }
        });

        findViewById(R.id.rq_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDFPermission.requestPermissions(SampleActivity.this, 0x1, mCameraPermissions, new IPermissionResultCallBack2() {
                    @Override
                    public void onResult(int requestCode, boolean isAllGranted, List<Permission> permissions) {
                        Toast.makeText(SampleActivity.this, "通用封装回调" + requestCode + "：是否全部授权：" + isAllGranted, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.content, SampleFragment.newInstance()).commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(SampleActivity.this, "Activity回调" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
