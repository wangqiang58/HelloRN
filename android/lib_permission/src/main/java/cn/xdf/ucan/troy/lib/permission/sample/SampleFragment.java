package cn.xdf.ucan.troy.lib.permission.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public class SampleFragment extends Fragment {
    private String[] mCameraPermissions = new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE"};

    public static SampleFragment newInstance() {
        return new SampleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.permission_fragment_sample, container, false);
        inflate.findViewById(R.id.fragment_rq_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDFPermission.requestPermissions(getActivity(), 0x3, mCameraPermissions);
            }
        });

        inflate.findViewById(R.id.fragment_rq_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XDFPermission.requestPermissions(getActivity(), 0x4, mCameraPermissions, new IPermissionResultCallBack2() {
                    @Override
                    public void onResult(int requestCode, boolean isAllGranted, List<Permission> permissions) {
                        Toast.makeText(getActivity(), "封装通用回调" + requestCode + "：是否全部授权：" + isAllGranted, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
        });
        return inflate;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getActivity(), "Fragment回调" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
