package com.example.sid_fu.blecentral.helper;

import com.example.sid_fu.blecentral.ui.BleData;
import com.example.sid_fu.blecentral.utils.Constants;
import com.example.sid_fu.blecentral.utils.DigitalTrans;
import com.example.sid_fu.blecentral.utils.Logger;
import com.example.sid_fu.blecentral.utils.SharedPreferences;

import java.text.DecimalFormat;

/**
 * author：Administrator on 2016/9/28 08:54
 * company: xxxx
 * email：1032324589@qq.com
 */

public class DataHelper {

    public DataHelper() {
    }
    public static BleData getData(byte[] data){
        float voltage = 0.00f,press = 0, temp =0;
        int temp1 = 0,state = 0,rssi = 0;
        DecimalFormat df = new DecimalFormat("######0");
        BleData bleData = new BleData();
        if(data==null) return bleData;
        if(data.length==0) return bleData;
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
        }else if(data.length==9) {
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
        return bleData;
    }
}
