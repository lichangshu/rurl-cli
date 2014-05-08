/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.microwww.rurl.rmi.RurlServiceWrap;
import net.microwww.rurl.rmi.imp.RurlFactory;

/**
 *
 * @author changshu.li
 */
public class ShowUrlServlet extends HttpServlet {

    private ServletContext context;
    @Override
    public void init(ServletConfig config) throws ServletException {
        context = config.getServletContext();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AbstractHttpRightFilter collect = (AbstractHttpRightFilter) context.getAttribute(AbstractHttpRightFilter.COLLECTION_FILTER);
        PrintWriter w = resp.getWriter();
        if(collect == null){
            resp.setContentType("text/html;charset=utf-8");
            w.append("not find collection !!");
            return;
        }
        Set<String> set = collect.getCollectionUrl();
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
        resp.setCharacterEncoding("utf-8");
    }

    public static String compileHtmlTemplets(String templ, String html) {
        String[] tp = templ.split("\\s");
        return html(tp, 0, html);
    }

    public static String html(String[] tag, int index, String body) {
        int i = index;
        if (tag.length <= index) {
            return body;
        } else {
            if (tag[i].trim().length() == 0) {
                return html(tag, index + 1, body);
            }
            String ht = "<" + tag[i] + ">";
            ht += html(tag, index + 1, body);
            ht += "</" + tag[i] + ">";
            return ht;
        }
    }
}
