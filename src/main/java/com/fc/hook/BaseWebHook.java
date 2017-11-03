package com.fc.hook;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.hook.Signature;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Request;
import com.fc.init.FCont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by biezhi on 2017/3/18.
 */
@Bean
public class BaseWebHook implements WebHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseWebHook.class);

    @Override
    public boolean before(Signature signature) {
        Request request = signature.request();
        String  uri     = request.uri();
        LOGGER.info("request: {}, ip: {}", uri, request.address());
        request.attribute("ChatServerAddress", FCont.CHAR_URL);
        request.attribute("version", FCont.VERSION);
        return true;
    }
}
