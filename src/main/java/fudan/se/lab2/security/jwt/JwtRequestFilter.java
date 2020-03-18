package fudan.se.lab2.security.jwt;

import fudan.se.lab2.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Write your code to make this filter works.
 *
 * @author LBW
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // TODO: Implement the filter.


        String tokenHead = request.getHeader("Authorization");
        //token头不等于空 并且以Bearer 开头进行token验证登录处理
        if (tokenHead != null && tokenHead.startsWith("Bearer ")) {
            //从请求头中截取token
            String token = tokenHead.substring(7);
            //通过token得到用户名  如果token已过期或者错误 会抛出异常,并被spring security捕获 调我们自定义的登录失败处理方法
            String userName = jwtTokenUtil.getUsernameFromToken(token);
            //用户名不等于空  并且当前上下文环境中没有认证过 就进行登录验证
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //通过用户名查询数据库 构建符合spring security要求的安全用户对象
                UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userName);
                //验证token和用户对象
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    //通过安全用户对象 构建一个登录对象
                    UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //传入当前http请求对象
                    login.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //将登录对象 写入到当前上下文环境中 后续的判断 权限控制就由spring Security做
                    SecurityContextHolder.getContext().setAuthentication(login);
                }
                //调用下一个过滤器  如果有token已经在此完成登录 没有登录的话 会被后续拦截器处理
                filterChain.doFilter(request, response);
            }

        }
    }
}
