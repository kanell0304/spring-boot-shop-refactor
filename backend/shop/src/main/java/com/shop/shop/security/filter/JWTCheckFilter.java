package com.shop.shop.security.filter;

import com.google.gson.Gson;
import com.shop.shop.domain.member.Address;
import com.shop.shop.domain.member.MemberShip;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.shop.shop.dto.MemberDTO;
import com.shop.shop.util.JWTUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        // Preflight 요청은 체크하지 않음
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        String path = request.getRequestURI();

        log.info("check uri.............." + path);

        //api/member/ 경로의 호출은 체크하지 않음
        if (path.startsWith("/api/member/")) {
            return true;
        }

        //이미지 조회 경로는 체크하지 않는다
        if (path.startsWith("/api/products/view/")) {
            return true;
        }

        // 상품 경로는 체크하지 않는다.
        if (path.startsWith("/api/items/**")) {
            return true;
        }

        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("------------------------JWTCheckFilter.......................");

        String authHeaderStr = request.getHeader("Authorization");

        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 필터 건너뛰기
        if (authHeaderStr == null || !authHeaderStr.startsWith("Bearer ")) {
            log.warn("JWT Check Skipped: No Authorization header found or invalid format.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            //Bearer accestoken...
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken);

            log.info("JWT claims: " + claims);

            long memberId = Long.parseLong(claims.get("memberId").toString()); // id → memberId 로 변경
            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String memberName = (String) claims.get("memberName");
            String phoneNumber = (String) claims.get("phoneNumber");
            Address address = (Address) claims.get("address");
            int stockMileage = claims.get("stockMileage") != null ? ((Number) claims.get("stockMileage")).intValue() : 0;
            LocalDateTime joinDate = (LocalDateTime) claims.get("joinDate");
            Boolean wtrSns = (Boolean) claims.get("wtrSns");
            Boolean delFlag = (Boolean) claims.get("delFlag");
            MemberShip memberShip = (MemberShip) claims.get("memberShip");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");

            MemberDTO memberDTO = new MemberDTO(memberId, email, password, memberName, phoneNumber, address, stockMileage, joinDate, wtrSns != null && wtrSns, social != null && social, delFlag != null && delFlag, memberShip, roleNames);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, password, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }
    }


}
