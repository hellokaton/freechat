package com.fc.init;

import com.blade.Blade;
import com.blade.Environment;
import com.blade.event.BeanProcessor;
import com.blade.ioc.annotation.Bean;

/**
 * Created by biezhi on 2017/3/18.
 */
@Bean
public class WebStart implements BeanProcessor {

    @Override
    public void processor(Blade blade) {

        Environment environment = blade.environment();
        FCont.CHAR_URL = environment.get("chat-server-url", "127.0.0.1:10029");
        FCont.VERSION = environment.get("app.version", "0.0.1");
        FCont.ADM_PWD = environment.get("admin.pwd", "hello.world");

        ChatServer chatServer = new ChatServer(FCont.CHAR_URL);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ChatServer.stop();
        }));
        chatServer.start();
    }
}
