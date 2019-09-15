package cn.dabin.opensource.ble.ui.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.common.PropertyType;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.vise.baseble.model.resolver.GattAttributeResolver;
import com.vise.baseble.utils.BleUtil;
import com.vise.baseble.utils.HexUtil;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;
import com.vise.xsnow.event.BusManager;
import com.vise.xsnow.event.Subscribe;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import cn.dabin.opensource.ble.R;
import cn.dabin.opensource.ble.adapter.ViewpagerFragmentAdapter;
import cn.dabin.opensource.ble.base.BaseActivity;
import cn.dabin.opensource.ble.event.CallbackDataEvent;
import cn.dabin.opensource.ble.event.ConnectEvent;
import cn.dabin.opensource.ble.event.NotifyDataEvent;
import cn.dabin.opensource.ble.service.DfuService;
import cn.dabin.opensource.ble.ui.fragment.DataFagm;
import cn.dabin.opensource.ble.ui.fragment.GuardianFagm;
import cn.dabin.opensource.ble.ui.fragment.HomeFrgm;
import cn.dabin.opensource.ble.ui.fragment.MeFrgm;
import cn.dabin.opensource.ble.ui.fragment.SettingFrgm;
import cn.dabin.opensource.ble.util.BluetoothDeviceManager;
import cn.dabin.opensource.ble.util.StringUtils;
import github.benjamin.bottombar.NavigationController;
import github.benjamin.bottombar.PageNavigationView;
import github.benjamin.bottombar.item.BaseTabItem;
import github.benjamin.bottombar.tabs.SpecialTab;
import github.benjamin.bottombar.tabs.SpecialTabRound;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuServiceController;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble
 * Created by :  dabin.
 * Created time: 2019/8/27 14:28
 * Changed by :  dabin.
 * Changed time: 2019/8/27 14:28
 * Class description:
 */
public class HomeAct extends BaseActivity {
    public static final UUID DFU_CHECK_FOR_UPDATE_SERVICE_UUID = UUID.fromString("8e400001-f315-4f60-9fb8-838830daea50");//空中升级DFU
    public static final UUID DFU_CHECK_FOR_UPDATE_CHAR_UUID = UUID.fromString("8e400001-f315-4f60-9fb8-838830daea50");//空中升级DFU

    public static final UUID DFU_UPDATEING1_SERVICE_UUID = UUID.fromString("0000fe59-0000-1000-8000-00805f9b34fb");//空中升级DFU
    public static final UUID DFU_UPDATEING1_CHAR_UUID = UUID.fromString("8ec90002-f315-4f60-9fb8-838830daea50");//空中升级DFU
    public static final UUID DFU_UPDATEING2_SERVICE_UUID = UUID.fromString("0000fe59-0000-1000-8000-00805f9b34fb");//空中升级DFU
    public static final UUID DFU_UPDATEING2_CHAR_UUID = UUID.fromString("8ec90001-f315-4f60-9fb8-838830daea50");//空中升级DFU

    public static final UUID SOFT_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");//软件信息
    public static final UUID SOFT_CHAR_UUID = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");//软件信息

    public static final UUID TX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");//UART TX(BLE发送)
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");//UART TX(BLE发送)


    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");//UART RX(BLE接收)
    public static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");//UART RX(BLE接收)


    public static final UUID SYSTEM_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");//System ID (MAC地址)  高版本兼容
    public static final UUID SYSTEM_CHAR_UUID = UUID.fromString("00002a23-0000-1000-8000-00805f9b34fb");//System ID (MAC地址)  高版本兼容


