package org.openmole.misc.caching;

import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class MethodCache {

    final Map<Object, Map<String, Object>> cache = new WeakHashMap<Object, Map<String, Object>>();

    void putCachedMethodResult(Object object, String method, Object result) {
        getMethodMap(object).put(method, result);
    }

    /*boolean isCachedMethodResult(Object object, String method) {
    return getMethodMap(object).containsKey(method);
    }*/
    Object getCachedMethodResult(Object object, String method) {
        Map<String, Object> methodMap = cache.get(object);
        if (methodMap == null) {
            return null;
        }

        return methodMap.get(method);
    }

    private Map<String, Object> getMethodMap(Object object) {
        Map<String, Object> methodMap = cache.get(object);
        if (methodMap != null) {
            return methodMap;
        }

        synchronized (cache) {
            methodMap = cache.get(object);
            if (methodMap == null) {
                methodMap = new TreeMap<String, Object>();
                cache.put(object, methodMap);
            }
            return methodMap;
        }
    }

    void clear(Object obj) {
        cache.remove(obj);
    }

    public int size() {
        return cache.size();
    }
}
