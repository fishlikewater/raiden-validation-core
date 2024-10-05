/*
 * Copyright (c) 2024 zhangxiang (fishlikewater@126.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.fishlikewater.raiden.validation.core;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code CollectionUtils}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/04/30
 */
public final class CollectionUtils {

    /**
     * 构建集合
     */
    @SafeVarargs
    public static <T> List<T> ofList(T... t) {
        return new ArrayList<>(Arrays.asList(t));
    }

    /**
     * 获取空的集合
     */
    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 构建集合
     */
    public static <T> List<T> newList() {
        return new ArrayList<T>();
    }

    /**
     * 获取空的集合
     */
    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 获取空的MAP
     */
    public static <K, T> Map<K, T> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        if (collection instanceof List) {
            List<T> list = (List<T>) collection;
            list.sort(comparator);
            return list;
        }
        if (collection instanceof SortedSet) {
            SortedSet<T> sortedSet = (SortedSet<T>) collection;
            return sortedSet
                    .stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
        throw new IllegalArgumentException("not support!!!");
    }

    /**
     * 获取集合的第一个元素
     *
     * @param collection 集合
     * @param <T>        泛型
     * @return 第一个元素
     */
    public static <T> T getFirst(Collection<T> collection) {
        return isNotEmpty(collection) ? collection.iterator().next() : null;
    }

    /**
     * 判断对象是否为空
     *
     * @param object 对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).isEmpty();
        }
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        }
        if (object instanceof Iterable) {
            return !((Iterable<?>) object).iterator().hasNext();
        }
        if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        }
        if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        }
        if (object instanceof Iterator) {
            return !((Iterator<?>) object).hasNext();
        }
        if (object instanceof Enumeration) {
            return !((Enumeration<?>) object).hasMoreElements();
        }
        try {
            return Array.getLength(object) == 0;
        } catch (IllegalArgumentException var2) {
            throw new IllegalArgumentException("Unsupported object type: " + object.getClass().getName());
        }
    }

    /**
     * 判断对象是否不为空
     *
     * @param object 对象
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object object) {
        return !isEmpty(object);
    }

    /**
     * 改变数组大小
     *
     * @param bytes   原数组
     * @param newSize 新大小
     * @return 新数组
     */
    public static byte[] resize(byte[] bytes, int newSize) {
        if (newSize < 0) {
            return bytes;
        }
        final byte[] newArray = new byte[newSize];
        if (newSize > 0 && isNotEmpty(bytes)) {
            System.arraycopy(bytes, 0, newArray, 0, Math.min(bytes.length, newSize));
        }
        return newArray;
    }
}
