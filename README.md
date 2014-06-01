rurl-cli
========

Rurl 的客户端实现

=== java 实现 ===

 *  配置 web.xml 配置拦截器:  根据需要配置需要拦截的 url..
```xml
  <filter>
    <filter-name>right-filter</filter-name>
    <filter-class>net.microwww.rurl.rmi.help.HttpSessionRightFilter</filter-class>
  </filter>
  <filter-mapping>
  <filter-name>right-filter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

 *  修改配置文件 复制 demo 文件 rconfig-simple.properties 到 classpath 下面后修改即可. 参数说明:

 1. rurl_server :  rurl 服务器地址
 2. cache_time : 缓存时间, 默认一个小时
 3. appname : 应用的ID 与 rurl 服务器里应用的 appname 相对应, 如果服务器没有配置该应用,会抛出 NullPointException .
 4. open_right : 是否开启权限过滤.
 5. collection_url 是否收集URL( 会收集到本地, 访问 /hessian.collection 地址可以查看所有收集到的 url , 并可以同步到 rurl 服务器).
 6. login_seesion_key 登录用户的 session key .
 7. login_page : 登录页面地址.
 8. query_on : 收集RUL的时候 是否同时收集 get 的query 参数. 如果启用,权限检测也同时需要检测 query 参数.
 9. 其他参数看见 配置文件的 说明.
 
 *  注意 HttpSessionRightFilter 该拦截器 通过 session 获取登录的帐号(session key 在配置文件中配置-6-).
 *  如果没有登录,会跳转到 login_page 页面.  
