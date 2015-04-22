package org.jaeyo.dde.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
	private Map<String, Cookie> cookies = new HashMap<String, Cookie>();

	public CookieUtil(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				this.cookies.put(cookies[i].getName(), cookies[i]);
			} // for i
		} // if
	} // INIT

	public static Cookie createCookie(String name, String value) throws UnsupportedEncodingException {
		return new Cookie(name, URLEncoder.encode(value, "euc-kr"));
	} // createCookie

	public static Cookie createCookie(String name, String value, String path, int maxAge)
			throws UnsupportedEncodingException {
		Cookie cookie = createCookie(name, value);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		return cookie;
	} // createCookie

	public static Cookie createCookie(String name, String value, String domain, String path, int maxAge)
			throws UnsupportedEncodingException {
		Cookie cookie = createCookie(name, value, path, maxAge);
		cookie.setDomain(domain);
		return cookie;
	} // createCookie

	public Cookie getCookie(String name) {
		return this.cookies.get(name);
	} // getCookie

	public String getValue(String name) throws UnsupportedEncodingException {
		Cookie cookie = this.cookies.get(name);
		if (cookie == null)
			return null;
		return URLDecoder.decode(cookie.getValue(), "euc-kr");
	} // getValue

	public boolean exists(String name) {
		return this.cookies.get(name) != null;
	} // exists
} // class