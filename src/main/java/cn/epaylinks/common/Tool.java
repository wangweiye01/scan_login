package cn.epaylinks.common;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Clob;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public final class Tool {
	static int fourNum = 0;

	public synchronized static int getFournum() {
		fourNum++;
		if (fourNum == 1000) {
			fourNum = 1;
		}
		return fourNum;
	}

	/**
	 * 生成主键ID
	 * 
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String createPramaryKey() throws Exception {
		StringBuffer result = new StringBuffer("");
		String fourStr = numberFormatI(getFournum());
		String dateStr = dateToDateS(new Date());
		result.append(dateStr);
		result.append(fourStr);
		return result.toString();
	}

	static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	// 将iSource转为长度为iArrayLen的byte数组，字节数组的低位是整型的低字节位
	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);

		}
		return bLocalArr;
	}

	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	/**
	 * 得到一个唯一标识的UUID
	 * 
	 * @return
	 * @author kevin.xia
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "").toUpperCase();
	}

	/**
	 * 验证字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotNullOrEmpty(String s) {
		if (null == s) {
			return false;
		}
		if (s.trim().equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符是否英文半角字符或标点 32 空格 33-47 标点 48-57 0~9 58-64 标点 65-90 A~Z 91-96 标点
	 * 97-122 a~z 123-126 标点
	 * 
	 * @param c
	 * @return
	 * @author kevin.xia
	 */
	public static boolean isBjChar(char c) {
		int i = (int) c;
		return i >= 32 && i <= 126;
	}

	/**
	 * 判断字符是否全角字符或标点 全角字符 - 65248 = 半角字符 全角空格例外
	 * 
	 * @param c
	 * @return
	 * @author kevin.xia
	 */
	public static boolean isQjChar(char c) {
		if (c == '\u3000')
			return true;

		int i = (int) c - 65248;
		if (i < 32)
			return false;
		return isBjChar((char) i);
	}

	/**
	 * 判断输入的字符串是否为null,如果为null，则将输入字符串用""替换。
	 * 
	 * @param nvlString
	 *            输入字符串
	 * @return
	 */
	public static String nvl(String nvlString) {
		return nvlString == null ? "" : nvlString;
	}

	/**
	 * 判断输入的对象是否为null,如果为null，则将输入字符串用""替换。
	 * 
	 * @param nvlString
	 *            输入对象
	 * @return
	 * @author kevin.xia
	 */
	public static String nvl(Object nvlString) {
		return nvlString == null ? "" : nvlString.toString();
	}

	/**
	 * 判断输入的字符串是否为null,如果为null，则将输入字符串用缺省值字符串替换。
	 * 
	 * @param nvlString
	 *            输入字符串
	 * @param defaultValue
	 *            缺省值字符串
	 * @return
	 */
	public static String nvl(String nvlString, String defaultValue) {
		return nvlString == null ? defaultValue : nvlString;
	}

	/**
	 * 判断输入的对象是否为null,如果为null，则将输入字符串用缺省值字符串替换。
	 * 
	 * @param nvlString
	 *            输入对象
	 * @param defaultValue
	 *            缺省值字符串
	 * @return
	 * @author kevin.xia
	 */
	public static String nvl(Object nvlString, String defaultValue) {
		return nvlString == null ? defaultValue : nvlString.toString();
	}

	/**
	 * 将字符串中的全角字符转换为半角
	 * 
	 * @param s
	 * @return
	 * @author kevin.xia
	 */
	public static String toBj(String s) {
		if (s == null || s.equals(""))
			return s;

		StringBuffer sb = new StringBuffer(s.length());
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c == '\u3000')
				sb.append('\u0020');
			else if (isQjChar(c))
				sb.append((char) ((int) c - 65248));
			else
				sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 将字符串中的半角字符转换为全角
	 * 
	 * @param s
	 * @return
	 * @author kevin.xia
	 */
	public static String toQj(String s) {
		if (s == null || s.equals(""))
			return s;

		StringBuffer sb = new StringBuffer(s.length());
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (c == '\u0020')
				sb.append('\u3000');
			else if (isBjChar(c))
				sb.append((char) ((int) c + 65248));
			else
				sb.append(c);
		}
		return sb.toString();
	}
	
	public static boolean isHanzi(String str){  
        if(str.getBytes().length == str.length()){  
            System.out.println("无汉字"); 
            return false;
        }else{  
            System.out.println("有汉字");  
            return true;
        }  
          
    }  


	/**
	 * MD5加密
	 * 
	 * @param s
	 * @return
	 * @author kevin.xia
	 */
	public final static String MD5(String s) {
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(s.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();

			char str[] = new char[md.length * 2];
			for (int i = 0, k = 0; i < md.length; i++) {
				str[k++] = hexDigits[md[i] >>> 4 & 0xf];
				str[k++] = hexDigits[md[i] & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param num
	 * @return
	 * @author kevin.xia
	 */
	public static double numberRound(double num) {
		if (num > 0) {
			num += 0.0000001;
		} else {
			num -= 0.0000001;
		}
		BigDecimal b = new BigDecimal(num);
		double f = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
		/*
		 * long l=(long)((num+0.005)*100); return l/100.0;
		 */
	}

	/**
	 * 五舍六入保留一位小数
	 * 
	 * @param num
	 * @return
	 * @author kevin.xia
	 */
	public static double numberFiveToSix(double num) {
		if (num > 0) {
			num = num + 0.0000001 - 0.01;
		} else {
			num = num - 0.0000001 + 0.01;
		}
		BigDecimal b = new BigDecimal(num);
		double f = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;

	}

	/**
	 * 字符串转换为decimal
	 * 
	 * @param num
	 * @return
	 * @author kevin.xia
	 */
	public static BigDecimal stringToDecimal(String num) {
		if(!Tool.isNotNullOrEmpty(num)){
			num = "0";
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		BigDecimal b = new BigDecimal(df.format(Double.parseDouble(num)));
		return b;
	}

	/**
	 * 数字左补0，总长度length
	 * 
	 * @param num
	 * @return
	 * @author kevin.xia
	 */
	public static String numberFormat(int num, int length) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		StringBuffer format = new StringBuffer("");
		for (int i = 0; i < length; i++) {
			format.append("0");
		}
		df.applyPattern(format.toString());
		return df.format(num);
	}

	/**
	 * 数字格式A 格式：整数部分 + 小数点 + 小数部分
	 * 整数部分7位，不足7位的，左面补0，补足7位。小数部分2位，不足2位的，右面补0，补足2位。小数点占1位。 例：
	 * 0456734.35；0456734.20； 0456734.00；0000000.00
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatA(double num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("0000000.00");
		return df.format(num);
	}

	/**
	 * 数字格式B 格式：整数部分 + 小数点 + 小数部分 整数部分m位，小数部分2位，不足2位的，右面补0，补足2位。小数点占1位。 例：
	 * 56734.35；4.20； 6734.00;
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatB(double num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("0.00");
		return df.format(num);
	}

	/**
	 * 数字格式D 格式：整数部分 整数部分4位，不足4位的，左面补0，补足4位。 例： 034； 333；
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatD(int num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("0000");
		return df.format(num);
	}

	/**
	 * 数字格式I 格式：整数部分 整数部分3位，不足3位的，左面补0，补足3位。 例： 033； 333；
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatI(int num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("000");
		return df.format(num);
	}

	/**
	 * 数字格式E 格式：整数部分 整数部分8位，不足8位的，左面补0，补足3位。 例： 00000033； 04457333；
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatE(int num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("00000000");
		return df.format(num);
	}

	/**
	 * 格式化数值输出 如果是整数,小数部分补两个零. 如是是一位小数,小数部分补一个零. 其它保留五位小数,整数部分原样输出.
	 * 
	 * @param num
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String numberFormatTwo(double num) throws Exception {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("#0.00###");
		return df.format(num);
	}

	/**
	 * 产生指定数量的空格字符串
	 * 
	 * @param number
	 * @return
	 * @author kevin.xia
	 */
	public static String spaceString(int number) {
		if (number <= 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < number; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
	}

	/**
	 * 产生指定总数量右补空格的字符串
	 * 
	 * @param number
	 * @return
	 * @author kevin.xia
	 */
	public static String spaceToRight(String str, int total) {
		int lenth = str.length();
		int number = total - lenth;
		if (number < 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer(str);
			for (int i = 0; i < number; i++) {
				sb.append(" ");
			}
			return sb.toString();
		}
	}

	/**
	 * 产生指定总数量右补0的字符串
	 * 
	 * @param number
	 * @return
	 * @author kevin.xia
	 */
	public static String zeroToRight(String str, int total) {
		int lenth = str.length();
		int number = total - lenth;
		if (number < 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer(str);
			for (int i = 0; i < number; i++) {
				sb.append("0");
			}
			return sb.toString();
		}
	}

	/**
	 * 产生指定总数量左补0的字符串
	 * 
	 * @param number
	 * @return
	 * @author kevin.xia
	 */
	public static String zeroToLeft(String str, int total) {
		int lenth = str.length();
		int number = total - lenth;
		if (number < 0) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer("");
			for (int i = 0; i < number; i++) {
				sb.append("0");
			}
			sb.append(str);
			return sb.toString();
		}
	}

	/**
	 * 把指定数值按指定位数(8),保存两位小数,不足位数前补零,超出指定位数则截取指定位数
	 * 
	 * @param num
	 * @return
	 * @author kevin.xia
	 */
	public static String formatDouble(double num) {
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		df.applyPattern("0.00");
		String value = df.format(num);
		if (value.length() >= 8) {
			value = value.substring(0, 8);
		} else {
			value = spaceString(8 - value.length()) + value;
		}
		return value;
	}

	/**
	 * 字符串日期时间yyyy-MM-dd HH:mm:ss转换成字符串日期YYYYMMDD
	 * 
	 * @param string
	 * @return
	 * @throws Exception
	 * @author kevin.xia
	 */
	public static String getYYYYMMDD(String string) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(string.substring(0, 4)).append(string.substring(5, 7))
				.append(string.substring(8, 10));
		return sb.toString();
	}

	/**
	 * 字符串转换成日期带格式化字符串
	 * 
	 * @param string
	 *            输入的字符串
	 * @param dateStyle
	 *            指定的格式
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date stringToDate(String string, String dateStyle)
			throws ParseException {
		SimpleDateFormat format = getSDF4Date(dateStyle);
		return format.parse(string);
	}

	/**
	 * 字符串转换成日期带格式化字符串
	 * 
	 * @param string
	 *            输入的字符串
	 * @param dateStyle
	 *            指定的格式
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date stringToDateTime(String string, String dateStyle)
			throws ParseException {
		SimpleDateFormat format = getSDF4DateTime(dateStyle);
		return format.parse(string);
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param string
	 *            格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date stringToDate(String string) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.parse(string);
	}

	/**
	 * 字符串转换成日期
	 * 
	 * @param string
	 *            格式：yyyy-MM-dd
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date stringToDate10(String string) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.parse(string);
	}

	/**
	 * 字符串转换成日期(带毫秒)
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date stringToDateS(String string) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return format.parse(string);
	}

	/**
	 * 日期转换成字符串(带毫秒) yyyyMMddHHmmsss
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static String dateToDateS(Date date) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(date);
	}

	/**
	 * 日期转换成字符串MMdd
	 * 
	 * @param string
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static String dateToStringMMDD(Date date) throws ParseException {
		DateFormat format = new SimpleDateFormat("MMdd");
		return format.format(date);
	}

	/**
	 * 带S日期转换成不带S
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date dateSToDate(Date date) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = format.format(date).substring(0, 18);
		return format.parse(strDate);
	}

	/**
	 * 日期转换成字符串yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 * @author kevin.xia
	 */
	public static String dateToString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	/**
	 * 日期按指定格式转换成字符串
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @author kevin.xia
	 */
	public static String dateToString(Date date, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/**
	 * 日期时间转换成日期
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 * @author kevin.xia
	 */
	public static Date dateTimeToDate(Date date) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = format.format(date).substring(0, 10);
		format = new SimpleDateFormat("yyyy-MM-dd");
		return format.parse(strDate);
	}

	/**
	 * 根据日期计算年龄
	 * 
	 * @param birthday
	 * @return
	 * @author kevin.xia
	 */
	public static Integer dateToAge(Date birthday) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birthday);
		int year = calendar.get(calendar.YEAR);
		calendar.setTime(new Date());
		return calendar.get(calendar.YEAR) - year;
	}

	/**
	 * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
	 * 
	 * @author patriotlml
	 * @param String
	 *            origin, 原始字符串
	 * @param int len, 截取长度(一个汉字长度按2算的)
	 * @return String, 返回的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String subString(String origin, int len)
			throws UnsupportedEncodingException {
		if (origin == null || origin.equals("")) {
			return "";
		}
		byte[] strByte = new byte[len];
		if (len > origin.getBytes("UTF-8").length) {
			return origin;
		}
		System.arraycopy(origin.getBytes("UTF-8"), 0, strByte, 0, len);
		int count = 0;
		for (int i = 0; i < len; i++) {
			int value = (int) strByte[i];
			if (value < 0) {
				count++;
			}
		}
		if (count % 2 != 0) {
			// len = (len == 1) ? ++len : --len;
			len--;
		}
		return new String(strByte, 0, len);
	}

	/**
	 * 格式化字符串为html格式 如 空格转换为&nbsp；\n 转换为<br/>
	 * 
	 * @param str
	 * @return
	 * @author kevin.xia
	 */
	public static String formatString(String str) {
		if (str == null)
			return str;
		return str.replaceAll(" ", "&nbsp;").replaceAll("\r\n|\r|\n", "<br />");
	}

	/**
	 * 判断是否是数字
	 * 
	 * @param s
	 * @return
	 * @author kevin.xia
	 */
	public static boolean isNumData(String s) {
		boolean tag = false;
		for (int j = 0; j < s.length(); j++) {
			if (!(s.charAt(j) >= 48 && s.charAt(j) <= 57)) {
				tag = false;
			} else {
				tag = true;
			}
		}
		return tag;
	}

	/**
	 * 判断传入参数是否为NUll,不是返回Long.
	 * 
	 * @param str
	 *            传入参数
	 * @return Long
	 */
	public static Long stringToLong(String str) {
		if (str == null) {
			return 0L;
		} else {
			return Long.valueOf(str);
		}
	}

	/**
	 * 判断传入参数是否为NUll,不是返回Date.
	 * 
	 * @param str
	 *            传入参数
	 * @return Long
	 */
	public static String dateSubString(String strDate) {
		if (strDate.trim().length() == 0) {
			return "";
		} else {
			if (strDate.trim().length() < 19) {
				return strDate.trim();
			} else {
				return strDate.trim().substring(0, 19);
			}

		}
	}

	/**
	 * 日期格式样式。可输入如下值：
	 * <p>
	 * "-"：　　yyyy-MM-dd；
	 * <p>
	 * "/"：　　yyyy/MM/dd。
	 * <p>
	 * "SC"：　　中国式日期格式。
	 * <p>
	 * "US"：　　美国式日期格式。
	 * <p>
	 * ""为默认格式。
	 * 
	 * @param dateStyle
	 *            日期格式样式
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getSDF4Date(String dateStyle) {
		SimpleDateFormat sdf = null;
		if (dateStyle == null)
			dateStyle = "";
		if (dateStyle.equals("-")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		} else if (dateStyle.equals("/")) {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		} else if (dateStyle.equals("SC")) {
			sdf = new SimpleDateFormat("yyyy年MM月dd日");
		} else if (dateStyle.equals("US")) {
			sdf = new SimpleDateFormat("MMM dd,yyyy", Locale.US);
		} else if (dateStyle.equals("")) {
			sdf = new SimpleDateFormat();
		} else {
			// 自定义日期格式
			sdf = new SimpleDateFormat(dateStyle);
		}
		return sdf;
	}

	/**
	 * 日期格式样式。可输入如下值：
	 * <p>
	 * "-"：　　yyyy-MM-dd；
	 * <p>
	 * "/"：　　yyyy/MM/dd。
	 * <p>
	 * "SC"：　　中国式日期格式。
	 * <p>
	 * "US"：　　美国式日期格式。
	 * <p>
	 * ""为默认格式。
	 * 
	 * @param dateStyle
	 *            日期格式样式
	 * @return SimpleDateFormat
	 */
	private static SimpleDateFormat getSDF4DateTime(String dateStyle) {
		SimpleDateFormat sdf = null;
		if (dateStyle == null)
			dateStyle = "";
		if (dateStyle.equals("-")) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if (dateStyle.equals("/")) {
			sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		} else if (dateStyle.equals("SC")) {
			sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		} else if (dateStyle.equals("US")) {
			sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm:ss ", Locale.US);
		} else if (dateStyle.equals("")) {
			sdf = new SimpleDateFormat();
		} else {
			// 自定义日期格式
			sdf = new SimpleDateFormat(dateStyle);
		}
		return sdf;
	}

	/**
	 * 将数据库中的date对象转换为yyyy-mm-dd的字符串
	 * 
	 * @param obj
	 * @return
	 * @author kevin.xia
	 */
	public static String date2str(Object obj) {
		String dateStr = "";
		if (obj != null) {
			dateStr = obj.toString();
			if (dateStr != null) {
				if (dateStr.length() > 10)
					dateStr = dateStr.substring(0, 10);
			}
		}
		return dateStr;
	}

	/**
	 * Clob转换为String
	 * 
	 * @param clob
	 * @return
	 * @author kevin.xia
	 */
	public static String clob2string(Clob clob) {
		if (clob == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer(65535); // 64K
		Reader clobStream = null;
		try {
			clobStream = clob.getCharacterStream();
			char[] b = new char[60000];// 每次获取60K
			int i = 0;
			while ((i = clobStream.read(b)) != -1) {
				sb.append(b, 0, i);
			}
		} catch (Exception ex) {
			sb = null;
		} finally {
			try {
				if (clobStream != null)
					clobStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (sb == null)
			return null;
		else
			return sb.toString();
	}

	/**
	 * Capitalize 首字母转换为大写
	 * 
	 * @param s
	 * @return
	 */
	public static String capitalize(String s) {
		char ch[];
		ch = s.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		String newString = new String(ch);
		return newString;
	}

	public static String getRandomSixNum() {
		Random ran = new Random();
		int n = Math.abs(ran.nextInt(900000) + 100000);
		return n + "";
	}

	/**
	 * 得到本月第一天的yyyy-MM-dd格式字符串
	 * 
	 * @return
	 */
	public static String getFirstDayStrOfMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		return dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到本月第一天的date类型数据，并且时间是0点0分0秒000毫秒
	 * 
	 * @return
	 */
	public static Date getFirstDayOfMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// dateToString(cal.getTime(),"yyyy-MM-dd HH:mm:ss.SSS");
		return cal.getTime();
	}

	/**
	 * 得到与本月相隔N个月的月份第一天的yyyy-MM-dd格式字符串
	 * 
	 * @param relativeNum
	 * @return
	 */
	public static String getFirstDayStrOfMonthRelativeThisMonth(int relativeNum) {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.add(Calendar.MONTH, relativeNum);
		return dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到与本月相隔N个月的月份第一天的date类型数据，并且时间是0点0分0秒000毫秒
	 * 
	 * @param relativeNum
	 * @return
	 */
	public static Date getFirstDayOfMonthRelativeThisMonth(int relativeNum) {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, relativeNum);
		return cal.getTime();
	}

	/**
	 * 得到本月最后一天的yyyy-MM-dd格式字符串
	 * 
	 * @return
	 */
	public static String getLastDayStrOfMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到本月最后一天的date类型数据，并且时间是0点0分0秒000毫秒
	 * 
	 * @return
	 */
	public static Date getLastDayOfMonth() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 得到与本月相隔N个月的月份最后一天的yyyy-MM-dd格式字符串
	 * 
	 * @param relativeNum
	 * @return
	 */
	public static String getLastDayStrOfMonthRelativeThisMonth(int relativeNum) {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.add(Calendar.MONTH, relativeNum + 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到与本月相隔N个月的月份最后一天的date类型数据，并且时间是0点0分0秒000毫秒
	 * 
	 * @param relativeNum
	 * @return
	 */
	public static Date getLastDayOfMonthRelativeThisMonth(int relativeNum) {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.MONTH, relativeNum + 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 得到今天的起点时间即今天的0点
	 * 
	 * @return
	 */
	public static Date getTodayStartTime() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 得到今天的起点时间即今天的0点yyyy-MM-dd格式字符串
	 * 
	 * @return
	 */
	public static String getTodayStartTimeStr() {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		return dateToString(cal.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到和今天间隔N天的那一天的起点时间
	 * 
	 * @param relativeNum
	 * @return
	 */
	public static Date getRelativeTodayStartTime(int relativeNum) {
		Calendar cal = Calendar.getInstance();// 获取当前日期
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, relativeNum);
		return cal.getTime();
	}

	/**
	 * 得到与传入时间间隔N天的日期date，时分秒保持一致
	 * 
	 * @param date
	 * @param relativeNum
	 * @return
	 */
	public static Date getRelativeDayOfDate(Date date, int relativeNum) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, relativeNum);
		return cal.getTime();
	}

	/**
	 * 得到与传入时间间隔N个月的日期date，时分秒保持一致
	 * 
	 * @param date
	 * @param relativeNum
	 * @return
	 */
	public static Date getRelativeMonthOfDate(Date date, int relativeNum) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, relativeNum);
		return cal.getTime();
	}

	/**
	 * 得到本月的第一天
	 * 
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));

		return dateToString(calendar.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 得到本月的最后一天
	 * 
	 * @return
	 */
	public static String getMonthLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return dateToString(calendar.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 将传入的字符串转成固定格式的字符串 格式：4位传入字符串的长度+字符串
	 * 
	 * @param str
	 * @param isAll
	 *            是否计算本身4位的长度
	 * @return
	 * @author kevin.xia
	 */
	public static String getSocketFormatStr(String str, boolean isAll)
			throws Exception {
		String result = "";
		int length = 0;
		if (isAll) {
			length = str.getBytes().length + 4;
		} else {
			length = str.getBytes().length;
		}
		String head = Tool.numberFormatD(length);
		result = head + str;
		return result;
	}

	/**
	 * 得到与传入String时间间隔N天的日期date数据，格式都是"yyyy—MM-dd"
	 * 
	 * @param date
	 * @param relativeNum
	 * @return
	 * @throws ParseException
	 */
	public static Date getRelativeDayOfStrDate(String date, int relativeNum)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(stringToDate10(date));
		cal.add(Calendar.DAY_OF_MONTH, relativeNum);
		return cal.getTime();
	}




	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	 public String getMAC() { 
		 String mac = null; 
		 try { 
		 Process pro = Runtime.getRuntime().exec("cmd.exe /c ipconfig/all"); 
		 
	 InputStream is = pro.getInputStream(); 
	 BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
	 String message = br.readLine(); 
		 
		 int index = -1; 
		 while (message != null) { 
		 if ((index = message.indexOf("Physical Address")) > 0) { 
		 mac = message.substring(index + 36).trim(); 
		 break; 
		 } 
		 message = br.readLine(); 
		 } 
		 System.out.println(mac); 
		 br.close(); 
		 pro.destroy(); 
		 } catch (IOException e) { 
		 System.out.println("Can't get mac address!"); 
		 return null; 
		 } 
		 return mac; 
		 } 
	 
	public static Cookie getLoginCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		System.out.println("遍历cookie");
		Cookie cookie = null;
		if(cookies != null){
			for(int i=0;i<cookies.length;i++){
				System.out.println("cookieName:"+cookies[i].getName()+",cookiesValue:"+cookies[i].getValue());
				if(cookies[i].getName().equals("CERLOGIN") && !Tool.nvl(cookies[i].getValue()).equals("")){
					System.out.println("找到登录cookie：cookieName:"+cookies[i].getName()+",cookiesValue:"+cookies[i].getValue());
					cookie = cookies[i];
				}
			}
		}
		return cookie;
	}
	
	
	
	
	public static String invokeHttpUrl(String urlStr,HashMap paramMap){
		URL url = null;
		HttpURLConnection httpConn = null;
		String sTotalString = "";
		InputStream is = null;
		OutputStream out = null;
		BufferedReader reader = null;
		try {
			url = new URL(urlStr);
			System.out.println("请求地址："+urlStr);
			// 以post方式请求
			httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setDoOutput(true);
			httpConn.setRequestMethod("POST");
			out = httpConn.getOutputStream();
			Iterator its = paramMap.keySet().iterator();
			String tmpStr="";
			while(its.hasNext()){
				String keyName = (String)its.next();
				tmpStr += keyName+"="+paramMap.get(keyName)+"&";
			}
			tmpStr = tmpStr.substring(0,tmpStr.length()-1);
			System.out.println(tmpStr);
			out.write(tmpStr.getBytes());
			out.flush();
			out.close();

			// 获取响应代码
			int code = httpConn.getResponseCode();
			
			String msg = httpConn.getResponseMessage();
			System.out.println("msg:"+msg);
			//读取响应内容
	        String sCurrentLine = "";  
	        if (code == 200)
	        {
	            is = httpConn.getInputStream();
	            
	            reader = new BufferedReader(
	                    new InputStreamReader(is));
	            while ((sCurrentLine = reader.readLine()) != null){
	                if (sCurrentLine.length() > 0){
	                    sTotalString = sTotalString + sCurrentLine.trim();
	                }
	            }
	            
	            is.close();
	            reader.close();
	        } else
	        {
	            sTotalString = "远程服务器连接失败,错误代码:" + code;

	        }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {			
					e.printStackTrace();
				}
			}
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpConn != null){
				httpConn.disconnect();
			}
		}
		
		return sTotalString;
	}
	
	/**
	 * 得到字符串中第一串数字
	 * @param str
	 * @return
	 */
	public static String getFristNums(String str) {
		str=str.trim();
		 String str2="";
		 boolean isFrist = false;
		 if(str != null && !"".equals(str)){
			 for(int i=0;i<str.length();i++){
				 if(str.charAt(i)>=48 && str.charAt(i)<=57){
					 if(!isFrist){
						 isFrist = true;
						 str2+=str.charAt(i);
					 }else{
						 if(str.charAt(i-1)>=48 && str.charAt(i-1)<=57){
							 str2 +=str.charAt(i);
						 }else{
							 break;
						 }
					 }
				 }
			 }
		 }
		return str2;
	}
	
	/**
	 * 判断周次是否是一周
	 * @param yzc
	 * @return
	 */
	public static boolean isOneWeek(String yzc)  {
		// TODO Auto-generated method stub
		String ksz = yzc.substring(0,2);
		String jsz = yzc.substring(3,5);
		return ksz.equals(jsz);
	}
	
	

	public static void getPxList(List pxs, String pxzds) {
		// TODO Auto-generated method stub
		String[] zds=pxzds.split(";");
		for(int i=0;i<zds.length;i++){
			String[] fileds = zds[i].split("\\|");
			Map map = new HashMap();
			map.put("key", fileds[0]);
			map.put("value", fileds[1]);
			pxs.add(map);
		}
		
	}
	
	public static String BigDecimalToString(Object obj,String type){
		String str="";
		if(obj!=null){
			if("double".equals(type)){
				double num=((BigDecimal)obj).doubleValue();
				str=num+"";
			}else if("int".equals(type)){
				str = Tool.nvl((BigDecimal)obj);
			}
		}
		return str;
	}

	public static boolean currentDateTimeIsInDateTimes(Date date1,Date date2){
		String date = Tool.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
		String dateo = Tool.dateToString(date1,"yyyy-MM-dd HH:mm:ss");
		String daten = Tool.dateToString(date2,"yyyy-MM-dd HH:mm:ss");
		boolean flag = false;
		if(date.compareTo(dateo)>=0 && date.compareTo(daten) <=0){
			flag = true;
		}
		return flag;
	}
	
	
	public static final byte[] input2byte(InputStream inStream)  
	    throws IOException {  
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
		byte[] buff = new byte[100];  
		int rc = 0;  
		while ((rc = inStream.read(buff, 0, 100)) > 0) {  
			swapStream.write(buff, 0, rc);  
		}  
		byte[] in2b = swapStream.toByteArray();  
		return in2b;  
	}  
	
	
	
	public static <T> String getType(T t){ 
	    if(t instanceof String){ 
	        return "string"; 
	    }else if(t instanceof Integer){ 
	        return "int"; 
	  }else if(t instanceof Double){
	      return "double";
	    }else
	        return "do not know"; 
	}
	
	// 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }

}
