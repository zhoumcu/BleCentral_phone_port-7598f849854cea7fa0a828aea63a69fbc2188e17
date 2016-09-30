package com.example.sid_fu.blecentral.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;

import com.example.sid_fu.blecentral.App;
import com.example.sid_fu.blecentral.BluetoothLeService;
import com.example.sid_fu.blecentral.ManageDevice;
import com.example.sid_fu.blecentral.ParsedAd;
import com.example.sid_fu.blecentral.R;
import com.example.sid_fu.blecentral.ScanDeviceRunnable;
import com.example.sid_fu.blecentral.db.DbHelper;
import com.example.sid_fu.blecentral.db.entity.RecordData;
import com.example.sid_fu.blecentral.ui.BleData;
import com.example.sid_fu.blecentral.ui.frame.BaseFragment;
import com.example.sid_fu.blecentral.utils.Constants;
import com.example.sid_fu.blecentral.utils.DataUtils;
import com.example.sid_fu.blecentral.utils.DigitalTrans;
import com.example.sid_fu.blecentral.utils.Logger;
import com.example.sid_fu.blecentral.utils.SharedPreferences;
import com.example.sid_fu.blecentral.utils.ToastUtil;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MainPagerFragment extends BaseFragment {
    //国标10分钟
    private static final long DISTIME = 6000000;
    private int timeCount = 30;
    private SimpleDateFormat d;
    private String nowtime;
    private ScanDeviceRunnable leftFRunnable;
    private ScanDeviceRunnable rightFRunnable;
    private ScanDeviceRunnable leftBRunnable;
    private ScanDeviceRunnable rightBRunnable;
    private List<RecordData> recordDatas = new ArrayList<>();

    /**
     * 获取数据库上一次关闭前保存的胎压数据
     */
    @Override
    protected void initData() {
        recordDatas =  DbHelper.getInstance(getActivity()).getCarDataList(mActivity.deviceId);
        for (RecordData data :recordDatas) {
            bleStringToDouble(data);
        }
    }

    /**
     * 初始化 用于获取数据超时或者断开连接进行判断
     */
    @Override
    protected void initRunnable() {
        leftFRunnable = new ScanDeviceRunnable(mHandler,1001);
        rightFRunnable = new ScanDeviceRunnable(mHandler,1002);
        leftBRunnable = new ScanDeviceRunnable(mHandler,1003);
        rightBRunnable = new ScanDeviceRunnable(mHandler,1004);
    }

    @Override
    protected void initConfig() {
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        d= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
        nowtime=d.format(new Date());//按以上格式 将当前时间转换成字符串
        //1分钟报警重复提醒
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                mActivity.manageDevice.leftB_notify = false;
                mActivity.manageDevice.leftF_notify = false;
                mActivity.manageDevice.rightB_notify = false;
                mActivity.manageDevice.rightF_notify = false;
                mHandler.postDelayed(this, 60000);
                Logger.e("重复报警");
            }
        };
        mHandler.postDelayed(runnable, 60000);
    }

    private Handler  mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String testtime=d.format(new Date());//按以上格式 将当前时间转换成字符串
                    try {
                        long result=(d.parse(testtime).getTime()-d.parse(nowtime).getTime())/1000;
                        ToastUtil.show("扫描四个耗时：" + result+"秒");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case  1:
//                    showDialog("正在拼命扫描中。。。",mActivity.mDeviceList.size());
                    break;
                case 2:
                    disFind((Integer)msg.obj);
                    break;
            }
        }
    };

    private  class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            timeCount--;
            connect();
        }
    }
    /**
     * 旧方法
     */
    private void connect() {
        //30s扫描超时自动连接
        if(mActivity.mDeviceList.size()!=4&&timeCount<=0) {
            if(mActivity.mDeviceList.size()==0) {
//                mActivity.startScan();
            }
//            loadDialog.dismiss();
            timeCount = 30;
            App.getInstance().speak("蓝牙扫描超时，请确保已经添加了4个传感器，正在尝试新的扫描");
            Logger.i("未扫描到4个设备，且连接超时，断开扫描再重新扫描！");
        }else if(mActivity.mDeviceList.size()==4) {
//            loadDialog.dismiss();
//          App.getInstance().speak("扫描完毕");
        }else {

//            mHandler.sendEmptyMessage(1);
            Logger.i("扫描中。。。。。。"+mActivity.mDeviceList.size());
        }
    }

    /**
     *  Handles various events fired by the Service.
     * ACTION_GATT_CONNECTED: connected to a GATT server.
     * ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
     * ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
     * ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read or notification operations.
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_CHANGE_RESULT.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra("DEVICE_ADDRESS");
                int rssi = intent.getIntExtra("RSSI",0);
                byte[] scanRecord = intent.getByteArrayExtra("SCAN_RECORD");
                Logger.e(":"+DataUtils.bytesToHexString(scanRecord));
                ParsedAd ad = DataUtils.parseData(scanRecord);
                bleIsFind(device.getAddress(),"", 3.5f);
                bleStringToDouble(device,true,ad.datas);
                if(getActivity().getResources().getBoolean(R.bool.isShowRssi))
                    showRssi(device, rssi);
                Logger.e("收到广播数据");
            }else if(BluetoothLeService.ACTION_DISCONNECT_SCAN.equals(action)) {
                //解绑断开
                Logger.e("解绑断开");
            }
        }
    };

    /**
     * 注册广播
     * @return
     */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_CHANGE_RESULT);
        intentFilter.addAction(BluetoothLeService.ACTION_DISCONNECT_SCAN);
        return intentFilter;
    }

    /**
     * 扫描发现设备 UI界面状态改变
     * @param state
     */
    private void disFind(int state) {
        try{
            RecordData recordData = new RecordData();
            recordData.setData(null);
            recordData.setDeviceId(mActivity.deviceId);
            if (!SharedPreferences.getInstance().getBoolean(Constants.DAY_NIGHT,false)) {
                getDataTimeOutForDay(recordData,state);
            }else {
                getDataTimeOutForNight(recordData,state);
            }
        }catch (IllegalStateException e) {
            Logger.e("TIME_OUT",e.toString());
        }
    }

    private void bleStringToDouble(BluetoothDevice device, boolean isNotify,byte[] data) {
        float voltage = 0.00f,press = 0, temp =0;
        int temp1 = 0,state = 0,rssi = 0;
        DecimalFormat df = new DecimalFormat("######0.0");
        BleData bleData = new BleData();
        Logger.e(DigitalTrans.byte2hex(data));
        if(data==null) return;
        if(isNotify&&data.length==4) {
            press = ((float)DigitalTrans.byteToAlgorism(data[1])*160)/51/100;
            voltage = ((float)(DigitalTrans.byteToAlgorism(data[3])-31)*20/21+160)/100;
            temp = DigitalTrans.byteToAlgorism(data[2])-50;
//            state = DigitalTrans.byteToBin(data[0]);
            state = DigitalTrans.byteToBin0x0F(data[0]);
            if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Bar")) {

            }else if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Kpa")) {
                press = press*102;
            }else{
                press = press*14.5f;
            }

            if(SharedPreferences.getInstance().getString(Constants.TEMP_DW, "℃").equals("℃")) {
                temp1 = (int)temp;
            }else {
                temp1 = (int)(temp*1.8f)+32;
            }
        }else {
            if(data.length==5&Constants.IS_TEST) {
                voltage = ((float)(DigitalTrans.byteToAlgorism(data[0])-31)*20/21+160)/100;
                temp = DigitalTrans.byteToAlgorism(data[1])-50;
                press = ((float)DigitalTrans.byteToAlgorism(data[2])*160)/51/100;
                state = DigitalTrans.byteToBin0x0F(data[3]);
                rssi = 256-DigitalTrans.byteToAlgorism(data[4]);
                Logger.e("信号强度：-"+rssi);
            }else if(data.length==4) {
                voltage = ((float)(DigitalTrans.byteToAlgorism(data[0])-31)*20/21+160)/100;
                temp = DigitalTrans.byteToAlgorism(data[1])-50;
                press = ((float)DigitalTrans.byteToAlgorism(data[2])*160)/51/100;
                state = DigitalTrans.byteToBin0x0F(data[3]);
            }else if(data.length==2) {
//                broadcastUpdate(BluetoothLeService.ACTION_SEND_OK,device);
                return;
            }
        }
        Logger.e("状态："+state+"\n");
        Logger.e("压力值："+press+"\n");
        Logger.e("温度："+temp+"\n");
        Logger.e("电压"+voltage+"");

        bleData.setTemp (temp1);
        bleData.setPress(Float.valueOf(df.format(press)));
        bleData.setStatus(state);
        bleData.setVoltage(voltage);

        showDataForUI(device.getAddress(),bleData);

        if(device.getAddress().equals(mActivity.manageDevice.getLeftBDevice())) {
            handleException(bleData, mActivity.manageDevice.getLeftBDevice());
            //开启定时器用于监听数据
            if(mActivity.manageDevice.disLeftBCount==1) {
                mHandler.removeCallbacks(leftBRunnable);
                mActivity.manageDevice.disLeftBCount = 0;
            }
            mActivity.manageDevice.disLeftBCount++;
            mHandler.postDelayed(leftBRunnable, DISTIME);// 打开定时器，执行操作
        }else if(device.getAddress().equals(mActivity.manageDevice.getRightBDevice())) {
            handleException(bleData, mActivity.manageDevice.getRightBDevice());
            if(mActivity.manageDevice.disRightBCount==1) {
                mHandler.removeCallbacks(rightBRunnable);
                mActivity.manageDevice.disRightBCount = 0;
            }
            mActivity.manageDevice.disRightBCount++;
            mHandler.postDelayed(rightBRunnable, DISTIME);// 打开定时器，执行操作

        }else if(device.getAddress().equals(mActivity.manageDevice.getLeftFDevice())) {
            handleException(bleData, mActivity.manageDevice.getLeftFDevice());
            if(mActivity.manageDevice.disLeftFCount==1) {
                mHandler.removeCallbacks(leftFRunnable);
                mActivity.manageDevice.disLeftFCount = 0;
            }
            mActivity.manageDevice.disLeftFCount++;
            mHandler.postDelayed(leftFRunnable, DISTIME);// 打开定时器，执行操作

        }else if(device.getAddress().equals(mActivity.manageDevice.getRightFDevice())) {
            handleException(bleData, mActivity.manageDevice.getRightFDevice());
            if(mActivity.manageDevice.disRightFCount==1) {
                mHandler.removeCallbacks(rightFRunnable);
                mActivity.manageDevice.disRightFCount = 0;
            }
            mActivity.manageDevice.disRightFCount++;
            mHandler.postDelayed(rightFRunnable, DISTIME);// 打开定时器，执行操作
        }
        RecordData recordData = new RecordData();
        recordData.setName(device.getAddress());
        recordData.setData(DigitalTrans.byte2hex(data));
        recordData.setDeviceId(mActivity.deviceId);
        DbHelper.getInstance(getActivity()).update(mActivity.deviceId,device.getAddress(),recordData);
    }

    private void bleStringToDouble(RecordData recordData) {
        float voltage = 0.00f,press = 0, temp =0;
        int temp1 = 0,state = 0,rssi = 0;
        byte[] data;
        DecimalFormat df = new DecimalFormat("######0");
        BleData bleData = new BleData();
        data = DigitalTrans.hex2byte(recordData.getData());
        if(data==null) return;
        if(data.length==4) {
            voltage = ((float)(DigitalTrans.byteToAlgorism(data[3])-31)*20/21+160)/100;
            press = ((float)DigitalTrans.byteToAlgorism(data[1])*160)/51/100;
            temp = (float)(DigitalTrans.byteToAlgorism(data[2])-50);
            state = DigitalTrans.byteToBin0x0F(data[0]);
            if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Bar")) {

            }else if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Kpa")) {
                press = press*102;
            }else{
                press = press*14.5f;
            }

            if(SharedPreferences.getInstance().getString(Constants.TEMP_DW, "℃").equals("℃")) {
                temp1 = (int)temp;
            }else {
                temp1 = (int)(temp*1.8f)+32;
            }
        }
        Logger.e("状态："+state+"\n"+"压力值："+press+"\n"+"温度："+temp+"\n"+"电压"+voltage+"");
        bleData.setTemp (temp1);
        bleData.setPress(press);
        bleData.setStatus(state);
        bleData.setVoltage(voltage);

        showDataForUI(recordData.getName(),bleData);

        if(recordData.getName().equals(mActivity.manageDevice.getLeftBDevice())) {
            handleException(bleData, mActivity.manageDevice.getLeftBDevice());
        }else if(recordData.getName().equals(mActivity.manageDevice.getRightBDevice())) {
            handleException(bleData, mActivity.manageDevice.getRightBDevice());

        }else if(recordData.getName().equals(mActivity.manageDevice.getLeftFDevice())) {
            handleException(bleData, mActivity.manageDevice.getLeftFDevice());

        }else if(recordData.getName().equals(mActivity.manageDevice.getRightFDevice())) {
            handleException(bleData, mActivity.manageDevice.getRightFDevice());
        }
    }
    private void handleException(BleData date, String str) {
        StringBuffer buffer = new StringBuffer();
        int state = 0;
        float maxPress ,minPress,maxTemp;
        setUnitTextSize();
        if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Bar")) {
            maxPress = 3.20f;
            minPress = 1.800f;
        }else if(SharedPreferences.getInstance().getString(Constants.PRESSUER_DW, "Bar").equals("Kpa")) {
            maxPress = 3.20f*102;
            minPress = 1.800f*102;
        }else{
            maxPress = 3.20f*14.5f;
            minPress = 1.800f*14.5f;
        }
        if(SharedPreferences.getInstance().getString(Constants.TEMP_DW, "℃").equals("℃")) {
            maxTemp = 65;
        }else {
            maxTemp = (int)(65*1.80f)+32;
        }
        Logger.e("maxPress"+maxPress+"minPress"+minPress+"maxTemp"+maxTemp);
        buffer.append(date.getPress() > maxPress ? "高压" + " " : "");
        buffer.append(date.getPress() < minPress ? "低压" + " " : "");
        buffer.append(date.getTemp() > maxTemp ? "高温" + " " : "");
