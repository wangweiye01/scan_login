package cn.epaylinks.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PoolCache
{
	//缓存超时时间 10分钟
	private static Long timeOutSecond = 600L;
	
	//每半小时清理一次缓存
	private static Long cleanIntervalSecond = 1800L;
	
	public static Map<String, ScanPool> cacheMap = new HashMap<String, ScanPool>();
	
	static{
		new Thread(new Runnable()
		{
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				while (true)
				{
					try
					{
						Thread.sleep(cleanIntervalSecond*1000);
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					clean();
				}
			}
			
			public void clean(){
				if(cacheMap.keySet().size() > 0){
					Iterator<String> iterator = cacheMap.keySet().iterator();
					while (iterator.hasNext())
					{
						String key = iterator.next();
						ScanPool pool = cacheMap.get(key);
						if(System.currentTimeMillis() - pool.getCreateTime() > timeOutSecond * 1000){
							cacheMap.remove(key);
						}
					}
				}
			}
		}).start();
	}

}
