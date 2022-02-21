package com.snow.common.utils;

import lombok.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2022/2/21 16:10
 */
public class GenericUtil {

    public static <T> Class<T> getGenericTypeOf(@NonNull Class<?> genericSuperClass, int index, @NonNull Class<?> endClass) {
        if (genericSuperClass == null) {
            throw new NullPointerException("genericSuperClass is marked non-null but is null");
        } else if (endClass == null) {
            throw new NullPointerException("endClass is marked non-null but is null");
        } else {
            TypeVariable<? extends Class<?>>[] typeParameters = genericSuperClass.getTypeParameters();
            if (typeParameters.length == 0) {
                throw new IllegalArgumentException("非泛型类:" + genericSuperClass);
            } else if (index >= typeParameters.length) {
                throw new IllegalArgumentException(String.format("泛型下标应为%s ~ %s", 0, typeParameters.length - 1));
            } else if (endClass.equals(genericSuperClass)) {
                throw new IllegalArgumentException("未找到泛型参数类");
            } else if (!genericSuperClass.isAssignableFrom(endClass)) {
                throw new IllegalArgumentException("encClass必须是genericSuperClass子类");
            } else {
                List<Class<?>> subClassList = new LinkedList();

                Class result;
                for(result = endClass; !result.equals(genericSuperClass) && !result.equals(Object.class); result = result.getSuperclass()) {
                    subClassList.add(0, result);
                }

                result = findInternal(subClassList, index);
                if (result == null) {
                    throw new IllegalArgumentException("未找到泛型参数类");
                } else {
                    return result;
                }
            }
        }
    }

    private static Class<?> findInternal(List<Class<?>> subClazz, int index) {
        Type superClass = ((Class)subClazz.get(0)).getGenericSuperclass();
        Type[] targets = ((ParameterizedType)superClass).getActualTypeArguments();
        if (targets[index] instanceof Class) {
            return (Class)targets[index];
        } else {
            GenericUtil.Meta m = (GenericUtil.Meta)getMetas((Class)subClazz.get(0)).get(targets[index].getTypeName());
            return subClazz.size() <= 1 ? null : findInternal(subClazz.subList(1, subClazz.size()), m.getIndex());
        }
    }

    private static Map<String, GenericUtil.Meta> getMetas(Class<?> genericClass) {
        TypeVariable<? extends Class<?>>[] typeParameters = genericClass.getTypeParameters();
        return (Map) IntStream.range(0, typeParameters.length).mapToObj((i) -> {
            return new GenericUtil.Meta(typeParameters[i].getName(), i);
        }).collect(HashMap::new, (m, v) -> {
            GenericUtil.Meta var10000 = (GenericUtil.Meta)m.put(v.getName(), v);
        }, HashMap::putAll);
    }

    private GenericUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    private static class Meta {
        private final String name;
        private final int index;

        public Meta(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return this.name;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
