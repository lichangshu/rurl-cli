/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.microwww.rurl.rmi.help;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import net.microwww.rurl.rmi.dto.Employe;

/**
 *
 * @author changshu.li
 */
public class HttpSessionRightFilter extends AbstractHttpRightFilter {

    /**
     * session 中缓存用户的KEY
     */
    private String login_seesion_key;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        this.login_seesion_key = Rconfig.getSessionKey();
    }

    @Override
    protected Employe getLogin(HttpServletRequest request) {
        return (Employe) request.getSession(true).getAttribute(login_seesion_key);
    }
}
