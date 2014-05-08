/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.cache;

import net.microwww.rurl.rmi.dto.Employe;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author changshu.li
 */
public class CacheFactoryTest {

    CommonCache cache = CacheFactory.getCommonCache();

    /**
     * Test of getCommonCache method, of class CacheFactory.
     */
    @Test
    public void testGetCacheJson() {
        System.out.println("getCommonCache");
        Object expResult = "vvvv" + System.currentTimeMillis();
        String key = expResult.toString();
        cache.cacheJson(key, expResult);
        Object result = cache.getCacheJson(key);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetCacheSerializable() {
        System.out.println("getCommonCache");
        Object expResult = "vvvv" + System.currentTimeMillis();
        String key = expResult.toString();
        Employe o = new Employe();
        o.setAccount(expResult.toString());
        cache.cache(key, o);
        Employe result = (Employe) cache.getCacheSerializable(key);
        assertEquals(expResult, result.getAccount());
    }

    @Test
    public void testIsCache() {
        System.out.println("getCommonCache");
        Object expResult = "vvvv" + System.currentTimeMillis();
        assertFalse(cache.isCached(expResult.toString()));

        String key = expResult.toString();

        Employe o = new Employe();
        o.setAccount(expResult.toString());
        cache.cache(key, o);
        Employe result = (Employe) cache.getCacheSerializable(key);
        assertEquals(expResult, result.getAccount());
    }

}
