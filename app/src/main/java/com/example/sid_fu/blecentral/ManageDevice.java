package com.example.sid_fu.blecentral;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/4/16.
 */
public class ManageDevice implements Serializable{
    private  BluetoothLeService mBluetoothLeService;
    private  Context context;

    public boolean isRecvData;

    public int disLeftFCount = 0;
    public int disRightFCount = 0;
    public int disLeftBCount = 0;
    public int disRightBCount = 0;

    public boolean leftF_notify;
    public boolean rightF_notify ;
    public boolean leftB_notify ;
    public boolean rightB_notify ;

    public String leftF_preContent;
    public String rightF_preContent ;
    public String leftB_preContent ;
    public String rightB_preContent ;

    public String getLeftBDevice() {
        return leftBDevice;
    }

    public void setLeftBDevice(String leftBDevice) {
        this.leftBDevice = leftBDevice;
    }

    public String getRightBDevice() {
        return rightBDevice;
    }

    public void setRightBDevice(String rightBDevice) {
        this.rightBDevice = rightBDevice;
    }

    public String getRightFDevice() {
        return rightFDevice;
    }

    public void setRightFDevice(String rightFDevice) {
        this.rightFDevice = rightFDevice;
    }

    public String getLeftFDevice() {
        return leftFDevice;
    }

    public void setLeftFDevice(String leftFDevice) {
        this.leftFDevice = leftFDevice;
    }

    public  String leftFDevice ;
    public  String rightFDevice ;
    public  String leftBDevice ;
    public  String rightBDevice ;


    public  String[] blueDeviceTable = {getLeftFDevice(),getRightFDevice(),getLeftBDevice(),getRightBDevice()};
    private static ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    public enum blueDevice{
        leftFDevice,
        rightFDevice,
        leftBDevice,
        rightBDevice;

        public static blueDevice getAnimal(String tagStr) {
            return valueOf(tagStr);
        }
    }
//    String MON = "初始状态";
//    String TUE = "正常";
//    String WED = "快漏气";
//    String THU = "慢漏气";
//    String FRI = "加气";
//    String SAT = "保留";
//    String SUN = "异常，传感器未能获取胎压信息";
    public static enum  status{
        正常(0),加气(1), 慢漏气(2),保留3(3),快漏气(4),保留5(5),保留6(6),保留7(7),保留(8);
        private final int mValue;
        status(int value)
        {
            mValue = value;
        }
    }
    public static final int BLE_GAP_AD_TYPE_ADVDATA                              = 0x00; /**< Flags for discoverability. */
    public static final int BLE_GAP_AD_TYPE_FLAGS                              = 0x01; /**< Flags for discoverability. */
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_MORE_AVAILABLE  = 0x02;/**< Partial list of 16 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_16BIT_SERVICE_UUID_COMPLETE        = 0x03; /**< Complete list of 16 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_MORE_AVAILABLE  = 0x04; /**< Partial list of 32 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_32BIT_SERVICE_UUID_COMPLETE        = 0x05; /**< Complete list of 32 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_MORE_AVAILABLE = 0x06; /**< Partial list of 128 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_128BIT_SERVICE_UUID_COMPLETE       = 0x07; /**< Complete list of 128 bit service UUIDs. */
    public static final int BLE_GAP_AD_TYPE_SHORT_LOCAL_NAME                   = 0x08; /**< Short local device name. */
    public static final int BLE_GAP_AD_TYPE_COMPLETE_LOCAL_NAME                = 0x09; /**< Complete local device name. */
    public static final int BLE_GAP_AD_TYPE_TX_POWER_LEVEL                     = 0x0A; /**< Transmit power level. */
    public static final int BLE_GAP_AD_TYPE_CLASS_OF_DEVICE                    = 0x0D; /**< Class of device. */
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_HASH_C              = 0x0E; /**< Simple Pairing Hash C. */
    public static final int BLE_GAP_AD_TYPE_SIMPLE_PAIRING_RANDOMIZER_R        = 0x0F; /**< Simple Pairing Randomizer R. */
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_TK_VALUE          = 0x10; /**< Security Manager TK Value. */
    public static final int BLE_GAP_AD_TYPE_SECURITY_MANAGER_OOB_FLAGS         = 0x11; /**< Security Manager Out Of Band Flags. */
    public static final int BLE_GAP_AD_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE    = 0x12; /**< Slave Connection Interval Range. */
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_16BIT      = 0x14; /**< List of 16-bit Service Solicitation UUIDs. */
    public static final int BLE_GAP_AD_TYPE_SOLICITED_SERVICE_UUIDS_128BIT     = 0x15; /**< List of 128-bit Service Solicitation UUIDs. */
    public static final int BLE_GAP_AD_TYPE_SERVICE_DATA                       = 0x16; /**< Service Data. */
    public static final int BLE_GAP_AD_TYPE_PUBLIC_TARGET_ADDRESS              = 0x17; /**< Public Target Address. */
    public static final int BLE_GAP_AD_TYPE_RANDOM_TARGET_ADDRESS              = 0x18; /**< Random Target Address. */
    public static final int BLE_GAP_AD_TYPE_APPEARANCE                         = 0x19; /**< Appearance. */
    public static final int BLE_GAP_AD_TYPE_MANUFACTURER_SPECIFIC_DATA         = 0xFF; /**< Manufacturer Specific Data. */

