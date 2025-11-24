package com.onlineorder.apigateway.filter;

import com.onlineorder.apigateway.util.JwtUtil;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil){
        super(Config.class);
        this.validator=validator;
        this.jwtUtil=jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Token cannot be found in request header");
                }
                String authHeader=exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    authHeader=authHeader.substring(7);
                }
                try {
                    jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    System.out.println("Token validation error: "+e.getMessage());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid token");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}
