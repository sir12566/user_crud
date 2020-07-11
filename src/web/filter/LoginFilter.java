package web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 登陆验证过滤器
 */
@WebFilter("/*")
public class LoginFilter implements Filter {


    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        //0.强制转换
        HttpServletRequest request = (HttpServletRequest)req;


        //1.获取资源请求路径
        String uri = request.getRequestURI();

        //2.判断是否包含登陆的相关路径 不能排除css，js，图片，验证码等资源

        if(uri.contains("login.jsp")||uri.contains("/loginServlet")
                ||uri.contains("/css/")||uri.contains("/js/")||uri.contains("/fonts/")
                ||uri.contains("/checkCodeServlet")){
            //包含 放行
            chain.doFilter(req, resp);
        }else {
            //不包含，需要验证用户是否登陆
            //3.从session中获取user
            Object user = request.getSession().getAttribute("user");
            if(user!=null){
                //登陆 放行
                chain.doFilter(req, resp);
            }else {

                request.setAttribute("login_msg","您还没有登陆，请登录");
                request.getRequestDispatcher("/login.jsp").forward(request,resp);
            }
        }



//        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {

    }

}
