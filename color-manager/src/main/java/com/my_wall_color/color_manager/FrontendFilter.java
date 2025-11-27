package com.my_wall_color.color_manager;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class FrontendFilter implements Filter {
    private final String indexContent;

    FrontendFilter(@Value("classpath:/frontend/index.html") Resource indexResource) throws IOException {
        indexContent = indexResource.getContentAsString(StandardCharsets.UTF_8);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest httpRequest)) return;

        String requestURI = httpRequest.getRequestURI();
        boolean isApiPath = requestURI.startsWith("/api");
        boolean isStaticFile = requestURI.matches(".*\\.\\w+$"); // has extension
        if (isApiPath || isStaticFile) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        };

        servletResponse.setContentLength(indexContent.length());
        servletResponse.setContentType(MediaType.TEXT_HTML_VALUE);
        servletResponse.getOutputStream().println(indexContent);
    }
}
