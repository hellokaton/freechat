package com.fc.interceptor;

import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.interceptor.Interceptor;
import com.fc.init.FCont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by biezhi on 2017/3/18.
 */
public class BaseInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseInterceptor.class);

    @Override
    public boolean before(Request request, Response response) {

        LOGGER.info("request: {}, ip: {}", request.uri(), request.address());
        request.attribute("chat_addr", FCont.CHAR_URL);

        return true;
    }

    @Override
    public boolean after(Request request, Response response) {
        return true;
    }
}
