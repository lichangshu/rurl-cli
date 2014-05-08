/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.microwww.rurl.rmi.RurlServiceWrap;
import net.microwww.rurl.rmi.imp.RurlFactory;

/**
 *
 * @author changshu.li
 */
public class UrlCollection {

    private String servletpath;
    private Set<String> pathset = new ConcurrentSkipListSet();

    public UrlCollection(String servletpath) {
        this.servletpath = servletpath;
    }

    
    public String put(HttpServletRequest req, boolean query) {
        String path = formatPath(req, servletpath, query);
        pathset.add(path);
        return path;
    }

    public static String formatPath(HttpServletRequest req, String servletpath, boolean appendQuery) {
        String url = req.getRequestURI().replaceAll("/+", "/").trim().substring(servletpath.length());
        String qery = req.getQueryString();
        if (appendQuery && qery != null) {
            url += "?" + req.getQueryString();
        }
        return url;
    }

    /**
     * 只读的 set
     *
     * @return
     */
    public Set<String> getPathset() {
        return Collections.unmodifiableSet(pathset);
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

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<String> set = this.getPathset();
        resp.setCharacterEncoding("utf-8");
        PrintWriter w = resp.getWriter();
        w.append("<ol>");
        for (String path : set) {
            w.append("<li>" + path + "</li>");
        }
        w.append("</ol>");
        w.append("<div><a href='?action=save'>保存</></div>");
        if ("save".equals(req.getParameter("action"))) {
            RurlServiceWrap rr = RurlFactory.getRurlWrap();
            rr.saveUrlRight(set);
            w.append("<div style='color:#090;'>保存成功</div>");
        }
        resp.setContentType("text/html;charset=utf-8");
    }
}
