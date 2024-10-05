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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;
import io.github.fishlikewater.raiden.processor.AbstractRaidenProcessor;
import io.github.fishlikewater.raiden.processor.context.RaidenContext;
import io.github.fishlikewater.raiden.validation.core.annotation.Validation;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code ValidationProcessor}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/10/04
 */
public class ValidationProcessor extends AbstractRaidenProcessor {

    public ValidationProcessor(RaidenContext raidenContext) {
        super(raidenContext);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() == ElementKind.PARAMETER) {
                    continue;
                }
                VariableElement parameter = (VariableElement) element;
                // 获取注解实例
                Validation validation = parameter.getAnnotation(Validation.class);
                if (validation != null) {
                    this.addValidCode(roundEnv, parameter, validation);
                }
            }
        }
        return false;
    }

    private void addValidCode(RoundEnvironment roundEnv, VariableElement parameter, Validation validation) {
        Element enclosingElement = parameter.getEnclosingElement();
        if (enclosingElement.getKind() == ElementKind.METHOD) {
            RaidenContext context = this.getRaidenContext();
            TreeMaker treeMaker = context.getTreeMaker();
            JCTree.JCArrayTypeTree classType = treeMaker.TypeArray(context.memberAccess("java.lang.Class"));
            JCTree.JCExpression initializer = this.createInitializer(treeMaker, context.getNames(), validation);
            JCTree.JCVariableDecl makeVarDef = context.makeVarDef(
                    treeMaker.Modifiers(0),
                    "classArr",
                    classType,
                    initializer);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add("io.github.fishlikewater.raiden.validation.core.annotation.Validation");
        return set;
    }

    private JCTree.JCExpression createInitializer(TreeMaker maker, Names names, Validation validation) {
        JCTree.JCExpression validationExpr = maker.Ident(names.fromString("validation"));
        JCTree.JCExpression groupsMethod = maker.Select(validationExpr, names.fromString("groups"));
        return maker.NewArray(null, List.of(maker.TypeArray(maker.Ident(names.fromString("Class")))), List.of(groupsMethod));
    }
}
