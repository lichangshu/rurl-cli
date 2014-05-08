/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.imp;

import com.caucho.hessian.client.HessianProxyFactory;
import java.net.MalformedURLException;
import java.util.Map;
import net.microwww.rurl.rmi.NoRightException;
import net.microwww.rurl.rmi.RurlRightService;

/**
 *
 * @author changshu.li
 */
class RurlRightServiceProxy implements RurlRightService {

    private RurlRightService service;
    public static final String path = "/right.hessian";

    public RurlRightServiceProxy(String url) {
        try {
            HessianProxyFactory factory = new HessianProxyFactory();
            factory.setOverloadEnabled(true);
            service = (RurlRightService) factory.create(RurlRightService.class, url.trim() + path);
        } catch (MalformedURLException ex) {
            throw new RuntimeException("连接权限服务器出错,请检查配置地址：" + url, ex);
        }
    }

    @Override
    public Map<String, Object> login(String account, String password) throws NoRightException {
        return service.login(account, password);
    }

    @Override
    public Map<String, Object>[] listAccountUrlRight(String appname, String account) {
        return service.listAccountUrlRight(appname, account);
    }

    @Override
    public Map<String, Object>[] listUrlRight(String appname) {
        return service.listUrlRight(appname);
    }

    @Override
    public Map<String, Object> getApplication(String appname) {
        return service.getApplication(appname);
    }

    @Override
    public boolean hasLoginRight(String appname, String account) {
        return service.hasLoginRight(appname, account);
    }

    @Override
    public Map<String, Object>[] saveURL(String appname, String[] url) {
        return service.saveURL(appname, url);
    }

    @Override
    public Map<String, Object> save4url(String appname, String url) {
        return service.save4url(appname, url);
    }
}
