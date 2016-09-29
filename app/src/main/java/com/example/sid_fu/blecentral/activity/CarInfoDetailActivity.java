package com.example.sid_fu.blecentral.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sid_fu.blecentral.App;
import com.example.sid_fu.blecentral.HomeActivity;
import com.example.sid_fu.blecentral.R;
import com.example.sid_fu.blecentral.db.DbHelper;
import com.example.sid_fu.blecentral.db.dao.DeviceDao;
import com.example.sid_fu.blecentral.db.dao.UserDao;
import com.example.sid_fu.blecentral.db.entity.CarBrand;
import com.example.sid_fu.blecentral.db.entity.Device;
import com.example.sid_fu.blecentral.db.entity.User;
import com.example.sid_fu.blecentral.ui.activity.BaseActionBarActivity;
import com.example.sid_fu.blecentral.utils.BitmapUtils;
import com.example.sid_fu.blecentral.utils.Constants;
import com.example.sid_fu.blecentral.utils.Logger;
import com.example.sid_fu.blecentral.utils.SharedPreferences;
import com.example.sid_fu.blecentral.utils.ToastUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sid-fu on 2016/5/17.
 */
public class CarInfoDetailActivity extends BaseActionBarActivity {

    public static final int DETAILS = 10001;
    @Bind(R.id.img_photo)
    ImageView imgPhoto;
    @Bind(R.id.tv_carname)
    TextView tvCarname;
    @Bind(R.id.tv_cartype)
    TextView tvCartype;
    @Bind(R.id.tv_displacement)
    TextView tvDisplacement;
    @Bind(R.id.tv_releaseTime)
    TextView tvReleaseTime;
    @Bind(R.id.tv_weight)
    TextView tvWeight;
    @Bind(R.id.tv_derailleur)
    TextView tvDerailleur;
    @Bind(R.id.tv_gears)
    TextView tvGears;
    @Bind(R.id.tv_ftyre)
    TextView tvFtyre;
    @Bind(R.id.tv_btyre)
    TextView tvBtyre;
    @Bind(R.id.btn_add)
    Button btnAdd;
    @Bind(R.id.edf_min)
    EditText edfMin;
    @Bind(R.id.edf_mid)
    EditText edfMid;
    @Bind(R.id.edf_max)
    EditText edfMax;
    @Bind(R.id.edb_min)
    EditText edbMin;
    @Bind(R.id.edb_mid)
    EditText edbMid;
    @Bind(R.id.edb_max)
    EditText edbMax;
    private SQLiteDatabase database;
    private CarInfoDetailActivity mContext;
    private int newDataId;
    private int id;
    private int state;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(SharedPreferences.getInstance().getString(Constants.LANDORPORT,Constants.DEFIED).equals("横屏"))
//        {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
//        }else{
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
//        }
        setContentView(R.layout.aty_car_info);
        ButterKnife.bind(this);
         /*显示App icon左侧的back键*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mContext = CarInfoDetailActivity.this;
        App.getInstance().addActivity(this);
        intitDatas();
        intView();

        App.getInstance().speak("请选择轮胎型号");
    }

    private void intView() {

    }

    private void intitDatas() {
        id = getIntent().getExtras().getInt("id");
        state = getIntent().getExtras().getInt("state");
        if (state == DETAILS) {
            btnAdd.setText("更新");
        }
        Logger.e("ID:" + id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 打开数据库，database是在Main类中定义的一个SQLiteDatabase类型的变量
                Message msg = new Message();
                msg.what = 0;
                if (state == DETAILS) {
                    msg.obj = DbHelper.getInstance(mContext).getUserCarInfo(id);
                } else {
                    msg.obj = DbHelper.getInstance(mContext).getCarInfo(id);
                }

                handler.sendMessage(msg);
            }
        }).start();
    }

    private CarBrand carBrand;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                carBrand = (CarBrand) msg.obj;
                tvDisplacement.setText(carBrand.getDisplacement());
                tvCarname.setText(carBrand.getBrandName());
                tvCartype.setText(carBrand.getCname());
                tvReleaseTime.setText(carBrand.getReleaseTime());
                tvWeight.setText(carBrand.getWeight());
                tvDerailleur.setText(carBrand.getDerailleur());
                tvGears.setText(carBrand.getGears());
                if (carBrand.getFtyre() != null)
                    tvFtyre.setText(carBrand.getFtyre());
                if (carBrand.getBtyre() != null)
                    tvBtyre.setText(carBrand.getBtyre());
                imgPhoto.setImageBitmap(BitmapUtils.getImageFromAssetsFile(mContext,"logo/"+carBrand.getEname()+".png"));
            }
        }
    };

    @OnClick(R.id.btn_add)
    public void addDate() {
        if (btnAdd.getText().equals("保存")) {
            if(Float.valueOf(edbMin.getText().toString())<Float.valueOf(edbMid.getText().toString())&&
                    Float.valueOf(edbMid.getText().toString())<Float.valueOf(edbMax.getText().toString())&&
                    Float.valueOf(edbMin.getText().toString())<Float.valueOf(edbMax.getText().toString())&&
                    Float.valueOf(edfMin.getText().toString())<Float.valueOf(edfMid.getText().toString())&&
                    Float.valueOf(edfMid.getText().toString())<Float.valueOf(edfMax.getText().toString())&&
                    Float.valueOf(edfMin.getText().toString())<Float.valueOf(edfMax.getText().toString()))
            {
                if (carBrand != null)
                    newDataId = DbHelper.getInstance(this).saveCarInfo(carBrand);
                if (newDataId > 0) {
                    new AlertDialog.Builder(CarInfoDetailActivity.this).setTitle("系统提示")//设置对话框标题
                            .setMessage("保存成功，是否开始配对，如果是\n" +
                                    "，请将手机贴近左前轮气嘴位置处\n" +
                                    "，点击开始配对；如果不想进行\n" +
                                    "配对，请点击返回首页，下次进\n" +
                                    "行配对。！")//设置显示的内容
                            .setPositiveButton("开始配对", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    gotoConfig();
                                }
                            }).setNegativeButton("返回首页", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                            Logger.e(" 请保存数据！");
                            gotoHome();
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
//                App.getInstance().speak("保存成功，是否开始配对，如果是，请将手机贴近左前轮气嘴位置处，点击开始配对；如果不想进行配对，请点击返回首页，下次进行配对！");
            }else
            {
                ToastUtil.show("胎压推荐值有错误");
            }
        } else {
            if (carBrand != null)
                DbHelper.getInstance(this).updateCarInfo(carBrand, id);
//            App.getInstance().speak("更新成功");
        }
    }

    private void gotoHome() {
        User u = new User();
        u.setName("test_user");
        new UserDao(this).add(u);
        Device deviceDate = new Device();
        deviceDate.setCarID(newDataId);
        deviceDate.setDeviceName(carBrand.getBrandName());
        deviceDate.setDeviceDescripe(carBrand.getSeriesName());
        deviceDate.setImagePath(carBrand.getEname());
        deviceDate.setBackMidValues(edbMid.getText().toString());
        deviceDate.setBackMinValues(edbMin.getText().toString());
        deviceDate.setBackmMaxValues(edbMax.getText().toString());
        deviceDate.setFromMidValues(edfMid.getText().toString());
        deviceDate.setFromMinValues(edfMin.getText().toString());
        deviceDate.setFromMaxValues(edfMax.getText().toString());
        deviceDate.setUser(u);
        boolean firstTimeUse = SharedPreferences.getInstance().getBoolean(Constants.FIRST_CONFIG, false);
        if(!firstTimeUse) {
            deviceDate.setIsDefult("true");
        }
        new DeviceDao(mContext).add(deviceDate);
        App.getInstance().exit();
//        App.getInstance().speak("返回首页");
        if(!firstTimeUse) {
            Intent intent = new Intent();
            intent.setClass(CarInfoDetailActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        SharedPreferences.getInstance().putBoolean(Constants.FIRST_CONFIG,true);
    }

    private void gotoConfig() {
//        User u = new User();
//        u.setName("test_user");
//        new UserDao(this).add(u);
        Device deviceDate = new Device();
        deviceDate.setCarID(newDataId);
        deviceDate.setDeviceName(carBrand.getBrandName());
        deviceDate.setDeviceDescripe(carBrand.getSeriesName());
        deviceDate.setImagePath(carBrand.getEname());
        deviceDate.setBackMidValues(edbMid.getText().toString());
        deviceDate.setBackMinValues(edbMin.getText().toString());
        deviceDate.setBackmMaxValues(edbMax.getText().toString());
        deviceDate.setFromMidValues(edfMid.getText().toString());
        deviceDate.setFromMinValues(edfMin.getText().toString());
        deviceDate.setFromMaxValues(edfMax.getText().toString());
        boolean firstTimeUse = SharedPreferences.getInstance().getBoolean(Constants.FIRST_CONFIG, false);
        if(!firstTimeUse) {
            deviceDate.setIsDefult("true");
        }
        Intent intent = new Intent();
        intent.setClass(CarInfoDetailActivity.this, ConfigDevice.class);
        intent.putExtra("device", deviceDate);
        startActivity(intent);
        //finish();
        App.getInstance().exit();
        App.getInstance().speak("开始配置蓝牙模块");
    }

    @OnClick(R.id.tv_btyre)
    public void onBtyre() {
        Intent intent = new Intent(CarInfoDetailActivity.this, CarLunTaiTypeActivity.class);
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.tv_ftyre)
    public void onFtyre() {
        Intent intent = new Intent(CarInfoDetailActivity.this, CarLunTaiTypeActivity.class);
        startActivityForResult(intent, 1002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1001:
                if (resultCode == 100) {
                    tvBtyre.setText(data.getStringExtra("car_tire"));
                    carBrand.setBtyre(data.getStringExtra("car_tire"));
//                    App.getInstance().speak("您选择的轮胎型号是" + data.getStringExtra("car_tire"));
                }
                break;
            case 1002:
                if (resultCode == 100) {
                    tvFtyre.setText(data.getStringExtra("car_tire"));
                    carBrand.setFtyre(data.getStringExtra("car_tire"));
//                    App.getInstance().speak("您选择的轮胎型号是" + data.getStringExtra("car_tire"));
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap getDiskBitmap(String pathString) {
        Logger.e(pathString);
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }


        return bitmap;
    }
}
