package com.service.util;

import com.security.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import tech.jhipster.config.JHipsterProperties;

import javax.servlet.http.HttpServletRequest;

import static com.security.jwt.JWTFilter.AUTHORIZATION_HEADER;

@Component
public class TokenUtils {
    private final TokenProvider tokenProvider;
    private final JHipsterProperties jHipsterProperties;

    public TokenUtils(TokenProvider tokenProvider, JHipsterProperties jHipsterProperties) {
        this.tokenProvider = tokenProvider;
        this.jHipsterProperties = jHipsterProperties;
    }

//    public  String getOrgId(HttpServletRequest request){
//        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
//
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(SecurityContextHolder.getContext().getAuthentication().getCredentials()).getBody().get("OrgId");
//    }

    public String getOrgId(HttpServletRequest request){
        String secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();
        String jwt = resolveToken(request);
        return String.valueOf(Jwts.parser().setSigningKey(secret).parseClaimsJws((String) jwt).getBody().get("OrgId"));

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