    public static final String MESSAGE_RECEIVED_ACTION = "cn.dabin.opensource.ble.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;
    private final int type_connect_mb0002 = 10001;
    private final int type_connect_dfu_targ = 10002;
    private final int type_check_for_update = 10003;
    private final int type_updateing = 10004;
    private final String defaultNname = "MB0002";
    private final String dfuNname = "DfuTarg";
    private final String TAG = HomeAct.this.getClass().getName();
    public BleCallBack callBack;
    DfuServiceInitiator starter;
    DfuServiceController controller;
    private int currentType = -1;
    private String currentVersion = "";
    private String requireVersion = "2.4a";
    private ViewPager viewPager;
    private BaseTabItem tabsMessage;
    private BaseTabItem tabsContact;
    private BaseTabItem tabsWork;
    private BaseTabItem tabsChart;
    private BaseTabItem tabsMine;
    private BluetoothLeDevice currentDevice;
    private ScanCallback periodScanCallback = new ScanCallback(new IScanCallback() {
        @Override
        public void onDeviceFound(final BluetoothLeDevice bluetoothLeDevice) {
            if (bluetoothLeDevice != null && bluetoothLeDevice.getName() != null) {
                ViseLog.e("发现蓝牙设备:" + StringUtils.value(bluetoothLeDevice.getName()));
                if ((bluetoothLeDevice.getName().equals(defaultNname) || bluetoothLeDevice.getName().equals(dfuNname))) {
                    ViseLog.e("发现可用设备： " + bluetoothLeDevice.getName() + " 当前设备地址： " + bluetoothLeDevice.getAddress());
                    currentDevice = bluetoothLeDevice;
                    stopScan();
                    loading("蓝牙连接中");
                    BluetoothDeviceManager.getInstance().connect(bluetoothLeDevice);
                }
            }
        }

        @Override
        public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {
            ViseLog.e("蓝牙扫描结束");
            stopScan();
            dissmiss();
        }

        @Override
        public void onScanTimeout() {
            ViseLog.e("蓝牙扫描超时");
        }

    });
    private final DfuProgressListener mDfuProgressListener = new DfuProgressListener() {
        @Override
        public void onDeviceConnecting(String deviceAddress) {
            ViseLog.e("dfu" + "设备连接中");
        }

        @Override
        public void onDeviceConnected(String deviceAddress) {
            ViseLog.e("dfu" + "设备已经连接");
        }

        @Override
        public void onDfuProcessStarting(String deviceAddress) {
            ViseLog.e("dfu" + "开始升级");
        }

        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            ViseLog.e("dfu" + "开始升级");
        }

        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            ViseLog.e("dfu" + "启用DFU升级模式");
        }

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            ViseLog.e("dfu" + "DFU设备升级中  当前进度 " + percent + "%");
            loading("设备正在升级 " + percent + "%");
            //显示进度
        }

        @Override
        public void onFirmwareValidating(String deviceAddress) {
            ViseLog.e("dfu" + "DFU设备升级中  onFirmwareValidating ");
        }

        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            ViseLog.e("dfu" + "DFU设备断开连接中");
        }

        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            ViseLog.e("dfu" + "DFU设备断开连接完成");
        }

        @Override
        public void onDfuCompleted(String deviceAddress) {
            ViseLog.e("dfu" + "DFU设备升级完成");
            //停止dfu
            //升级成功，重新连接设备
            showCenterSuccessMsg("升级成功");
            dissmiss();
        }

        @Override
        public void onDfuAborted(String deviceAddress) {
            ViseLog.e("dfu" + "DFU设备升级断开");
            //升级流产，失败
            showTopWrongMsg("升级失败");
            dissmiss();
            startScan();
        }

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            ViseLog.e("dfu" + "DFU设备升级错误");
            //失败
            showTopWrongMsg("升级失败");
            dissmiss();
            startScan();
        }
    };

    public static void openAct(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, HomeAct.class);
        context.startActivity(intent);
    }

    public void setCallBack(BleCallBack callBack) {
        this.callBack = callBack;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);
        viewPager = findViewById(R.id.viewPager);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .init();
        initBottomBar();
        ViseLog.getLogConfig().configAllowLog(true);//配置日志信息
        ViseLog.plant(new LogcatTree());//添加Logcat打印信息
        BluetoothDeviceManager.getInstance().init(this);
        BusManager.getBus().register(this);

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
        ViseLog.d("onDestroy()");
        ViseBle.getInstance().clear();
        BusManager.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        ViseLog.d("onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        ViseLog.d("onPause");
        super.onPause();
        isForeground = false;
        stopScan();
        DfuServiceListenerHelper.unregisterProgressListener(this, mDfuProgressListener);
    }

    @Override
    protected void onRestart() {
        ViseLog.d("onRestart");
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        if (currentDevice == null) {
            loading("扫描蓝牙设备中");
            reqPermission(Permission.ACCESS_COARSE_LOCATION);
        } else {
            if (StringUtils.value(currentDevice.getName()).equals(dfuNname)) {
                DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);
            }
        }
    }


    private void reqPermission(@NonNull String currentPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                    .runtime()
                    .permission(currentPermission)
                    .onGranted(permissions -> {
                        if (!currentPermission.contains(Permission.ACCESS_FINE_LOCATION)) {
                            reqPermission(Permission.ACCESS_FINE_LOCATION);
                        } else {
                            enableBluetooth();
                        }
                    })
                    .onDenied(permissions -> {
                        // Storage permission are not allowed.
                        reqPermission(currentPermission);
                    })
                    .start();
        } else {
            enableBluetooth();
        }
    }

    private void enableBluetooth() {
        if (!BleUtil.isBleEnable(this)) {
            BleUtil.enableBluetooth(this, 1);
        } else {
            boolean isSupport = BleUtil.isSupportBle(this);
            boolean isOpenBle = BleUtil.isBleEnable(this);
            if (isSupport) {
                ViseLog.e("设备支持蓝牙");
            } else {
                ViseLog.e("设备支持不蓝牙");
            }
            if (isOpenBle) {
                ViseLog.e("蓝牙已经打开");
            } else {
                ViseLog.e("蓝牙未打开");
            }
        }
        if (currentDevice == null) {
            startScan();
        }
    }

    private void startScan() {
        ViseBle.getInstance().startScan(periodScanCallback);
    }

    private void stopScan() {
        ViseBle.getInstance().stopScan(periodScanCallback);
    }


    private void startUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DfuServiceInitiator.createDfuNotificationChannel(getApplicationContext());
        }
        starter = new DfuServiceInitiator(currentDevice.getAddress())
                .setDeviceName(currentDevice.getName())
                .setKeepBond(true)
