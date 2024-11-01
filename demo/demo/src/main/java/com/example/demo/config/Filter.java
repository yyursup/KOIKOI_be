package com.example.demo.config;

import com.example.demo.entity.Account;
import com.example.demo.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Qualifier("handlerExceptionResolver")
    @Autowired
    HandlerExceptionResolver resolver;

    private final List<String> AUTH_PERMISSION = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/login",
            "/api/register"
    );

    public boolean checkIsPublicAPI(String uri) {
        // uri: /api/register

        // neu gap nhung cai api trong list o tren => cho phep truy cap luon => true
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // check token => false
        return AUTH_PERMISSION.stream().anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // check xem cai api ma nguoi dung ycau co phai la 1 public api ?

        boolean isPublicAPI = checkIsPublicAPI(request.getRequestURI());

        if(isPublicAPI) {
            filterChain.doFilter(request, response);
        } else {
            String token = getToken(request);
            if(token == null){
                // ko dc phep truy cap
                resolver.resolveException(request, response, null, new AuthException("Empty token!"));
                return;
            }

            // => cos token
            //check xem token co dung hay khong => lay thong tin account tu token
            Account account;
            try{
                account = tokenService.getAccountByToken(token);
            }catch (ExpiredJwtException e) {
                //response token het han
                resolver.resolveException(request, response, null, new AuthException("Expired token!"));
                return;
            }catch (MalformedJwtException malformedJwtException) {
                //response token sai
                resolver.resolveException(request, response, null, new AuthException("Invalid token!"));
                return;
            }

            // => token chuan
            // => cho phep truy cap
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    account,token,account.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.substring(7);
    }
}