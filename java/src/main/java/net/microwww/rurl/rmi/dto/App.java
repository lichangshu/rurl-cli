/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.dto;

import java.io.Serializable;

/**
 * 与 server 中的 webapp 数据等价
 *
 * @author changshu.li
 */
public class App implements Serializable {

    private int id;
    private String appName;
    private String name;
    private boolean allLogin;
    private boolean unknownUrl;
    private int loginGroup;
    private String discrption;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllLogin() {
        return allLogin;
    }

    public void setAllLogin(boolean allLogin) {
        this.allLogin = allLogin;
    }

    public boolean isUnknownUrl() {
        return unknownUrl;
    }

    public void setUnknownUrl(boolean unknownUrl) {
        this.unknownUrl = unknownUrl;
    }

    public int getLoginGroup() {
        return loginGroup;
    }

    public void setLoginGroup(int loginGroup) {
        this.loginGroup = loginGroup;
    }

    public String getDiscrption() {
        return discrption;
    }

    public void setDiscrption(String discrption) {
        this.discrption = discrption;
    }
}
