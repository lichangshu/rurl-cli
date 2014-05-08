/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import net.microwww.rurl.rmi.NoRightException;
import net.microwww.rurl.rmi.cache.CacheFactory;
import net.microwww.rurl.rmi.dto.App;
import net.microwww.rurl.rmi.dto.Employe;
import net.microwww.rurl.rmi.dto.RightURL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author changshu.li
 */
public abstract class AbstractRight {

    public static final int NO_RIGHT_CODE_4_NO_LOGIN = -11;
    public static final int NO_RIGHT_CODE_4_LOGINED = -12;

    private static final Log logger = LogFactory.getLog(AbstractRight.class);
    public static final String NL = "\n";
    public static final String SKIP_URL_SPLIT = ";";

    /**
     * 检测的 URL 是否需要 QUERY 参数
     */
    private boolean urlQueryOn = false;
    /**
     * 需要跳过的 URL 规则的字符串
     */
    private String skipurl;
    /**
     * 跳过权限检测的 URL 集合
     */
    private Set<String> skipUrlSet = new ConcurrentSkipListSet<String>();
    /**
     * 带星号规则的 URL 的正则
     *
     */
    private Set<Pattern> skipPatternSet;

    private static final Pattern PT_STAR = Pattern.compile("\\*");
    private static final Pattern PT_POINT = Pattern.compile("\\.");

    public synchronized void init(){
        if(skipPatternSet == null){
            if(skipurl != null){
                String[] ku = skipurl.split(SKIP_URL_SPLIT);
                setSkiplist(ku);
            }else{
                setSkiplist(new String[]{});
            }
        }
    }
    public void setSkiplist(String[] str) {
        Set<Pattern> hset = new HashSet<Pattern>();
        for (String s : str) {
            s = s.trim();
            if (s.indexOf('*') >= 0) {
                s = PT_POINT.matcher(s).replaceAll("\\\\.");
                if (s.startsWith("*") || s.endsWith("*")) {
                    s = PT_STAR.matcher(s).replaceAll(".*");
                } else {
                    s = PT_STAR.matcher(s).replaceAll("[\\\\w.-]*");
                }
                hset.add(Pattern.compile(s));
            } else {
                skipUrlSet.add(s);
            }
        }
        skipPatternSet = Collections.unmodifiableSet(hset);
    }

    /**
     * 获取登录用户
     *
     * @param request
     * @return
     */
    abstract protected Employe getLogin(HttpServletRequest request);

    abstract protected List<RightURL> getAccountUrls(Employe employe);

    abstract protected App getApp();

    abstract protected List<RightURL> listAppURL(App app);

    /**
     *
     * 缓存字符串，而非集合对象, 在非内存缓存时候可以节省反序列号的代价.
     *
     * 结构是 空行开始 每行一条 方便匹配
     *
     * @param employe
     * @param path
     * @return
     */
    protected boolean employeRightContainPath(final Employe employe, String path) {
        //数据结构有变化时 修改前缀
        String idcode = "right_path_v2_AbstractRight_" + employe.getCacheKey();
        String cachestring = CacheFactory.getCommonCache().getCacheString(idcode);
        if (cachestring == null) {
            synchronized (idcode) {//重复放置 无所谓
                List<RightURL> list = getAccountUrls(employe);
                StringBuffer buffer = new StringBuffer(NL);
                for (RightURL url : list) {
                    if (url.getWebappPath() != null) {
                        buffer.append(url.getWebappPath().trim()).append(NL);
                    }
                }
                cachestring = buffer.toString();
                CacheFactory.getCommonCache().cache(idcode, cachestring);
            }
        }
        return cachestring.contains(NL + path.trim() + NL);
    }

    /**
     * 只检测 无 QUERY 参数的部分. 使用缓存， 同 employeRightContainPath
     *
     * @param path
     * @return
     */
    protected boolean webappHadConfigPath(String path) {
        App app = getApp();
        String idcode = "config_path_in_app_id_v2" + app.getId();
        String cachestring = CacheFactory.getCommonCache().getCacheString(idcode);
        if (cachestring == null) {
            synchronized (idcode) {//重复放置 无所谓
                List<RightURL> list = listAppURL(app);
                StringBuffer buffer = new StringBuffer(NL);
                for (RightURL url : list) {
                    String wpath = url.getWebappPath();
                    if (wpath != null) {
                        wpath = url.getWebappPath().trim();
                        if (wpath.contains("?")) {
                            buffer.append(wpath.split("\\?")[0]).append(NL);//将 非 Query 部分放入缓存
                        }
                        buffer.append(url.getWebappPath().trim()).append(NL);
                    }
                }
                cachestring = buffer.toString();
                CacheFactory.getCommonCache().cache(idcode, cachestring);
            }
        }
        return cachestring.contains(NL + path.trim().split("\\?")[0] + NL);//只取 Path 部分
    }

    protected void checkRight(HttpServletRequest req, String servletpath) throws NoRightException {

        String uri = UrlCollection.formatPath(req, servletpath, urlQueryOn);// 是否 启用query

        if (skipUrlSet.contains(uri)) {
            logger.debug("skip " + uri);
            return;
        }
        if (skipStar(uri)) {
            logger.debug("skip " + uri);
            return;
        }
        App app = getApp();
        if (app.isUnknownUrl()) {//未知的链接 允许访问
            if (!webappHadConfigPath(uri)) {// 是未知的 链接
                logger.debug("skip webapp unknown url " + uri);
                return;
            }
        }
        // 不允许访问 查看权限
        Employe employe = getLogin(req);
        if (employe != null) {//登录用户查看权限
            if (employeRightContainPath(employe, uri)) {//权限包含
                logger.debug("login in user has right url " + uri);
                return;
            }
            logger.info("no right to access [" + uri + "] for login user " + employe.getAccount());
            throw new NoRightException(NO_RIGHT_CODE_4_LOGINED, "没有权限访问该地址:" + uri);
        }
        logger.warn("no right to access [" + uri + "], ip:" + UrlCollection.getIpAddr(req));
        throw new NoRightException(NO_RIGHT_CODE_4_NO_LOGIN, "没有登录不能访问该地址:" + uri);
    }

    /**
     * 检测不包括 query 参数的部分
     *
     * @param url
     * @return
     */
    protected boolean skipStar(String url) {
        if(skipPatternSet == null){
            init();
        }
        String uri = url.split("\\?")[0];
        for (Pattern sk : skipPatternSet) {
            boolean mat = sk.matcher(uri).matches();
            if (mat) {
                skipUrlSet.add(url);
                return true;
            }
        }
        return false;
    }

    public boolean isUrlQueryOn() {
        return urlQueryOn;
    }

    protected void setUrlQueryOn(boolean urlQueryOn) {
        this.urlQueryOn = urlQueryOn;
    }

    public String getSkipurl() {
        return skipurl;
    }

    protected void setSkipurl(String skipurl) {
        this.skipurl = skipurl;
    }
}
