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

/**
 * <p>
 * {@code ValueLimitEnum}
 * </p>
 * 值限定枚举
 *
 * @author fishlikewater@126.com
 * @version 1.0.2
 * @since 2024年06月06日 23:13
 **/
public interface BaseEnum<T> {

    /**
     * 获取枚举编码
     *
     * @return 编码
     */
    T getCode();
}
