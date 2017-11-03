package com.fc.controller;

import com.blade.mvc.annotation.GetRoute;
import com.blade.mvc.annotation.Param;
import com.blade.mvc.annotation.Path;
import com.blade.mvc.annotation.PostRoute;
import com.fc.init.ChatServer;
import com.fc.init.FCont;

/**
 * Created by biezhi on 2017/3/18.
 */
@Path
public class IndexController {

    @GetRoute("/")
    public String index() {
        return "index.html";
    }

    @PostRoute("opt")
    public void opt(@Param String pwd, @Param String opt) {
        if (FCont.ADM_PWD.equals(pwd)) {
            if ("shutdown".equals(opt)) {
                ChatServer.stop();
            }
            if ("startup".equals(opt)) {
                ChatServer.startup();
            }
        }
    }

}
