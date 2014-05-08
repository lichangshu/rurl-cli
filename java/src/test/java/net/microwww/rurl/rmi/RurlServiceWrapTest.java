/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.microwww.rurl.rmi.dto.App;
import net.microwww.rurl.rmi.dto.Employe;
import net.microwww.rurl.rmi.dto.RightURL;
import net.microwww.rurl.rmi.help.Rconfig;
import net.microwww.rurl.rmi.imp.RurlFactory;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lcs
 */
public class RurlServiceWrapTest {

    RurlServiceWrap instance = RurlFactory.getRurlWrap();

    @Test
    public void testListUrlRight() {
        System.out.println("listUrlRight");
        String account = "1";
        List<RightURL> result = instance.listUrlRight(account);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testListAppURL() {
        System.out.println("listAppURL");
        List<RightURL> result = instance.listAppURL();
        assertFalse(result.isEmpty());
    }

    @Test
    public void testListMenu() {
        System.out.println("listMenu");
        String account = "1";
        List<RightURL> result = instance.listMenu(account);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testLogin() throws Exception {
        System.out.println("login");
        String account = "1";
        String password = "1";
        Employe result = instance.login(account, password);
        assertEquals(account, result.getAccount());
    }

    @Test
    public void testSaveUrlRight() {
        System.out.println("saveUrlRight");
        Set<String> set = new HashSet<String>();
        set.add("/app/");
        set.add("/groups/");
        set.add("/url/");
        List<RightURL> result = instance.saveUrlRight(set);
        assertEquals(result.size(), set.size());
    }

    @Test
    public void testGetApplication() {
        System.out.println("getApplication");
        App result = instance.getApplication();
        assertEquals(Rconfig.getAppName(), result.getAppName());
    }
}
