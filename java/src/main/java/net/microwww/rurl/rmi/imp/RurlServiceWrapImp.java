/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.imp;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.microwww.rurl.rmi.NoRightException;
import net.microwww.rurl.rmi.RurlRightService;
import net.microwww.rurl.rmi.RurlServiceWrap;
import net.microwww.rurl.rmi.dto.App;
import net.microwww.rurl.rmi.dto.Employe;
import net.microwww.rurl.rmi.dto.RightURL;

/**
 *
 * @author changshu.li
 */
class RurlServiceWrapImp implements RurlServiceWrap {

    private String appname;
    private RurlRightService service;

    @Override
    public Employe login(String account, String password) throws NoRightException {
        Map<String, Object> map = service.login(account, password);
        if (service.hasLoginRight(appname, account)) {
            try {
                JSONObject json = (JSONObject) JSONObject.toJSON(map);
                return JSONObject.toJavaObject(json, Employe.class);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new NoRightException(-3, String.format("您（%s）没有登录该应用（%s）的权限!", account, appname));
    }

    @Override
    public List<RightURL> listUrlRight(String account) {
        Map<String, Object>[] list = service.listAccountUrlRight(appname, account);
        List<RightURL> result = new ArrayList();
        for (Map<String, Object> map : list) {
            JSONObject json = (JSONObject) JSONObject.toJSON(map);
            RightURL url = JSONObject.toJavaObject(json, RightURL.class);
            result.add(url);
        }
        return result;
    }

    public List<RightURL> listAppURL() {
        Map<String, Object>[] list = service.listUrlRight(appname);
        List<RightURL> result = new ArrayList();
        for (Map<String, Object> map : list) {
            JSONObject json = (JSONObject) JSONObject.toJSON(map);
            RightURL url = JSONObject.toJavaObject(json, RightURL.class);
            result.add(url);
        }
        return result;
    }

    @Override
    public List<RightURL> listMenu(String account) {
        List<RightURL> result = new ArrayList();
        List<RightURL> list = listUrlRight(account);
        for (RightURL url : list) {
            if (url.getType() == 1) {
                result.add(url);
            }
            if (url.getType() == 2) {
                result.add(url);
            }
        }
        Collections.sort(result, new Comparator<RightURL>() {
            public int compare(RightURL o1, RightURL o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        return result;
    }

    @Override
    public List<RightURL> saveUrlRight(Set<String> set) {
        Map[] list = service.saveURL(appname, set.toArray(new String[set.size()]));
        List<RightURL> result = new ArrayList();
        for (Object map : list) {
            JSONObject json = (JSONObject) JSONObject.toJSON(map);
            RightURL url = JSONObject.toJavaObject(json, RightURL.class);
            result.add(url);
        }
        return result;
    }

    @Override
    public App getApplication() {
        Map map = service.getApplication(appname);
        JSONObject json = (JSONObject) JSONObject.toJSON(map);
        return JSONObject.toJavaObject(json, App.class);
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public RurlRightService getService() {
        return service;
    }

    public void setService(RurlRightService service) {
        this.service = service;
    }
}
