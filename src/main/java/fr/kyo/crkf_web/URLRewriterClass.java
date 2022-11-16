package fr.kyo.crkf_web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class URLRewriterClass implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest srequest = (HttpServletRequest) request;
        HttpServletResponse sresponse = (HttpServletResponse) response;

        String url = srequest.getRequestURI().trim();
        System.out.println(url);
        url = url.substring(url.indexOf("/faces/") + 7);
        StringBuilder forward = new StringBuilder();

        forward.append("/faces/");
        if (!url.matches(".\\.jsf$")) {
            forward.append(url);
            forward.append(".jsf");
        }
        else
            forward.append(url);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(forward.toString());
        requestDispatcher.forward(srequest, sresponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
