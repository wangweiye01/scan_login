package cn.epaylinks.common;

public class ScanPool
{

	//创建时间
	private Long createTime = System.currentTimeMillis();
	
	//登录状态
	private boolean scanFlag = false;
	
	public boolean isScan(){
		return scanFlag;
	}
	
	public void setScan(boolean scanFlag){
		this.scanFlag = scanFlag;
	}
	
	/**
	 * 获取扫描状态，如果还没有扫描，则等待固定秒数
	 * @param wiatSecond 需要等待的秒数
	 * @return
	 */
	public synchronized boolean getScanStatus(){
		try
		{
			if(!isScan()){ //如果还未扫描，则等待
				this.wait();
			}
			if (isScan())
			{
				return true;
			}
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 扫码之后设置扫码状态
	 */
	public synchronized void scanSuccess(){
		try
		{
			setScan(true);
			this.notifyAll();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void notifyPool(){
		try
		{
			this.notifyAll();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Long createTime)
	{
		this.createTime = createTime;
	}

}
