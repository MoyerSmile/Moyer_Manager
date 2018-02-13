package sl.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sl.service.TLoginService;
import sl.service.impl.LoginServiceImpl;
import web.xzy.base.HttpInfo;

/**
 * 登录拦截器
 * 
 * @author moyer
 * @date 2018年2月4日
 */
public class LoginFilter implements Filter {

    private TLoginService loginService = new LoginServiceImpl();

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String currentURL = request.getRequestURI();
        String reffer = request.getHeader("Referer");// 获取请求者

        if ("/html/login/login.html".equals(currentURL) || (reffer != null
                && reffer.endsWith("/html/login/login.html"))) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpInfo httpInfo = new HttpInfo();
        if (request.getParameter(SlLoginConfig.TT_TOKEN) != null) {
            httpInfo.setInfo(SlLoginConfig.TT_TOKEN,
                request.getParameter(SlLoginConfig.TT_TOKEN));
        }
        String token = loginService.byToken(httpInfo, request);
        if (token == null) {
            response.sendRedirect(
                request.getContextPath() + "/html/login/login.html");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
