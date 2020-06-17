package com.creditas.backend.shopcars.application.infraestructure.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.juli.logging.LogFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter : OncePerRequestFilter() {
    private var Logger = LogFactory.getLog("JWTAuthorizationFilter.class")

    var HEADER: String = "Authorization"
    var PREFIX: String = "Bearer"
    val SECRET_KEY: String = "[fESVs4W%9OK]bwO!"

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (this.existJWT(request, response)) {
            var claims: Claims = this.validateJWT(request)
            if (claims.get("authorities") != null) {
                this.setUpSpringAuthentication(claims)
            } else {
                SecurityContextHolder.clearContext()
            }

        }
        filterChain.doFilter(request, response)
    }


    fun setUpSpringAuthentication(claims: Claims): Unit {
        var authorities: List<String> = claims.get("authorities") as List<String>
        var auth: UsernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken(claims.subject,
                        null,
                        authorities.stream().map(::SimpleGrantedAuthority)
                                .collect(Collectors.toList<SimpleGrantedAuthority>()))

        SecurityContextHolder.getContext().authentication = auth
    }

    fun validateJWT(req: HttpServletRequest): Claims {
        var jwtToken: String = req.getHeader(HEADER).replace(PREFIX, " ")
        return Jwts.parser().setSigningKey(SECRET_KEY.toByteArray())
                .parseClaimsJws(jwtToken).body
    }

    fun existJWT(req: HttpServletRequest, res: HttpServletResponse): Boolean {
        var authHeader: String? = req.getHeader(HEADER)
        if (authHeader == null) return false
        Logger.warn(authHeader.contains(PREFIX))
        //if (!authHeader.contains(PREFIX)) return false
        return true
    }


    fun createJWT(email: String, id: Long, role: String, request: HttpServletRequest): String {
        var grantedAuthorities: List<GrantedAuthority> = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_$role");
        var token: String = Jwts
                .builder()
                .claim("ip", request.getRemoteAddr())
                .claim("id", id)
                .setId("shopcars")
                .setSubject(email)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.toByteArray()).compact()
        return "Bearer " + token
    }
}