    public ManageDevice(){

    }
    public ManageDevice(Context context,BluetoothLeService mBluetoothLeService){
        this.context = context;
        this.mBluetoothLeService = mBluetoothLeService;
    }
    public static boolean isEquals(List<MyBluetoothDevice> mList,BluetoothDevice add)
    {
        for (MyBluetoothDevice ble : mList)
        {
            if(ble.getDevice().equals(add))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isBundConfigEquals(List<MyBluetoothDevice> mList,BluetoothDevice add)
    {
        for (MyBluetoothDevice ble : mList)
        {
            if(ble.getDevice().getAddress().equals(add.getAddress()))
            {
                return true;
            }
        }
        return false;
    }
    public static boolean isConfigEquals(List<BluetoothDevice> mList,BluetoothDevice add)
    {
        for (BluetoothDevice ble : mList)
        {
            if(ble.getAddress().equals(add.getAddress()))
            {
                return true;
            }
        }
        return false;
    }
    public static void setSuccess(BluetoothDevice device,MyBluetoothDevice status)
    {
        if(device.equals(status.getDevice()))
        {
            status.setSuccessComm(true);
        }
    }
    public static void setFailed(BluetoothDevice device,MyBluetoothDevice status)
    {
        if(device.equals(status.getDevice()))
        {
            status.setRequestConnect(false);
            status.setSuccessComm(false);
            status.setBluetoothGatt(null);
        }
    }
    public static void setIsScan(BluetoothDevice device,MyBluetoothDevice status)
    {
        if(device.equals(status.getDevice()))
        {
            status.setBleScaned(true);
        }
    }
    //解析广播数据
    public static byte[] adv_report_parse(byte type, byte[] adv_data)
    {
        int index = 0;
        int length;
        byte[] type_data;

        length = adv_data.length;
        while (index < length)
        {
            int   field_length = adv_data[index];
            byte  field_type   = adv_data[index+1];

            if (field_type == type)
            {
                type_data = new byte[field_length-1];
                int i;
                for(i = 0;i < field_length-1;i++)
                {
                    type_data[i] = adv_data[index+2+i];
                }
                return type_data;
            }
            index += field_length+1;
        }
        return null;
    }
    public static ParsedAd parseData(byte[] adv_data) {
        ParsedAd parsedAd = new ParsedAd();
        ByteBuffer buffer = ByteBuffer.wrap(adv_data).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2) {
            byte length = buffer.get();
            if (length == 0)
                break;
            byte type = buffer.get();
            length -= 1;
            switch (type) {
                case 0x01: // Flags
                    parsedAd.flags = buffer.get();
                    length--;
                    break;
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                case 0x14: // List of 16-bit Service Solicitation UUIDs
                    while (length >= 2) {
                        parsedAd.uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;
                case 0x04: // Partial list of 32 bit service UUIDs
                case 0x05: // Complete list of 32 bit service UUIDs
                    while (length >= 4) {
                        parsedAd.uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getInt())));
                        length -= 4;
                    }
                    break;
                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                case 0x15: // List of 128-bit Service Solicitation UUIDs
                    while (length >= 16) {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        parsedAd.uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;
                case 0x08: // Short local device name
                case 0x09: // Complete local device name
                    byte sb[] = new byte[length];
                    buffer.get(sb, 0, length);
                    length = 0;
                    parsedAd.localName = new String(sb).trim();
                    break;
                case (byte) 0xFF: // Manufacturer Specific Data
                    parsedAd.manufacturer = buffer.getShort();
                    length -= 2;
                    break;
                default: // skip
                    break;
            }
            if (length > 0) {
                buffer.position(buffer.position() + length);
            }
        }
        return parsedAd;
    }


}