// If you want to have experimental buttonless DFU feature supported call additionally:
                //starter.setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);
                .setZip(R.raw.update);
        controller = starter.start(this, DfuService.class);
    }


    private void stopUpdate() {
        new DfuServiceInitiator(currentDevice.getAddress())
                .setDisableNotification(true)
                .setZip(R.raw.update)
                .start((getBaseContext()), DfuService.class);
    }

    @Subscribe
    public void showDeviceCallbackData(CallbackDataEvent event) {
        if (event != null) {
            if (event.isSuccess()) {
                if (event.getBluetoothGattChannel() != null && event.getBluetoothGattChannel().getCharacteristic() != null
                        && event.getBluetoothGattChannel().getPropertyType() == PropertyType.PROPERTY_READ) {
                    String returnMsg = new String(event.getData(), StandardCharsets.UTF_8);
                    String serviceName = GattAttributeResolver.getAttributeName(event.getBluetoothGattChannel().getCharacteristic().getUuid().toString(), "其他服务").toLowerCase().replace(" ", "");
                    ViseLog.e("当前服务名称： " + serviceName + "\n 返回消息: " + returnMsg);
                    if (currentDevice.getName().equals(defaultNname) && currentType == type_check_for_update) {
                        currentVersion = returnMsg.toLowerCase();
                        if (serviceName.contains("softwarerevisionstring")) {
                            ViseLog.e("当前服务名称： " + serviceName + "\n 当前版本信息: " + currentVersion);
                            if (!"2.4a".equals(currentVersion)) {
                                loading("连接到更新通道");
                                new Handler().postDelayed(this::sendUpdateMsg, 3000);
                            } else {
                                showCenterSuccessMsg("当前设备已经是最新版本！");
                                BluetoothDeviceManager.getInstance().disconnect(currentDevice);
                            }
                        }
                    } else if (currentType == type_updateing) {
                        loading("发送数据包");
                        startUpdate();
                    } else if (currentType == type_connect_mb0002) {
                        showCenterSuccessMsg("连接成功！");
                        dissmiss();
                    }

                }
            }
        }
    }

    @Subscribe
    public void showDeviceNotifyData(NotifyDataEvent event) {
        if (event != null && event.getData() != null && event.getBluetoothLeDevice() != null
                && event.getBluetoothLeDevice().getAddress().equals(currentDevice.getAddress())) {
            String msg = new String(event.getData(), StandardCharsets.UTF_8);
            ViseLog.e("showDeviceNotifyData " + msg);
            ViseLog.e("showDeviceNotifyData hex " + Arrays.toString(event.getData()));
            if (callBack != null) {
                callBack.receivedData(msg);
            }
        }
    }

    @Subscribe
    public void showConnectedDevice(ConnectEvent event) {
        ViseLog.e("设备连接成功");
        if (event != null) {
            if (event.isSuccess()) {
                if (StringUtils.value(currentDevice.getName()).equals(defaultNname)) {
                    if (currentType == -1) {
                        currentType = type_check_for_update;
                        loading("检查升级中");
                        bindCheckUpdateNotice();
                        bindCheckUpdateReadAndWrite();
                    } else if (currentType == type_check_for_update) {
                        currentType = type_connect_mb0002;
                        loading("连接蓝牙中");
                        bindWrite();
                        bindRead();
                        bindNotice();
                    } else if (currentType == type_updateing) {
                        currentType = type_connect_mb0002;
                        loading("连接蓝牙中");
                        bindWrite();
                        bindRead();
                        bindNotice();
                    }
                    //检查升级
                } else if (StringUtils.value(currentDevice.getName()).equals(dfuNname)) {
                    loading("升级中");
                    currentType = type_updateing;
                    bindUpdateRead();
                    bindUpdateWrite();
                }
            } else {
                if (event.isDisconnected()) {
                    startScan();
                } else {
                    showCenterInfoMsg("尝试重连！");
                    startScan();
                }
            }
        }
    }


    public synchronized void sendUpdateMsg() {
        runOnUiThread(() -> {
            int sendCount = 5;
            while (sendCount > 0) {
                sendCount--;
                String input = "01";
                byte[] inputValue = HexUtil.decodeHex(input.toCharArray());
                new Handler().postDelayed(() -> BluetoothDeviceManager.getInstance().write(currentDevice, inputValue), sendCount * 00);
            }

        });
    }

    private void initNotice(BluetoothGattService service, BluetoothGattCharacteristic characteristic, int charaProp) {
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

    public synchronized void sendMsg(String msg) {
        runOnUiThread(() -> {
            byte[] value;
            //send data to service
            value = msg.getBytes(StandardCharsets.UTF_8);
            if (currentDevice != null) {
                BluetoothDeviceManager.getInstance().write(currentDevice, value);
                ViseLog.e("发送消息" + msg);
            } else {
                ViseLog.e("当前蓝牙设备还未连接！");
            }
        });
    }


    private void bindRead() {
        new Handler().postDelayed(() -> {
            BluetoothGattService service2 = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(RX_SERVICE_UUID);
            BluetoothGattCharacteristic characteristic2 = service2.getCharacteristic(RX_CHAR_UUID);
            int charaProp2 = characteristic2.getProperties();
            ViseLog.e("DeviceControl--- serviceUUid 绑定消息读取服务" + service2.getUuid().toString());
            ViseLog.e("DeviceControl--- characteristicUUid" + characteristic2.getUuid().toString());
            ViseLog.e("DeviceControl--- charaProp" + charaProp2);
            initNotice(service2, characteristic2, charaProp2);
        }, 500);
    }

    private void bindWrite() {
        new Handler().postDelayed(() -> {
            BluetoothGattService service1 = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(TX_SERVICE_UUID);
            BluetoothGattCharacteristic characteristic1 = service1.getCharacteristic(TX_CHAR_UUID);
            int charaProp1 = characteristic1.getProperties();
            ViseLog.e("DeviceControl--- serviceUUid 绑定消息写入服务" + service1.getUuid().toString());
            ViseLog.e("DeviceControl--- characteristicUUid" + characteristic1.getUuid().toString());
            ViseLog.e("DeviceControl--- charaProp" + charaProp1);
            initNotice(service1, characteristic1, charaProp1);
        }, 1000);
    }

    private void bindNotice() {
        new Handler().postDelayed(() -> {
            BluetoothGattService service4 = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(SYSTEM_SERVICE_UUID);
            BluetoothGattCharacteristic characteristic4 = service4.getCharacteristic(SYSTEM_CHAR_UUID);
            int charaProp4 = characteristic4.getProperties();
            ViseLog.e("DeviceControl--- serviceUUid 绑定读写的通知服务" + service4.getUuid().toString());
            ViseLog.e("DeviceControl--- characteristicUUid" + characteristic4.getUuid().toString());
            ViseLog.e("DeviceControl--- charaProp" + charaProp4);
            initNotice(service4, characteristic4, charaProp4);
        }, 2000);

    }

    private void bindUpdateRead() {
        BluetoothGattService dfuService1 = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(DFU_UPDATEING1_SERVICE_UUID);
        BluetoothGattCharacteristic dfucharacter1 = dfuService1.getCharacteristic(DFU_UPDATEING1_CHAR_UUID);
        int dfuCharaProp1 = dfucharacter1.getProperties();
        ViseLog.e("DeviceControl--- serviceUUid 绑定更新读取服务" + dfuService1.getUuid().toString());
        ViseLog.e("DeviceControl--- characteristicUUid" + dfucharacter1.getUuid().toString());
        ViseLog.e("DeviceControl--- charaProp" + dfuCharaProp1);
        initNotice(dfuService1, dfucharacter1, dfuCharaProp1);
    }

    private void bindUpdateWrite() {
        BluetoothGattService dfuService2 = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(DFU_UPDATEING2_SERVICE_UUID);
        BluetoothGattCharacteristic dfucharacter2 = dfuService2.getCharacteristic(DFU_UPDATEING2_CHAR_UUID);
        int dfuCharaProp2 = dfucharacter2.getProperties();
        ViseLog.e("DeviceControl--- serviceUUid 绑定更新写入" + dfuService2.getUuid().toString());
        ViseLog.e("DeviceControl--- characteristicUUid" + dfucharacter2.getUuid().toString());
        ViseLog.e("DeviceControl--- charaProp" + dfuCharaProp2);
        initNotice(dfuService2, dfucharacter2, dfuCharaProp2);
    }

    private void bindCheckUpdateNotice() {
        BluetoothGattService softService = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(SOFT_SERVICE_UUID);
        BluetoothGattCharacteristic softCharacter = softService.getCharacteristic(SOFT_CHAR_UUID);
        int SoftcharaProp = softCharacter.getProperties();
        ViseLog.e("DeviceControl--- serviceUUid" + "检查升级DFU 通知服务");
        ViseLog.e("DeviceControl--- serviceUUid" + softService.getUuid().toString());
        ViseLog.e("DeviceControl--- characteristicUUid" + softCharacter.getUuid().toString());
        ViseLog.e("DeviceControl--- charaProp" + SoftcharaProp);
        initNotice(softService, softCharacter, SoftcharaProp);
    }

    private void bindCheckUpdateReadAndWrite() {
        BluetoothGattService softService = BluetoothDeviceManager.getInstance().getDeviceMirror(currentDevice).getGattService(DFU_CHECK_FOR_UPDATE_SERVICE_UUID);
        BluetoothGattCharacteristic softCharacter = softService.getCharacteristic(DFU_CHECK_FOR_UPDATE_CHAR_UUID);
        int SoftcharaProp = softCharacter.getProperties();
        ViseLog.e("DeviceControl--- serviceUUid" + "检查升级DFU 读写");
        ViseLog.e("DeviceControl--- serviceUUid" + softService.getUuid().toString());
        ViseLog.e("DeviceControl--- characteristicUUid" + softCharacter.getUuid().toString());
        ViseLog.e("DeviceControl--- charaProp" + SoftcharaProp);
        initNotice(softService, softCharacter, SoftcharaProp);
    }

    public interface BleCallBack {

        void receivedData(String msg);

        void disConnected();

    }

}
