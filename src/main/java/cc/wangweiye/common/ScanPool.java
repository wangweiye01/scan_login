package cc.wangweiye.common;

public class ScanPool {

    //创建时间
    private Long createTime = System.currentTimeMillis();

    //登录状态
    private boolean scanFlag = false;

    public boolean isScan() {
        return scanFlag;
    }

    public void setScan(boolean scanFlag) {
        this.scanFlag = scanFlag;
    }

    public synchronized boolean getScanStatus() {
        try {
            if (!isScan()) { //如果还未扫描，则等待
                this.wait();
            }
            if (isScan()) {
                return true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 扫码之后设置扫码状态
     */
    public synchronized void scanSuccess() {
        try {
            setScan(true);
            this.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void notifyPool() {
        try {
            this.notifyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Long getCreateTime() {
        return createTime;
    }
}
