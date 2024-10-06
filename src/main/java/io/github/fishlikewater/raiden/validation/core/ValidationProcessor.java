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

import com.google.auto.service.AutoService;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import io.github.fishlikewater.raiden.processor.AbstractRaidenProcessor;
import io.github.fishlikewater.raiden.processor.context.RaidenContext;
import io.github.fishlikewater.raiden.validation.core.annotation.Validation;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code ValidationProcessor}
 *
 * @author zhangxiang
 * @version 1.0.0
 * @since 2024/10/04
 */
@AutoService(Processor.class)
public class ValidationProcessor extends AbstractRaidenProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element element : elements) {
                if (element.getKind() != ElementKind.PARAMETER) {
                    continue;
                }
                VariableElement parameter = (VariableElement) element;
                Validation validation = parameter.getAnnotation(Validation.class);
                if (validation != null) {
                    this.addValidCode(parameter);
                }
            }
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Collections.singletonList(Validation.class.getName()));
    }

    private void addValidCode(VariableElement parameter) {
        Element enclosingElement = parameter.getEnclosingElement();
        JCTree.JCVariableDecl variableDecl = (JCTree.JCVariableDecl) this.getRaidenContext().getJavaEvn().getElementUtils().getTree(parameter);
        if (enclosingElement.getKind() != ElementKind.METHOD) {
            return;
        }

        RaidenContext context = this.getRaidenContext();
        TreeMaker treeMaker = context.getTreeMaker();
        for (JCTree.JCAnnotation annotation : variableDecl.mods.annotations) {
            if (!annotation.annotationType.toString().equals("Validation")) {
                continue;
            }

            List<JCTree.JCExpression> groups = getGroups(annotation);
            List<JCTree.JCExpression> args = List.of(treeMaker.Ident(getNameFromString(parameter.getSimpleName().toString())));
            if (!groups.isEmpty()) {
                args = args.appendList(groups);
            }
            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) this.getRaidenContext().getTrees().getTree(enclosingElement);
            JCTree.JCExpressionStatement exec = treeMaker.Exec(treeMaker.Apply(
                    List.of(memberAccess("java.lang.Object")),
                    memberAccess("io.github.fishlikewater.raiden.validation.core.ValidationUtil.validate"),
                    args));
            jcMethodDecl.body = treeMaker.Block(0, List.of(exec, jcMethodDecl.body));
            break;
        }
    }

    private List<JCTree.JCExpression> getGroups(JCTree.JCAnnotation annotation) {
        for (JCTree.JCExpression arg : annotation.args) {
            if (!(arg instanceof JCTree.JCAssign)) {
                continue;
            }

            JCTree.JCAssign assign = (JCTree.JCAssign) arg;
            String attributeName = assign.lhs.toString();

            if (attributeName.equals("groups")) {
                if (assign.rhs instanceof JCTree.JCNewArray) {
                    return ((JCTree.JCNewArray) assign.rhs).elems;
                } else if (assign.rhs instanceof JCTree.JCFieldAccess) {
                    return List.of(assign.rhs);
                }
            }
        }
        return List.nil();
    }
}