//        buffer.append(date.getTemp() < 20 ? "低温" + " " : "");
        ManageDevice.status[] statusData = ManageDevice.status.values();
        //状态检测
        if(date.getStatus()==8||date.getStatus()==0) {

        }else{
            buffer.append(statusData[date.getStatus()] + " ");
        }
        if (buffer.toString().contains("快漏气")||date.getPress() > maxPress || date.getPress() < minPress || date.getTemp() >= maxTemp ? true: false) {
            //高压
            bleIsException(str,buffer.toString());
        }else {
            //正常
            bleIsFind(str,buffer.toString(),date.getVoltage());
        }
    }

    /**
     * 发现蓝牙模块发出的广播 UI变化初始化
     * @param strAddress
     * @param noticeStr
     * @param date
     */
    private void bleIsFind(String strAddress, String noticeStr, float date) {
        if (!SharedPreferences.getInstance().getBoolean(Constants.DAY_NIGHT,false)) {
            dicoverBlueDeviceForDay(strAddress,noticeStr,date);
        }else {
            dicoverBlueDeviceForNight(strAddress,noticeStr,date);
        }
    }

    /**
     * 汽车轮胎异常情况报警
     * @param strAddress
     * @param noticeStr
     */
    private void bleIsException(String strAddress,String noticeStr) {
        if (!SharedPreferences.getInstance().getBoolean(Constants.DAY_NIGHT,false)) {
            bleIsExceptionForDay(strAddress,noticeStr);
        }else {
            bleIsExceptionForNight(strAddress,noticeStr);
        }
    }


//    private void showDialog(String str,int number)
//    {
//        if(!loadDialog.isShowing())
//        {
//            loadDialog.setText(str);
//            loadDialog.show();
//            loadDialog.setCountNum(number);
//        }else{
//            loadDialog.setCountNum(number);
//        }
//    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        myTimerTask.cancel();
//        myTimerTask = null;
//        timer = null;
        getActivity().unregisterReceiver(mGattUpdateReceiver);
    }
}
