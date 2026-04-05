package com.shop.shop.controller.login;

import com.google.gson.Gson;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.dto.MemberDetails;
import com.shop.shop.security.CustomUserDetailsService;
import com.shop.shop.service.MemberService;
import com.shop.shop.util.CustomJWTException;
import com.shop.shop.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public void login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        log.info("--- [AuthController] --- 매개변수 정보 : email: {}, password: {}", email, password);

        try {
            // `UserDetailsService`에서 반환하는 객체를 `MemberDetails`로 캐스팅
            MemberDetails memberDetails = (MemberDetails) customUserDetailsService.loadUserByUsername(email);

            if (!passwordEncoder.matches(password, memberDetails.getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("AuthController: 인증성공 : " + email);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // `MemberDetails`에서 `MemberDTO` 변환
            MemberDTO memberClaims = memberDetails.toMemberDTO();

            Map<String, Object> claims = new HashMap<>();
            claims.put("memberId", memberClaims.getId());
            claims.put("memberName", memberClaims.getMemberName());
            claims.put("social", memberClaims.isSocial());
            claims.put("roleNames", memberClaims.getRoleNames());

            Map<String, Object> responseMap = new HashMap<>(claims);
            responseMap.put("accessToken", jwtUtil.generateToken(claims, 10));
            responseMap.put("refreshToken", jwtUtil.generateToken(claims, 60 * 24));

            String jsonStr = new Gson().toJson(responseMap);

            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(jsonStr);

        } catch (BadCredentialsException e) {
            log.error("AuthController: 이메일 또는 비밀번호 오류 : " + email, e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("AuthController: 로그인 과정 중 예기치 못한 오류 ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping("/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String refreshToken) {

        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(claims, 10);
        String newRefreshToken = checkTime((Integer) claims.get("exp"))
                ? jwtUtil.generateToken(claims, 60 * 24)
                : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkExpiredToken(String token) {
        try {
            jwtUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTime(Integer exp) {
        java.util.Date expDate = new java.util.Date((long) exp * 1000);
        long gap = expDate.getTime() - System.currentTimeMillis();
        long leftMin = gap / (1000 * 60);
        return leftMin < 60;
    }

}
