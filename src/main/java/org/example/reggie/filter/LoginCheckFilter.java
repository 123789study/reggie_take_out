package org.example.reggie.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.BaseContext;
import org.example.reggie.common.R;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器,通配符可用
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);

        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2、判断本次请求是否需要处理
        boolean check=check(urls,requestURI);

        //3、如果不需要处理，则直接放行
        if (check){
            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4-1、 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户已经登录过了,用户id为:{}",request.getSession().getAttribute("employee"));

            Long empId =(Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);


            filterChain.doFilter(request,response);
            return;
        }

        //4-2、 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户已经登录过了,用户id为:{}",request.getSession().getAttribute("employee"));

            Long UserId =(Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(UserId);


            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户为登录");
        //5、如果未登录则返回未登录结果
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配检查是否要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            final var match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }

}
