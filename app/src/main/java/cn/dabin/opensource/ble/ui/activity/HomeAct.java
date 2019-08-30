package cn.dabin.opensource.ble.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.ViewpagerFragmentAdapter;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.event.BleEvent;
import cn.dabin.opensource.ble.service.UartService;
import cn.dabin.opensource.ble.ui.fragment.DataFagm;
import cn.dabin.opensource.ble.ui.fragment.GuardianFagm;
import cn.dabin.opensource.ble.ui.fragment.HomeFrgm;
import cn.dabin.opensource.ble.ui.fragment.MeFrgm;
import cn.dabin.opensource.ble.ui.fragment.SettingFrgm;
import cn.dabin.opensource.ble.util.DevicePreferences;
import github.benjamin.bottombar.NavigationController;
import github.benjamin.bottombar.PageNavigationView;
import github.benjamin.bottombar.item.BaseTabItem;
import github.benjamin.bottombar.tabs.SpecialTab;
import github.benjamin.bottombar.tabs.SpecialTabRound;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble
 * Created by :  dabin.
 * Created time: 2019/8/27 14:28
 * Changed by :  dabin.
 * Changed time: 2019/8/27 14:28
 * Class description:
 */
public class HomeAct extends BaseActivity  implements RadioGroup.OnCheckedChangeListener, ServiceConnection, View.OnClickListener {
    public static final String MESSAGE_RECEIVED_ACTION = "cn.dabin.opensource.ble.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int CONNECT_LAST_DEVICE = 3;
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    public static boolean isForeground = false;
    private static int currentStatus = UART_PROFILE_DISCONNECTED;
    public final String TAG = this.getClass().getSimpleName();
    private UartService uartService = null;
    private BluetoothDevice btDevice = null;
    private BluetoothAdapter btAdapter = null;
    private ArrayAdapter<String> listAdapter;
    private String lastDeviceAddress = null; // stores last-connected BT device address
    private boolean mUserDisconnect = false; // flag for user vs unexpected BT disconnect
    private ViewPager viewPager;
    private BaseTabItem tabsMessage;
    private BaseTabItem tabsContact;
    private BaseTabItem tabsWork;
    private BaseTabItem tabsChart;
    private BaseTabItem tabsMine;
    private FrameLayout mainContent;

