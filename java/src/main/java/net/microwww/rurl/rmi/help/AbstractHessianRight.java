/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import java.util.List;
import net.microwww.rurl.rmi.cache.CacheFactory;
import net.microwww.rurl.rmi.dto.App;
import net.microwww.rurl.rmi.dto.Employe;
import net.microwww.rurl.rmi.dto.RightURL;
import net.microwww.rurl.rmi.imp.RurlFactory;

/**
 *
 * @author changshu.li
 */
public abstract class AbstractHessianRight extends AbstractRight {

    @Override
    protected List<RightURL> getAccountUrls(Employe employe) {
        String idcode = "RightURL_listurl_AbstractHessianRightFilter" + employe.getCacheKey();
        List<RightURL> clist = (List) CacheFactory.getCommonCache().getCacheJson(idcode);
        if (clist != null) {
            return clist;
        }
        synchronized (idcode) {
            List<RightURL> list = RurlFactory.getRurlWrap().listUrlRight(employe.getAccount());
            CacheFactory.getCommonCache().cacheJson(idcode, list);
            return list;
        }
    }

    @Override
    protected App getApp() {
        String idcode = "webapp_cache_AbstractHessianRightFilter" + Rconfig.getAppName();
        App app = (App) CacheFactory.getCommonCache().getCacheJson(idcode);
        if (app == null) {
            synchronized (idcode) {
                app = RurlFactory.getRurlWrap().getApplication();
                CacheFactory.getCommonCache().cacheJson(idcode, app);
            }
        }
        return app;
    }

    @Override
    protected List<RightURL> listAppURL(App app) {
        return RurlFactory.getRurlWrap().listAppURL();
    }
}
