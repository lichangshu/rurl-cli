/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.microwww.rurl.rmi.NoRightException;

/**
 *
 * @author changshu.li
 */
public abstract class AbstractHttpRightFilter extends AbstractHessianRight implements Filter {

    public static final String COLLECTION_FILTER = "AbstractHttpRightFilter_COLLECTION_FILTER";
    public static final String COLLECTION_SERVLET_PATH = "/hessian.collection";
    protected UrlCollection collection;
    /**
     * 没有登录时 登录页面地址
     */
    private String loginpage;
    /**
     * 权限过滤是否开启
     */
    private boolean right_on = true;
    /**
     * 是否开启权限控制
     */
    private boolean isCollecting = false;

    private ServletContext context;
    private String servletpath;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        filterConfig.getServletContext().setAttribute(COLLECTION_FILTER, this);
        this.setUrlQueryOn(Rconfig.isQueryOn());
        this.setSkipurl(Rconfig.getSkipurl());
        this.loginpage = Rconfig.getLoginPage();
        this.right_on = Rconfig.isOpenRight();
        this.isCollecting = Rconfig.isCollectUrl();
        this.servletpath = this.context.getContextPath();
        this.collection = new UrlCollection(this.servletpath);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;
        if (isCollecting) {
            String path = collection.put(hsr, this.isUrlQueryOn()).split("\\?")[0];
            if (path.equals(COLLECTION_SERVLET_PATH)) {
                collection.service(hsr, rsp);
                return;
            }
        }
        if (right_on) {
            try {
                checkRight(hsr, servletpath);
            } catch (NoRightException ex) {
                onNoRightException(request, response, chain, ex);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    public void destroy() {
    }

    protected void onNoRightException(ServletRequest request, ServletResponse res, final FilterChain chain, NoRightException ex) throws IOException {
        HttpServletResponse response = (HttpServletResponse) res;
        if (ex.getCode() == NO_RIGHT_CODE_4_NO_LOGIN) {
            if (loginpage != null && loginpage.startsWith("/")) {
                response.sendRedirect(loginpage);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);// 401
    }

    public Set getCollectionUrl() {
        return collection.getPathset();
    }
}
