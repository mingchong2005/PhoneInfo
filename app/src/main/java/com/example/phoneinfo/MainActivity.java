package com.example.phoneinfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.FileOutputStream;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class MainActivity extends Activity {

    private static final String TAG = "w60testled";
    private final String GREEN_LED_ON = "com.dfl.greenled.on";
    private final String GREEN_LED_OFF = "com.dfl.greenled.off";

    private IntentFilter mIntentFilter;

    private PhoneInfoUtils Simid;
    String name_list = null;
    String CellString = null;
    private Handler mTimerHandler = new Handler();

    int numer_counter = 0;
    private static TelephonyManager mTel;

    private static final int NOT_NOTICE = 2;//


    final byte[] LIGHT_ON = { '2', '5', '5' };
    final byte[] LIGHT_OFF = { '0' };
    private final int RED = 0xffff0000;
    private final int GREEN = 0xff00ff00;
    private final int BLUE = 0xff0000ff;
    private final int BLACK = 0xff000000;

    boolean isRedLedsOn = false;
    boolean isGreenLedsOn = false;
    boolean isBLueLedsOn = false;

    String RED_LED_DEV = "/sys/class/leds/red/brightness";
    String GREEN_LED_DEV = "/sys/class/leds/green/brightness";
    String BLUE_LED_DEV = "/sys/class/leds/blue/brightness";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button ButtonmyButton = (Button)findViewById(R.id.myButton);
        ButtonmyButton.setOnClickListener(myButtonBtnListener);
        final Button RedButton = (Button)findViewById(R.id.RedLeds);
        RedButton.setOnClickListener(RedLedsBtnListener);
        final Button GreenButton = (Button)findViewById(R.id.GreenLeds);
        GreenButton.setOnClickListener(GreenLedsBtnListener);
        final Button BluemyButton = (Button)findViewById(R.id.BlueLeds);
        BluemyButton.setOnClickListener(BlueLedsBtnListener);

        Simid = new PhoneInfoUtils(this);

        CellString = Simid.setCurrentCellLocationExt();
        writeFile(CellString,"testlog.txt");
        mTimerHandler.postDelayed(runnableReadTemperature, 500);
        /*
        Simid.getIccid();

        Simid.getNativePhoneNumber();

        //��ȡ�ֻ���������Ϣ
        Simid.getProvidersName();
        Simid.getImei();
        int networktype = Simid.getTelNetworkTypeINT();
        Log.w(TAG, "getTelNetworkTypeINT =" + networktype);
 		writeFile(String.valueOf(networktype),"testlog.txt");

        //mTimerHandler.postDelayed(runnableReadTemperature, 500);

*/
    }

    Runnable runnableReadTemperature = new Runnable() {
        @Override
        public void run() {
            //name_list = Simid.getPhoneInfo();
            //Log.w(TAG, "String strarray =" + name_list);
            //writeFile(name_list,"testlog.txt");
            //Simid.getIccid();

            //Simid.getNativePhoneNumber();

            //��ȡ�ֻ���������Ϣ
            //Simid.getProvidersName();
            //Simid.getImei();
            if(numer_counter++ > 10){
                numer_counter = 0;
                int networktype = Simid.getTelNetworkTypeINT();
                Log.w(TAG, "getTelNetworkTypeINT =" + networktype);

                CellString = Simid.setCurrentCellLocationExt();
                writeFile(CellString,"testlog.txt");
            }
            mTimerHandler.removeCallbacks(runnableReadTemperature);
            mTimerHandler.postDelayed(runnableReadTemperature, 60000);
        }
    };


    @Override
    protected void onPause() {
        //unregister();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume ");
    }

    protected void onStop(){
        super.onStop();
    }

    public static void writeFile(String str, String pathname) {
        String path = Environment.getExternalStorageDirectory().getPath();
        Log.w(TAG, "path:" + path);
        File file = new File(path, pathname);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            long currentTimeMillis = System.currentTimeMillis();
            Date d = new Date(currentTimeMillis);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(d);
            RandomAccessFile random = new RandomAccessFile(file, "rw");
            random.seek(random.length());
            String s = time + "-----" + str + "\n";
            random.writeUTF(s);
            random.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void setLedLightColor(int color) {
        boolean red = false, green = false, blue = false;
        switch (color) {
            case RED:
                red = true;
                break;
            case GREEN:
                green = true;
                break;
            case BLUE:
                blue = true;
                break;
            default:
                break;
        }
        try {
            FileOutputStream fRed = new FileOutputStream(RED_LED_DEV);
            fRed.write(red ? LIGHT_ON : LIGHT_OFF);
            fRed.close();
            FileOutputStream fGreen = new FileOutputStream(GREEN_LED_DEV);
            fGreen.write(green ? LIGHT_ON : LIGHT_OFF);
            fGreen.close();
            FileOutputStream fBlue = new FileOutputStream(BLUE_LED_DEV);
            fBlue.write(blue ? LIGHT_ON : LIGHT_OFF);
            fBlue.close();


        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }
    }

    private View.OnClickListener myButtonBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(GREEN_LED_ON);
            getApplication().sendBroadcast(intent);

        }
    };
    private View.OnClickListener RedLedsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isRedLedsOn) {
                isRedLedsOn = false;
                setLedLightColor(BLACK);
            }
            else {
                isRedLedsOn = true;
                setLedLightColor(RED);
            }

        }
    };    private View.OnClickListener GreenLedsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isGreenLedsOn){
                isGreenLedsOn = false;
                setLedLightColor(BLACK);
            }
            else {
                isGreenLedsOn = true;
                setLedLightColor(GREEN);
            }

        }
    };    private View.OnClickListener BlueLedsBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isBLueLedsOn){
                isBLueLedsOn = false;
                setLedLightColor(BLACK);
            }
            else {
                isBLueLedsOn = true;
                setLedLightColor(BLUE);
            }

        }
    };
}
