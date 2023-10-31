package com.im.flashcomms.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.rmi.server.ExportException;
import java.util.Optional;

public class SpElUtils {

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();


    public static String getMethodKey(Method method){
        return method.getDeclaringClass() + "#"+ method.getName();
    }

    public static String parseSpEl(Method method, Object[] args, String key) {
        String[] params = Optional.ofNullable(PARAMETER_NAME_DISCOVERER.getParameterNames(method)).orElse(new String[]{});
        EvaluationContext context = new StandardEvaluationContext();
        for(int i = 0; i < params.length ; i ++){
            //入参  params是参数名字
            context.setVariable(params[i],args[i]);
        }
        Expression expression = PARSER.parseExpression(key);
        return expression.getValue(context,String.class);
    }
}
