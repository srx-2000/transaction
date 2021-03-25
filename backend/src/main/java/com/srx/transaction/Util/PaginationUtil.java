package com.srx.transaction.Util;


import com.srx.transaction.Entities.DTO.ResultMessage;
import com.srx.transaction.Entities.Shop;
import com.srx.transaction.Exception.ReflectExtion;
import com.srx.transaction.Serivce.BaseService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.srx.transaction.Enum.ResultCode.*;
import static com.srx.transaction.Enum.ResultCode.ERROR_INDEX;

public class PaginationUtil {

    public static ResultMessage getPaginationResult(Integer currentPage, Integer pageSize, BaseService service, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (currentPage > 0) {
            Map<String, Object[]> map = getParam(currentPage, pageSize, args);
            Class[] classes= (Class[]) map.get("classes");
            Object[] params= map.get("params");
            Object[] preParams= map.get("preParams");
            Class<? extends BaseService> serviceClass = service.getClass();
            Method method = serviceClass.getMethod(methodName, classes);
            Class<?> returnType = method.getReturnType();
            if (returnType.isAssignableFrom(List.class)) {
                Object listInvoke = method.invoke(service, params);
                List<Object> list = (List<Object>) listInvoke;
                if (currentPage > 1) {
                    Object previewListInvoke = method.invoke(service, preParams);
                    List<Object> previewList = (List<Object>) previewListInvoke;
                    if (list.size() == 0 && previewList.size() != 0) {
                        return new ResultMessage(ERROR_NO_MORE_DATA);
                    }
                }
                if (list.size() == 0) {
                    return new ResultMessage(ERROR_NO_DATA);
                }
                return new ResultMessage(DATA_RETURN_SUCCESS, list);
            } else {
                throw new ReflectExtion("该反射方法只代理分页服务方法");
            }

        } else {
            return new ResultMessage(ERROR_INDEX);
        }
    }

    private static Map<String, Object[]> getParam(Integer currentPage, Integer pageSize, Object... args) {
        Class[] classes = new Class[args.length + 2];
        for (int i = 0; i < args.length; i++) {
            Class<?> clazz = args[i].getClass();
            classes[i] = clazz;
        }
        classes[args.length] = Integer.class;
        classes[args.length + 1] = Integer.class;
        Object[] params = new Object[args.length + 2];
        for (int i = 0; i < args.length; i++) {
            params[i] = args[i];
        }
        params[args.length] = currentPage;
        params[args.length + 1] = pageSize;
        Object[] preParams = new Object[args.length + 2];
        for (int i = 0; i < args.length; i++) {
            preParams[i] = args[i];
        }
        preParams[args.length] = currentPage-1;
        preParams[args.length + 1] = pageSize;
        Map<String, Object[]> map = new HashMap<>();
        map.put("classes", classes);
        map.put("params", params);
        map.put("preParams", preParams);
        return map;
    }
}
