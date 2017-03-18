package com.fc.init;

import com.blade.config.BConfig;
import com.blade.context.WebContextListener;
import com.blade.kit.StringKit;
import com.blade.kit.base.Config;
import com.blade.mvc.view.ViewSettings;
import com.blade.mvc.view.template.JetbrickTemplateEngine;

import javax.servlet.ServletContext;

/**
 * Created by biezhi on 2017/3/18.
 */
public class WebStart implements WebContextListener {

    @Override
    public void init(BConfig bConfig, ServletContext sec) {

        // 注册模板引擎
        ViewSettings.$().templateEngine(new JetbrickTemplateEngine());

        Config config = bConfig.config();
        String address = config.get("chat.addr", "127.0.0.1:10029");
        FCont.CHAT_ADDR = StringKit.split(address)[0];
        ChatServer chatServer = new ChatServer(address);
        chatServer.start();
    }
}
