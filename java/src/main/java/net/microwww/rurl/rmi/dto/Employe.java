/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.dto;

import java.io.Serializable;

/**
 * 于service 中的 Emloye 等价
 *
 * @author changshu.li
 */
public class Employe implements Serializable {

    /**
     * 为了兼容，为字符串。
     * 会被存储到 Account 表中 的 account
     * 会用来作为 js dom 对象的ID使用，如果含有特殊字符，可能导致 JS 错误，用户无法插入到数据库
     * 修改 js 的 getstaffid 方法（有多处）。
     */
    private String account;
    /**
     * 会被存储到 Account 表中 的 name 页面中显示名字使用
     */
    private String name;
    private Object other;

    public String getAccount() {
        return account;
    }

    public void setAccount(String id) {
        this.account = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    /**
     * 用户重新登录的时候 可以获取新的key 而不需要去手动去清除缓存
     */
    String cacheKey = "" + super.hashCode();

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() {
        return this.cacheKey;
    }
}
