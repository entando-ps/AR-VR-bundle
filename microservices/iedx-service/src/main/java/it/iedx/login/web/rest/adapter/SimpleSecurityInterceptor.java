package it.iedx.login.web.rest.adapter;

import it.iedx.login.domain.Token;
import it.iedx.login.service.TokenService;
import it.iedx.login.web.rest.AccessCodeResource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class SimpleSecurityInterceptor extends HandlerInterceptorAdapter {

    private final Logger log = LoggerFactory.getLogger(AccessCodeResource.class);

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean result = super.preHandle(request, response, handler);
        String value = request.getHeader(HEADER_SEC_NAME);
        Optional<Token> token = tokenService.getCurrent();

        if (StringUtils.isBlank(value)) {
            value = request.getParameter(HEADER_SEC_NAME);
        }
        result = result
            && StringUtils.isNotBlank(value)
            && token.isPresent()
            && value.equals(token.get().getCurrent());
        if (!result) {
            log.error("*** API token verification failed! ***");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return result;
    }

    public final static String HEADER_SEC_NAME = "i-edx-token";
}
