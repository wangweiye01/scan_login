package cn.epaylinks.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * 处理自定义字符串转换
 * 
 * @author xiasihua
 * @version [1.0.0, 2017-01-22]
 * @since [channel-gateway/1.0.0]
 */
public class StringUtil
{

    /**
     * 将Map转换为SortedMap
     * @param map
     * @return
     */
    public static SortedMap<String,String> mapToSortedMap(Map<String,String> map)
    {
        SortedMap<String,String> sortedMap = new TreeMap<String, String>();
        if(map != null && map.size()>0)
        {
            Set<String> set = map.keySet();
            for(String key:set)
            {
                sortedMap.put(key, map.get(key));
            }
            
        }
        return sortedMap;
    }

    /**
     * 将sortedMap 转换成key/value使用&链接符连接的字符传,空值不参与转换
     * 
     * @param map
     *            源map
     * @param outStr
     *            连接时需要在map中排除的key
     * @return
     */
    public static String toSignKVStr(SortedMap<String, String> map, String[] outStr)
    {
        return toSignKVStr(map, outStr, true);
    }

    /**
     * 将sortedMap 转换成key/value使用&链接符连接的字符串
     * 
     * @param map
     *            源map
     * @param outStr
     *            连接时需要在map中排除的key
     * @param isOutempty
     *            是否排除值为空字符串的键值对
     * @return
     */
    public static String toSignKVStr(SortedMap<String, String> map, String[] outStr, boolean isOutEmpty)
    {
        StringBuffer sb = new StringBuffer("");
        for (String key : map.keySet())
        {
            String value = map.get(key);
            // key对应的值为NULL或包含排除key，则跳过
            if (value == null || contains(outStr, key))
            {
                continue;
            }
            if (isOutEmpty && StringUtils.isEmpty(value))
            {
                continue;
            }
            sb.append(key).append("=").append(value).append("&");
        }
        String str = sb.toString().substring(0, sb.toString().length() - 1);
        return str;
    }

    /**
     * 将key/value使用&链接符连接的字符串转换成sortedMap
     * 
     * @param str
     * @return
     */
    public static SortedMap<String, String> toSortedMap(String str)
    {
        SortedMap<String, String> map = new TreeMap<String, String>();
        String[] array = str.split("&");
        for (int i = 0; i < array.length; i++)
        {
            map.put(array[i].split("=")[0], array[i].split("=")[1]);
        }
        return map;
    }

    /**
     * 判断数组中是否存在目标字符串
     * 
     * @param array
     * @param str
     * @return ture or false
     */
    public static boolean contains(String[] array, String str)
    {
        if (array == null)
        {
            return false;
        }
        for (String temp : array)
        {
            if (temp.equals(str))
            {
                return true;
            }
        }
        return false;
    }
}
