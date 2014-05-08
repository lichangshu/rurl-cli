/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.imp;

import net.microwww.rurl.rmi.RurlRightService;
import net.microwww.rurl.rmi.RurlServiceWrap;
import net.microwww.rurl.rmi.help.Rconfig;


/**
 *
 * @author changshu.li
 */
public class RurlFactory {

	private static RurlRightService service;
	private static RurlServiceWrapImp rdapRight;

	public static RurlRightService getRurlRightService() {
		if (service == null) {
			synchronized (RurlFactory.class) {//改单例方式用于 延迟加载
				if (service == null) {
					String server = Rconfig.getRurlServer();
					service = new RurlRightServiceProxy(server);
				}
			}
		}
		return service;
	}

	public static RurlServiceWrap getRurlWrap() {
		if (rdapRight == null) {
			synchronized (RurlFactory.class) {//改单例方式用于 延迟加载
				if (rdapRight == null) {
					RurlServiceWrapImp right = new RurlServiceWrapImp();
					right.setAppname(Rconfig.getAppName());
					right.setService(getRurlRightService());
					rdapRight = right;
				}
			}
		}
		return rdapRight;
	}
}
