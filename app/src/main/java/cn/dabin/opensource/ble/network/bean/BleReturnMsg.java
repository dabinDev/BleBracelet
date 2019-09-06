package cn.dabin.opensource.ble.network.bean;

/**
 * Project :  BleBracelet.
 * Package name: cn.dabin.opensource.ble.network.bean
 * Created by :  dabin.
 * Created time: 2019/9/4 20:23
 * Changed by :  dabin.
 * Changed time: 2019/9/4 20:23
 * Class description:
 */
public class BleReturnMsg {
    private int time;//T:时间点
    private String distance;// R:距离值
    private String speedX;//X:加速度
    private String speedY;// Y:加速度
    private String speedZ;//Z:加速度

    public BleReturnMsg(String msg) {
      /*  String tempMsg = msg.replace(" ","").toLowerCase();
        if (tempMsg.contains("T:") && tempMsg.contains("R:") && tempMsg.contains("X:") && tempMsg.contains("Y:") && tempMsg.contains("Z:")) {
            tempMsg.replace("T")
        }*/
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSpeedX() {
        return speedX;
    }

    public void setSpeedX(String speedX) {
        this.speedX = speedX;
    }

    public String getSpeedY() {
        return speedY;
    }

    public void setSpeedY(String speedY) {
        this.speedY = speedY;
    }

    public String getSpeedZ() {
        return speedZ;
    }

    public void setSpeedZ(String speedZ) {
        this.speedZ = speedZ;
    }
}
