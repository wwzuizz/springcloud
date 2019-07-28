/**
 *
 */

package com.linwen.aop;

import com.linwen.comm.base.BaseController;
import com.linwen.comm.validation.ValidationParameters;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Guocg
 */
@Aspect
@Component
public class MyAspect {
    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 只切控制器的方法
     */
    @Pointcut("(" +
            "execution(public * com.linwen.controller..*Controller.*(..))"
            + ")"
    )
    void pointcut() {
    }

    @Pointcut("@annotation(com.linwen.comm.validation.annotation.OAuthRequired)")
    void OAuthRequired() {
    }

    @Pointcut("@annotation(com.linwen.comm.validation.annotation.ValidationRequest)")
    void ValidationRequest() {
    }

    /**
     * Around 之后
     *
     * @param jp
     */
    @Before("pointcut()")
    public void Before(JoinPoint jp) {
        Object[] objects = jp.getArgs();
        try {
            AopUiit.settingHttpParameter(jp.getTarget(), objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用方法之前
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointcut()||OAuthRequired()||ValidationRequest()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Object baseController = AopUiit.handleOauth(pjp, method);
        if (baseController != null) return baseController;
        ValidationParameters validationParameters = new ValidationParameters();
        baseController = validationParameters.handleParameters(pjp, method);
        if (baseController != null) return baseController;
        baseController = IpRolePermitted.isRolePermitted(pjp);
        if (baseController != null) return baseController;
        AopUiit.edit(pjp);
        return pjp.proceed();
    }

    /**
     * After 之后
     *
     * @param jp
     * @param returning
     */
    @AfterReturning(pointcut = "pointcut()", returning = "returning")
    public void AfterReturning(JoinPoint jp, Object returning) {
        Object Object = jp.getTarget();
        if (Object instanceof BaseController) {
            HttpServletRequest request = ((BaseController) Object).getHttpServletRequest();
            if (request != null) {
                String servletPath = request.getServletPath().replaceFirst("/", "");
                DocGen.DocGen(jp, returning, servletPath);

//                BaseUser user = (BaseUser) BaseController.isLogin();
//                if (user != null && servletPath.contains("admin")) {
//                    if (servletPath.contains("Add") || servletPath.contains("Edit") || servletPath.contains("delete")) {
//                    }
//                }
            }
        }
    }
}
