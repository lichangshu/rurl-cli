/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.cache;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.Serializable;
import net.microwww.rurl.rmi.help.Rconfig;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author changshu.li
 */
public class CacheFactory {

    private static final Log logger = LogFactory.getLog(CacheFactory.class);

    /**
     * 一个小时过期
     *
     * @return
     */
    public static CommonCache getCommonCache() {
        return CommonCacheImp.ccache;
    }

    public static class CommonCacheImp implements CommonCache {

        public static final String CACHE_PRE = Rconfig.getCachePrefix();
        public static final int cache_time = Integer.valueOf(Rconfig.getCacheTime());
        private static CommonCacheImp ccache = new CommonCacheImp();

        private Cache mcache;

        public CommonCacheImp() {
            try {
                CacheManager manager = CacheManager.create();
                Cache cache = new Cache(CACHE_PRE, 500, true, false, cache_time, cache_time / 4);
                manager.addCache(cache);
                this.mcache = manager.getCache(CACHE_PRE);
            } catch (CacheException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public void cache(String key, Serializable o) {
            Element ele = new Element(key, o);
            mcache.put(ele);
        }

        @Override
        public void cache(String key, String o) {
            this.cache(key, (Serializable) o);
        }

        @Override
        public void cacheJson(String key, Object o) {
            String json = JSONObject.toJSONString(o, SerializerFeature.WriteClassName);
            this.cache(key, json);
        }

        @Override
        public String getCacheString(String key) {
            return (String) this.getCacheSerializable(key);
        }

        @Override
        public Serializable getCacheSerializable(String key) {
            try {
                Element ele = this.mcache.get(key);
                if (ele == null) {
                    return null;
                }
                return ele.getValue();
            } catch (Exception ex) {
                CacheFactory.logger.error(ex);
                return null;
            }
        }

        @Override
        public Object getCacheJson(String key) {
            String val = this.getCacheString(key);
            if (val == null) {
                return null;
            }
            return JSONObject.parse(val);
        }

        @Override
        public boolean isCached(String key) {
            return null != this.getCacheSerializable(key);
        }

        @Override
        public void removeCache(String key) {
            this.mcache.remove(key);
        }

    }
}
