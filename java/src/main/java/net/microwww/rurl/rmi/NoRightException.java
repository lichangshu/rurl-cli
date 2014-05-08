/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi;

/**
 *
 * @author changshu.li
 */
public class NoRightException extends Exception {

	public static final int CODE = -1;
	public int code = 0;

	public NoRightException(int code, String message) {
		super(message);
		this.code = code;
	}

	public NoRightException(String message) {
		super(message);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getErrorMessage() {
		switch (this.getCode()) {
			case -1:
				return "帐号不存在";
			case -2:
				return "密码错误";
			case -3:
				return "没有权限";
		}
		return this.getMessage();
	}
}
