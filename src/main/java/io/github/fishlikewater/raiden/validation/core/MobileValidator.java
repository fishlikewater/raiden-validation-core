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

import io.github.fishlikewater.raiden.validation.core.annotation.Mobile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * {@code Mobile}
 * 手机号验证
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/15
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private String regex;

    private boolean allowNull;

    @Override
    public void initialize(Mobile mobile) {
        this.allowNull = mobile.allowNull();
        this.regex = mobile.regex();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ObjectUtils.isNullOrEmpty(value)) {
            return this.allowNull;
        }
        return value.matches(regex);
    }
}
