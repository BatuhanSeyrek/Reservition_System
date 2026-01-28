package com.batuhanseyrek.rezarvasyonSistemi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // === TOKEN KONTROLÜNDEN MUAF YOLLAR ===
        // startsWith kullanarak supervisor altındaki tüm yolları tek seferde kapsıyoruz
        if (path.startsWith("/user/login") ||
                path.startsWith("/user/register") ||
                path.startsWith("/admin/login") ||
                path.startsWith("/admin/register") ||
                path.startsWith("/user/refenceIdLogin") ||
                path.startsWith("/supervisor/") || // Bu satır tüm supervisor işlemlerini kurtarır
                path.startsWith("/store/getAvailableSlotsReference") ||
                path.startsWith("/store/referenceReservationAdd") ||
                path.startsWith("/store/store")
        ) {
            filterChain.doFilter(request, response);
            return; // Muaf yollar için filtrenin geri kalanını çalıştırmıyoruz
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String userName = jwtUtil.extraUserName(token);

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                if (jwtUtil.isValid(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}