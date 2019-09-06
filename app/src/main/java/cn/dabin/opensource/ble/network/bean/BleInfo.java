package cn.dabin.opensource.ble.network.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/9/4 0:31
 * Changed by :  dabin.
 * Changed time: 2019/9/4 0:31
 * Class description:
 */

public class BleInfo extends LitePalSupport {
    private String deviceName = "";
    @Column(unique = true)
    private String macAddress = "";
    private int currentBattery = 100;
    private String lastSysTime = "";
    private int distance = 100;
    private int shock = 100;
    private int tipTime = 100;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getShock() {
        return shock;
    }

    public void setShock(int shock) {
        this.shock = shock;
    }

    public int getTipTime() {
        return tipTime;
    }

    public void setTipTime(int tipTime) {
        this.tipTime = tipTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getCurrentBattery() {
        return currentBattery;
    }

    public void setCurrentBattery(int currentBattery) {
        this.currentBattery = currentBattery;
    }

    public String getLastSysTime() {
        return lastSysTime;
    }

    public void setLastSysTime(String lastSysTime) {
        this.lastSysTime = lastSysTime;
    }
}
