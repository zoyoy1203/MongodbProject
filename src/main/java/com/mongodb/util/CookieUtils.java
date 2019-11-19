package com.mongodb.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;

public class CookieUtils {
	private static final String KEY = ":cookie@ybzy.com123!";
	/**
	 * 指令浏览器创建cookie文件用的方法
	 * @param username : 放到cookie信息你 用户名
	 * @param req
	 * @param resp : 调用addCoolie方法的response对象
	 * @param sec : 设置cookie失效的时间,单位秒
	 */
	public static void createCookie(String username, HttpServletRequest req, HttpServletResponse resp, int sec) {
		Cookie userCookie = new Cookie("userKey", username);
		Cookie ssidCookie = new Cookie("ssid", md5Encrypt(username)) ;
		userCookie.setMaxAge(sec);
		ssidCookie.setMaxAge(sec);
		resp.addCookie(userCookie);
		resp.addCookie(ssidCookie);
	}

	/**
	 * 这个方法的作用就加密,把一个明文字符串,加密那个都看不懂的密文
	 * @param ss : 等待被加密的明文
	 * @return
	 */
	public static String md5Encrypt(String ss) {
		ss = ss==null?"":ss+KEY;
		char[] md5Digist = { '0','1', '2', '3', '4', '1', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; // 字典
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); // md5 sha1
			byte[] ssarr = ss.getBytes();
			md.update(ssarr); // 把明文放到加密类MessageDigest的对象实例去出,更新数据
			byte[] mssarr = md.digest();  // 这里就是真正加密了  :

			int len = mssarr.length;
			char[]  str = new char[len*2];
			int k = 0; //记数

			for(int i=0;i<len;i++) {
				byte b = mssarr[i];  //0101011  111111 01
				str[k++] = md5Digist[b >>> 4 & 0xf];
				str[k++] = md5Digist[b & 0xf];
			}
			return new String(str);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}