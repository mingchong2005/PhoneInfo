package com.example.phoneinfo;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class PhoneInfoUtils {

    private static final String TAG = "PhoneInfoUtils";

    private static TelephonyManager telephonyManager;
    //�ƶ���Ӫ�̱��
    private String NetworkOperator;
    private Context context;

    public PhoneInfoUtils(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    //��ȡsim��iccid
    public String getIccid() {
        String iccid = "N/A";
        iccid = telephonyManager.getSimSerialNumber();
        Log.w(TAG, "iccid strarray =" + iccid);
        return iccid;
    }

    //��ȡ�绰����
    public String getNativePhoneNumber() {
        String nativePhoneNumber = "N/A";
        nativePhoneNumber = telephonyManager.getLine1Number();
        Log.w(TAG, "nativePhoneNumber strarray =" + nativePhoneNumber);
        return nativePhoneNumber;
    }

    //��ȡ�ֻ���������Ϣ
    public String getProvidersName() {
        String providersName = "N/A";
        NetworkOperator = telephonyManager.getNetworkOperator();
        //IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02���й��ƶ���01���й���ͨ��03���й����š�
//        Flog.d(TAG,"NetworkOperator=" + NetworkOperator);
        if (NetworkOperator.equals("46000") || NetworkOperator.equals("46002")) {
            providersName = "�й��ƶ�";//�й��ƶ�
        } else if(NetworkOperator.equals("46001")) {
            providersName = "�й���ͨ";//�й���ͨ
        } else if (NetworkOperator.equals("46003")) {
            providersName = "�й�����";//�й�����
        }
        Log.w(TAG, "providersName strarray =" + providersName);
        return providersName;

    }
    
    //��ȡimei
    public String getImei() {
        String imei = "N/A";
        imei = telephonyManager.getDeviceId();
        Log.w(TAG, "imei strarray =" + imei);
        return imei;
    }  
    
    public String setCurrentCellLocationExt() {    
    	//��ȡMCC MNC LAC CELL_ID    
    	StringBuffer cellbuff = new StringBuffer();
    	if (telephonyManager.getCellLocation() != null) {
    		        GsmCellLocation gcl = (GsmCellLocation) telephonyManager.getCellLocation();
    		        String operator = telephonyManager.getNetworkOperator(); 
    		        if (operator.length() != 0) {            
    		        	//String mMcc = parseInt(operator.substring(0, 3));            
    		        	//String mMnc = parseInt(operator.substring(3));            
    		        	int mLac = gcl.getLac();  
    		        	cellbuff.append("\nmLac = " + String.valueOf(mLac));
    		        	
    		        	int mCellId = gcl.getCid();   
    		        	cellbuff.append("\nmCellId = " + String.valueOf(mCellId));
    		        	}    
    		  }
    	return  cellbuff.toString();
    }

    
    
    public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuffer sb = new StringBuffer();

        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());//�ƶ���Ӫ�̱��
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());//�ƶ���Ӫ������
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        return  sb.toString();
    }

    /**
     * ��ȡConnectivityManager
     */
    public static ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    /**
     * ��ȡConnectivityManager
     */
    
    /*
    public static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
    
    */
    public static TelephonyManager getTelephonyManager() {
        return telephonyManager;//(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }
    /**
     * get connected network type by {@link ConnectivityManager}
     * <p>
     * such as WIFI, MOBILE, ETHERNET, BLUETOOTH, etc.
     *
     * @return {@link ConnectivityManager#TYPE_WIFI}, {@link ConnectivityManager#TYPE_MOBILE},
     * {@link ConnectivityManager#TYPE_ETHERNET}...
     */
    public static int getConnectedTypeINT(Context context) {
        NetworkInfo net = getConnectivityManager(context).getActiveNetworkInfo();
        if (net != null) {
            Log.i(TAG, "NetworkInfo: " + net.toString());
            return net.getType();
        }
        return -1;
    }

    /**
     * get network type by {@link TelephonyManager}
     * <p>
     * such as 2G, 3G, 4G, etc.
     *
     * @return {@link TelephonyManager#NETWORK_TYPE_CDMA}, {@link TelephonyManager#NETWORK_TYPE_GPRS},
     * {@link TelephonyManager#NETWORK_TYPE_LTE}...
     */
    public static int getTelNetworkTypeINT() {
        return getTelephonyManager().getNetworkType();
    }
  /*
     public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_GPRS = 1;
    public static final int NETWORK_TYPE_EDGE = 2;
    public static final int NETWORK_TYPE_UMTS = 3;
    public static final int NETWORK_TYPE_CDMA = 4;
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    public static final int NETWORK_TYPE_EVDO_A = 6;
    public static final int NETWORK_TYPE_1xRTT = 7;
    public static final int NETWORK_TYPE_HSDPA = 8;
    public static final int NETWORK_TYPE_HSUPA = 9;
    public static final int NETWORK_TYPE_HSPA = 10;
    public static final int NETWORK_TYPE_IDEN = 11;
    public static final int NETWORK_TYPE_EVDO_B = 12;
    public static final int NETWORK_TYPE_LTE = 13;
    public static final int NETWORK_TYPE_EHRPD = 14;
     public static final int NETWORK_TYPE_HSPAP = 15;
  */
    public static NetWorkType getNetworkType(Context context) {
        int type = getConnectedTypeINT(context);
        switch (type) {
            case ConnectivityManager.TYPE_WIFI:
                return NetWorkType.WIFI;
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                int teleType = getTelephonyManager().getNetworkType();
                switch (teleType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetWorkType.NET_2_G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetWorkType.NET_3_G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetWorkType.NET_4_G;
                    default:
                        return NetWorkType.UN_KNOWN;
                }
            default:
                return NetWorkType.UN_KNOWN;
        }
    }

    public enum NetWorkType {
        UN_KNOWN(-1),
        WIFI(1),
        NET_2_G(2),
        NET_3_G(3),
        NET_4_G(4);

        public int value;

        NetWorkType(int value) {
            this.value = value;
        }
    }
    
}