    public static void openAct(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeAct.class);
        context.startActivity(intent);
    }

    // this is used to keep track of BT connection state for tryConnectBT
    public static boolean isBTConnected() {
        return currentStatus == UART_PROFILE_CONNECTED;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        initView();
    }

    private void initView() {
        mainContent = findViewById(R.id.main_content);
        viewPager = findViewById(R.id.viewPager);
        initBottomBar();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        EventBus.getDefault().register(this);
        if (btAdapter == null) {
            showCenterInfoMsg("蓝牙不可用");
            finish();
            return;
        }
        initService();
        // retrieve last connected BT device, this needs to be set up for quick autoconnect
        lastDeviceAddress = DevicePreferences.getLastDevice(getApplicationContext());
        Log.d(TAG, "..retrieved lastDeviceAddress= " + lastDeviceAddress);
        if (lastDeviceAddress != null && btAdapter.isEnabled()) {
            btDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(lastDeviceAddress);
        }
    }

    private void initBottomBar() {
        PageNavigationView tab = findViewById(R.id.tab);
        tabsMessage = newItem(R.drawable.ic_home_def, R.drawable.icon_home_che, "首页");
        tabsContact = newItem(R.drawable.icon_data_def, R.drawable.icon_data_che, "监控");
        tabsWork = newItem(R.drawable.icon_value_def, R.drawable.icon_value_che, "数据");
        tabsChart = newItem(R.drawable.icon_set_def, R.drawable.icon_set_che, "设置");
        tabsMine = newItem(R.drawable.icon_mine_def, R.drawable.icon_mine_che, "我的");

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFrgm());
        fragmentList.add(new GuardianFagm());
        fragmentList.add(new DataFagm());
        fragmentList.add(new SettingFrgm());
        fragmentList.add(new MeFrgm());
        NavigationController navigationController = tab.custom().addItem(tabsMessage).addItem(tabsContact).addItem(tabsWork).addItem(tabsChart).addItem(tabsMine).build();
        viewPager.setAdapter(new ViewpagerFragmentAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOffscreenPageLimit(5);
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(2);
    }

    /**
     * 正常tab
     */
    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        SpecialTab mainTab = new SpecialTab(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xffb8bfcc);
        mainTab.setTextCheckedColor(0xff689df8);
        return mainTab;
    }

    /**
     * 中间圆形圆形tab
     */
    private BaseTabItem newRoundItem(int drawable, int checkedDrawable, String text) {
        SpecialTabRound mainTab = new SpecialTabRound(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xffb8bfcc);
        mainTab.setTextCheckedColor(0xff689df8);
        return mainTab;
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BleEvent event) {
        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
        switch (event.getCurrentEvent()) {
            case BleEvent.ACTION_GATT_CONNECTED:
                Log.d(TAG, "UART_CONNECT_MSG");
                /*btnConnectDisconnect.setText("Disconnect");
                edtMessage.setEnabled(true);
                btnSend.setEnabled(true);
                ((TextView) findViewById(R.id.deviceName)).setText(btDevice.getName() + " - ready");
                listAdapter.add("[" + currentDateTimeString + "] Connected to: " + btDevice.getName());
                messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                currentStatus = UART_PROFILE_CONNECTED;*/
                break;
            case BleEvent.ACTION_GATT_DISCONNECTED:
            /*    Log.d(TAG, "UART_DISCONNECT_MSG");
                btnConnectDisconnect.setText("Connect");
                edtMessage.setEnabled(false);
                btnSend.setEnabled(false);
                ((TextView) findViewById(R.id.deviceName)).setText("Not Connected");
                listAdapter.add("[" + currentDateTimeString + "] Disconnected to: " + btDevice.getName());
                currentStatus = UART_PROFILE_DISCONNECTED;
                uartService.close();
                tryConnectBT();*/ // try to reconnect, if needed
                //setUiState();
                break;
            case BleEvent.ACTION_GATT_SERVICES_DISCOVERED:
                uartService.enableTXNotification();
                break;
            case BleEvent.ACTION_DATA_AVAILABLE:
               /* try {
                    String text = new String(event.getValue(), StandardCharsets.UTF_8);
                    listAdapter.add("[" + currentDateTimeString + "] RX: " + text);
                    messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                    commitToFile("[" + currentDateTimeString + "] RX: " + text);

                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }*/
                break;
            case BleEvent.DEVICE_DOES_NOT_SUPPORT_UART:
                showMessage("Device doesn't support UART. Disconnecting");
                uartService.disconnect();
                break;
            default:
                break;

        }

    }

    // bluetooth autoconnect to last device and reconnect service
    public void tryConnectBT() {
        if (mUserDisconnect) {
            mUserDisconnect = false;
            return; // this was an intentional disconnect, do nothing
        }
        if (lastDeviceAddress == null) {
            return; // we have nothing to connect to..
        }

        final Handler handler = new Handler();
        final Runnable runner = new Runnable() {
            @Override public void run() {
                Log.d(TAG, "checking BT connection onStart");
                if (uartService == null) {
                    return;
                }
                if (!isBTConnected()) {
                    uartService.connect(lastDeviceAddress);
                }
                if (!isBTConnected()) {
                    Log.d(TAG, "Failed to connect, try again later..");
                    handler.postDelayed(this, 10000);
                }
            }
        };
        handler.postDelayed(runner, 100);
    }

    private void commitToFile(String loggedText) throws IOException {
        FileOutputStream fOut = openFileOutput("savedData.txt",
                MODE_APPEND);
        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        osw.write(loggedText);
        osw.flush();
        osw.close();
    }

    private void initService() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        tryConnectBT();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        unbindService(this);
        uartService.stopSelf();
        uartService = null;

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        Log.d(TAG, "onResume");
        if (!btAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECT_LAST_DEVICE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d(TAG, "..connecting to lastDeviceAddress= " + lastDeviceAddress);
                    uartService.connect(lastDeviceAddress);
                }
                break;
            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    btDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + btDevice + "mserviceValue" + uartService);
                    ((TextView) findViewById(R.id.deviceName)).setText(btDevice.getName() + " - connecting");
                    // store connected device
                    Log.d(TAG, "..storing lastDeviceAddress= " + deviceAddress);
                    DevicePreferences.setLastDevice(getApplicationContext(), deviceAddress);
                    uartService.connect(lastDeviceAddress);


                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }


    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (currentStatus == UART_PROFILE_CONNECTED) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain); // this only starts new task if not already running
            showMessage("nRFUART's running in background.\n             Disconnect to exit");
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("系统消息！")
                    .setMessage("确定退出嘛？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    @Override public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        uartService = ((UartService.LocalBinder) iBinder).getService();
        Log.d(TAG, "onServiceConnected uartService= " + uartService);
        if (!uartService.initialize()) {
            Log.e(TAG, "Unable to initialize Bluetooth");
            finish();
        }
    }

    @Override public void onServiceDisconnected(ComponentName componentName) {
        //uartService.disconnect(btDevice);
        uartService = null;
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                if (!btAdapter.isEnabled()) {
                    Log.i(TAG, "onClick - BT not enabled yet");
                    Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                } else {
                   /* if (btnConnectDisconnect.getText().equals("Connect")) {
                        //Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices
                        Intent newIntent = new Intent(HomeAct.this, DeviceListAct.class);
                        startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
                    } else {
                        //Disconnect button pressed
                        if (btDevice != null) {
                            mUserDisconnect = true;
                            uartService.disconnect();
                        }
                    }*/
                }
                break;
            case R.id.sendButton:
                EditText editText = findViewById(R.id.sendText);
                String message = editText.getText().toString();
                byte[] value;
                //send data to service
                value = message.getBytes(StandardCharsets.UTF_8);
                uartService.writeRXCharacteristic(value);
                //Update the log with time stamp
                String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                listAdapter.add("[" + currentDateTimeString + "] TX: " + message);
               /* messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
                edtMessage.setText("");*/
                break;
            default:
                break;
        }
    }


}
