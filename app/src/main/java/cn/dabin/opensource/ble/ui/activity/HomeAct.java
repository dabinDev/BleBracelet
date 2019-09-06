package cn.dabin.opensource.ble.ui.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.SingleFilterScanCallback;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.ViewpagerFragmentAdapter;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.event.CallbackDataEvent;
import cn.dabin.opensource.ble.event.ConnectEvent;
import cn.dabin.opensource.ble.event.NotifyDataEvent;
import cn.dabin.opensource.ble.global.BleApplication;
import cn.dabin.opensource.ble.network.bean.BleInfo;
import cn.dabin.opensource.ble.ui.fragment.DataFagm;
import cn.dabin.opensource.ble.ui.fragment.GuardianFagm;
import cn.dabin.opensource.ble.ui.fragment.HomeFrgm;
import cn.dabin.opensource.ble.ui.fragment.MeFrgm;
import cn.dabin.opensource.ble.ui.fragment.SettingFrgm;
import cn.dabin.opensource.ble.util.BluetoothDeviceManager;
import cn.dabin.opensource.ble.util.Logger;
import cn.dabin.opensource.ble.util.StringUtils;
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
public class HomeAct extends BaseActivity implements IScanCallback {
    public static final UUID DFU_SERVICE_UUID = UUID.fromString("8e400001-f315-4f60-9fb8-838830daea50");//空中升级DFU
    public static final UUID DFU_CHAR_UUID = UUID.fromString("8e400001-f315-4f60-9fb8-838830daea50");//空中升级DFU

    public static final UUID TX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");//UART TX(BLE发送)
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");//UART TX(BLE发送)

    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");//UART RX(BLE接收)
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");//UART RX(BLE接收)

    public static final UUID SYSTEM_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");//System ID (MAC地址)  高版本兼容
    public static final UUID SYSTEM_CHAR_UUID = UUID.fromString("00002A23-0000-1000-8000-00805F9B34FB");//System ID (MAC地址)  高版本兼容


    private final String TAG = HomeAct.this.getClass().getName();
    private BluetoothLeDevice currentDevice = null;
    public static final String MESSAGE_RECEIVED_ACTION = "cn.dabin.opensource.ble.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private ViewPager viewPager;
    private BaseTabItem tabsMessage;
    private BaseTabItem tabsContact;
    private BaseTabItem tabsWork;
    private BaseTabItem tabsChart;
    private BaseTabItem tabsMine;


