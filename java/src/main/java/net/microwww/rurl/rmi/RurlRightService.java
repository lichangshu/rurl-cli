/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi;

import java.util.Map;

/**
 *
 * @author changshu.li
 */
public interface RurlRightService {

    public Map<String, Object> login(String account, String password) throws NoRightException;

    public Map<String, Object>[] listAccountUrlRight(String appname, String account);

    public Map<String, Object>[] listUrlRight(String appname);

    public Map<String, Object> getApplication(String appname);

    public boolean hasLoginRight(String appname, String account);

    public Map<String, Object>[] saveURL(String appname, String[] url);

    public Map<String, Object> save4url(String appname, String url);
}
