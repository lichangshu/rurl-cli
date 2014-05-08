/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.cache;

import java.io.Serializable;

/**
 *
 * @author changshu.li
 */
public interface CommonCache {

	public void cache(String key, Serializable o);

	public void cache(String key, String o);

	public void cacheJson(String key, Object o);

	public String getCacheString(String key);

	public Serializable getCacheSerializable(String key);

	public Object getCacheJson(String key);

	public boolean isCached(String key);

	public void removeCache(String key);
}