    public static void openAct(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeAct.class);
        context.startActivity(intent);
    }


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        viewPager = findViewById(R.id.viewPager);
        BluetoothDeviceManager.getInstance().init(this);
        BusManager.getBus().register(this);
        initBottomBar();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        ViseLog.d(TAG, "onDestroy()");
        BusManager.getBus().unregister(this);
        BluetoothDeviceManager.getInstance().disconnect(currentDevice);
    }

    @Override
    protected void onStop() {
        ViseLog.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        ViseLog.d(TAG, "onPause");
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onRestart() {
        ViseLog.d(TAG, "onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        reqPermission(Permission.ACCESS_COARSE_LOCATION);
    }


    private void reqPermission(@NonNull String currentPermission) {
        AndPermission.with(this)
                .runtime()
                .permission(currentPermission)
                .onGranted(permissions -> {
                    if (!currentPermission.contains(Permission.ACCESS_FINE_LOCATION)) {
                        reqPermission(Permission.ACCESS_FINE_LOCATION);
                    } else {
                        scanDevice();
                    }
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                    reqPermission(currentPermission);
                })
                .start();
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("系统消息！")
                .setMessage("确定退出嘛？")
                .setPositiveButton("确定", (dialog, which) -> finish())
                .setNegativeButton("取消", null)
                .show();
    }


    private void scanDevice() {
        //该方式是扫到指定设备就停止扫描
        ViseBle.getInstance().startScan(new SingleFilterScanCallback(HomeAct.this).setDeviceName("MB0002"));
    }


    public void sendMsg(String msg) {
        runOnUiThread(() -> {
            byte[] value;
            //send data to service
            value = msg.getBytes(StandardCharsets.UTF_8);
            if (BluetoothDeviceManager.getInstance().isConnected(currentDevice)) {
                BluetoothDeviceManager.getInstance().write(currentDevice, value);
            } else {
                Logger.e(TAG, "当前蓝牙设备还未连接！");
            }
        });
    }


    @Subscribe
    public void showConnectedDevice(ConnectEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                Logger.e(TAG, "   Connect Success! ");
                currentDevice = event.getDeviceMirror().getBluetoothLeDevice();
                if (event.getDeviceMirror() != null && event.getDeviceMirror().getBluetoothGatt() != null) {
                    BluetoothGattService service1 = event.getDeviceMirror().getGattService(TX_SERVICE_UUID);
                    BluetoothGattCharacteristic characteristic1 = service1.getCharacteristic(TX_CHAR_UUID);
                    int charaProp1 = characteristic1.getProperties();
                    initNoticeCallBack(service1, characteristic1, charaProp1);

                    BluetoothGattService service2 = event.getDeviceMirror().getGattService(RX_SERVICE_UUID);
                    BluetoothGattCharacteristic characteristic2 = service2.getCharacteristic(RX_CHAR_UUID);
                    int charaProp2 = characteristic2.getProperties();
                    initNoticeCallBack(service2, characteristic2, charaProp2);

                    BluetoothGattService service3 = event.getDeviceMirror().getGattService(SYSTEM_SERVICE_UUID);
                    BluetoothGattCharacteristic characteristic3 = service3.getCharacteristic(SYSTEM_CHAR_UUID);
                    int charaProp3 = characteristic3.getProperties();
                    initNoticeCallBack(service3, characteristic3, charaProp3);
                }
            } else {
                if (event.isDisconnected()) {
                    Logger.e(TAG, "   isDisconnected   :" + true);
                } else {
                    Logger.e(TAG, "   isDisconnected   :" + false);
                }
            }
        }
    }

    private void initNoticeCallBack(BluetoothGattService service, BluetoothGattCharacteristic characteristic, int charaProp) {
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
            BluetoothDeviceManager.getInstance().bindChannel(currentDevice, PropertyType.PROPERTY_WRITE, service.getUuid(), characteristic.getUuid(), null);
        } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
            BluetoothDeviceManager.getInstance().bindChannel(currentDevice, PropertyType.PROPERTY_READ, service.getUuid(), characteristic.getUuid(), null);
            BluetoothDeviceManager.getInstance().read(currentDevice);
        }
        if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            BluetoothDeviceManager.getInstance().bindChannel(currentDevice, PropertyType.PROPERTY_NOTIFY, service.getUuid(), characteristic.getUuid(), null);
            BluetoothDeviceManager.getInstance().registerNotify(currentDevice, false);
        } else if ((charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
            BluetoothDeviceManager.getInstance().bindChannel(currentDevice, PropertyType.PROPERTY_INDICATE, service.getUuid(), characteristic.getUuid(), null);
            BluetoothDeviceManager.getInstance().registerNotify(currentDevice, true);
        }
    }

    @Subscribe
    public void showDeviceCallbackData(CallbackDataEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                if (event.getBluetoothGattChannel() != null && event.getBluetoothGattChannel().getCharacteristic() != null
                        && event.getBluetoothGattChannel().getPropertyType() == PropertyType.PROPERTY_READ) {
                    String msg = new String(event.getData(), StandardCharsets.UTF_8);
                    Logger.e(TAG, "   showDeviceCallbackData   :" + msg);
                }
            }
        }
    }

    @Subscribe
    public void showDeviceNotifyData(NotifyDataEvent event) {
        if (event != null && event.getData() != null && event.getBluetoothLeDevice() != null
                && event.getBluetoothLeDevice().getAddress().equals(currentDevice.getAddress())) {
            String msg = new String(event.getData(), StandardCharsets.UTF_8);
            Logger.e(TAG, "   showDeviceNotifyData    :" + msg);

        }
    }


    @Override public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
        Logger.e(TAG, StringUtils.value(bluetoothLeDevice.getName()) + bluetoothLeDevice.getAddress());
        if (StringUtils.value(bluetoothLeDevice.getName()).equals("MB0002")) {
            ViseBle.getInstance().stopScan(new SingleFilterScanCallback(HomeAct.this));
            runOnUiThread(() -> {
                String address = StringUtils.value(bluetoothLeDevice.getAddress());
                BleInfo info = BleApplication.getBleInfo(address);
                if (info == null) {
                    info = new BleInfo();
                    info.setMacAddress(address);
                    info.setDeviceName(bluetoothLeDevice.getName());
                }
                info.saveOrUpdate("macAddress=?", address);
                if (!BluetoothDeviceManager.getInstance().isConnected(bluetoothLeDevice)) {
                    BluetoothDeviceManager.getInstance().connect(bluetoothLeDevice);
                } else {
                    Logger.e(TAG, "当前设备已经连接！");
                }
            });

        }
    }

    @Override public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
        Logger.e(TAG, "onScanFinish");

    }

    @Override public void onScanTimeout() {
        if (currentDevice == null) {
            Logger.e(TAG, "蓝牙扫描超时！");
            return;
        }
        if (!BluetoothDeviceManager.getInstance().isConnected(currentDevice)) {
            Logger.e(TAG, "蓝牙扫描超时！");
            return;
        }
    }
}
