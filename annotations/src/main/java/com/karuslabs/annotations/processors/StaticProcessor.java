/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.annotations.processors;

import com.sun.source.tree.*;

import java.util.List;

import javax.annotation.processing.*;
import javax.lang.model.element.*;

import static javax.lang.model.element.Modifier.*;


@SupportedAnnotationTypes({
    "com.karuslabs.annotations.Static"
})
public class StaticProcessor extends TreeProcessor {
    
    @Override
    protected void process(Element element) {
        ClassTree tree = (ClassTree) trees.getTree(element);
        for (Tree member : tree.getMembers()) {
            if (member instanceof MethodTree) {
                checkMethod((MethodTree) member);
                
            } else if (member instanceof VariableTree) {
                checkField((VariableTree) member);
            }
        }
    }
    
    protected void checkMethod(MethodTree member) {
        if (member.getName().contentEquals("<init>") && (!member.getModifiers().getFlags().contains(PRIVATE) && !isEmpty(member))) {
            error(member, "Invalid constructor modifier, constructor must either be private or undeclared");
        }
    }
    
    protected boolean isEmpty(MethodTree tree) {
        List<StatementTree> statements = (List<StatementTree>) tree.getBody().getStatements();
        if (statements.size() > 1 || !tree.getParameters().isEmpty()) {
            return false;
        }
        
        ExpressionStatementTree statement = (ExpressionStatementTree) statements.get(0);
        if (statement.getExpression() instanceof MethodInvocationTree) {
            return ((MethodInvocationTree) statement.getExpression()).getArguments().isEmpty();
            
        } else {
            return false;
        }
    }
    
    protected void checkField(VariableTree field) {
        if (!field.getModifiers().getFlags().contains(STATIC)) {
            error(field, "Invalid field, field must be declared static");
        }
    }
    
}
