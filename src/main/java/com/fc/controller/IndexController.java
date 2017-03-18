package com.fc.controller;

import com.blade.mvc.annotation.Controller;
import com.blade.mvc.annotation.QueryParam;
import com.blade.mvc.annotation.Route;
import com.blade.mvc.http.HttpMethod;
import com.fc.init.ChatServer;
import com.fc.init.FCont;

/**
 * Created by biezhi on 2017/3/18.
 */
@Controller
public class IndexController {

    @Route(values = "/")
    public String index() {
        return "index.html";
    }

    @Route(values = "opt", method = HttpMethod.POST)
    public void opt(@QueryParam String pwd, @QueryParam String opt){
        if(FCont.ADM_PWD.equals(pwd)){
            if("shutdown".equals(opt)){
                ChatServer.stop();
            }
            if("startup".equals(opt)){
                ChatServer.startup();
            }
        }
    }

}
