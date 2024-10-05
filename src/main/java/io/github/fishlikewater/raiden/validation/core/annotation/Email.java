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
package io.github.fishlikewater.raiden.validation.core.annotation;

import io.github.fishlikewater.raiden.validation.core.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * {@code Email}
 * 邮箱校验
 *
 * @author zhangxiang
 * @version 1.0.3
 * @since 2024/07/15
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = EmailValidator.class
)
public @interface Email {

    String regex() default "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";

    boolean allowNull() default true;

    String message() default "values that are not allowed to be entered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
