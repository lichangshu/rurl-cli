/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi;

import java.util.List;
import java.util.Set;
import net.microwww.rurl.rmi.dto.App;
import net.microwww.rurl.rmi.dto.Employe;
import net.microwww.rurl.rmi.dto.RightURL;

/**
 *
 * @author changshu.li
 */
public interface RurlServiceWrap {

    List<RightURL> listUrlRight(String account);

    List<RightURL> listAppURL();

    List<RightURL> listMenu(String account);

    Employe login(String account, String password) throws NoRightException;

    List<RightURL> saveUrlRight(Set<String> set);

    App getApplication();
}
