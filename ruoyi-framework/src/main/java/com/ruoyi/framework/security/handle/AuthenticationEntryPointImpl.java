package com.ruoyi.framework.security.handle;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 认证失败处理类 返回未授权
 *
 * @author ruoyi
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    // 静态资源文件夹路径（可以根据需要修改）
    private static final String STATIC_RESOURCE_PATH = "/static/";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {

        // 获取请求的路径
        String requestURI = request.getRequestURI();

        // 如果请求路径是静态资源路径，直接放行
        if (requestURI.startsWith(STATIC_RESOURCE_PATH)) {
            // 继续处理静态资源请求，不进行认证处理
            response.setStatus(HttpServletResponse.SC_OK);  // 设置HTTP状态为200
            return;
        }
        // 认证失败的处理逻辑
        String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(403, msg)));
    }
}

