package top.by.xrh;

import top.by.xrh.util.HttpClientUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		String s = HttpClientUtil.sendGet("user/find");
		System.out.println(s);
	}
}
