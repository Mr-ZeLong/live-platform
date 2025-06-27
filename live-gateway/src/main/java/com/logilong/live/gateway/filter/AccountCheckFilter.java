package com.logilong.live.gateway.filter;

import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import com.logilong.live.account.interfaces.IAccountTokenRPC;
import com.logilong.live.common.interfaces.enums.GatewayHeaderEnum;
import com.logilong.live.gateway.properties.GatewayApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.handler.codec.http.cookie.CookieHeaderNames.MAX_AGE;
import static org.springframework.web.cors.CorsConfiguration.ALL;


@Component
public class AccountCheckFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCheckFilter.class);

    @DubboReference
    private IAccountTokenRPC accountTokenRPC;
    @Resource
    private GatewayApplicationProperties gatewayApplicationProperties;

    @Value("${live.http.headers.ACCESS_CONTROL_ALLOW_ORIGIN}")
    private String ACCESS_CONTROL_ALLOW_ORIGIN;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求url，判断是否为空，如果为空则返回请求不通过
        ServerHttpRequest request = exchange.getRequest();
        String reqUrl = request.getURI().getPath();
        // 允许跨域请求（和api模块中地loginserviceimpl中的一样，设置了域名才加这个，我这里无法解决跨域就没有设置域名）
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = response.getHeaders();
        // headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://web.live.com:5500");
        // 这里我们不设置域名，就设置为localhost
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, this.ACCESS_CONTROL_ALLOW_ORIGIN);
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);

        if (StringUtils.isEmpty(reqUrl)) {
            return Mono.empty();
        }
        //根据url，判断是否存在于url白名单中，如果存在，则不对token进行校验
        List<String> notCheckUrlList = gatewayApplicationProperties.getNotCheckUrlList();
        for (String notCheckUrl : notCheckUrlList) {
            if (reqUrl.startsWith(notCheckUrl)) {
                LOGGER.info("请求没有进行token校验，直接传达给业务下游");
                //直接将请求转给下游
                return chain.filter(exchange);
            }
        }
        //如果不存在url白名单，那么就需要提取cookie，并且对cookie做基本的格式校验
        List<HttpCookie> httpCookieList = request.getCookies().get("livetk");
        if (CollectionUtils.isEmpty(httpCookieList)) {
            LOGGER.error("请求没有检索到livetk的cookie，被拦截");
            return Mono.empty();
        }
        String liveTokenCookieValue = httpCookieList.get(0).getValue();
        if (StringUtils.isEmpty(liveTokenCookieValue) || StringUtils.isEmpty(liveTokenCookieValue.trim())) {
            LOGGER.error("请求的cookie中的livetk是空，被拦截");
            return Mono.empty();
        }
        //token获取到之后，调用rpc判断token是否合法，如果合法则吧token换取到的userId传递给到下游
        Long userId = accountTokenRPC.getUserIdByToken(liveTokenCookieValue);
        //如果token不合法，则拦截请求，日志记录token失效
        if (userId == null) {
            LOGGER.error("请求的token失效了，被拦截");
            return Mono.empty();
        }
        // gateway --(header)--> springboot-web(interceptor-->get header)
        ServerHttpRequest.Builder builder = request.mutate();
        builder.header(GatewayHeaderEnum.USER_LOGIN_ID.getName(), String.valueOf(userId));
        return chain.filter(exchange.mutate().request(builder.build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
