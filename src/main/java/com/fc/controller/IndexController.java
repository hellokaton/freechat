package com.fc.controller;

import com.blade.mvc.annotation.Controller;
import com.blade.mvc.annotation.Route;
import com.fc.init.ChatServer;

/**
 * Created by biezhi on 2017/3/18.
 */
@Controller
public class IndexController {

    @Route(values = "/")
    public String index() {
        return "index.html";
    }

    /**
     * 关闭聊天服务
     */
    @Route(values = "shutdown")
    public void shutdown() {
        ChatServer.stop();
    }

}
