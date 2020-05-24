/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
package com.karuslabs.commons.command.aot.generation.blocks;

import com.karuslabs.commons.command.aot.*;

import java.text.MessageFormat;
import java.util.*;
import javax.lang.model.element.*;

import org.apache.commons.lang.StringEscapeUtils;

import org.checkerframework.checker.nullness.qual.Nullable;

import static javax.lang.model.element.Modifier.STATIC;


public class MethodBlock {
    
    StringBuilder builder;
    MessageFormat argument;
    MessageFormat literal;
    MessageFormat alias;
    Set<String> variables;
    int count;
    @Nullable CharSequence type;
    
    
    public MethodBlock() {
        builder = new StringBuilder();
        argument = new MessageFormat("        var {0} = new Argument<>(\"{1}\", {2}, {3}, {4}, {5});\n");
        literal = new MessageFormat("        var {0} = new Literal<>(\"{1}\", {2}, {3});\n");
        alias = new MessageFormat("        Literal.alias({0}, \"{1}\");\n");
        variables = new HashSet<>();
        count = 0;
    }
    
    
    public void start(CharSequence type) {
        this.type = type;
        variables.add("source");
        variables.add("commands");
        
        builder.setLength(0);
        builder.append("    public static Map<String, CommandNode<CommandSender>> of(").append(type).append(" source) {\n");
        builder.append("        var commands = new HashMap<String, CommandNode<CommandSender>>();\n\n");
    }
    
    
    public String command(Token token) {
        var variable = "command";
        while (!variables.add(variable)) {
            variable = variable + count++;
        }
        
        switch (token.type) {
            case ARGUMENT:
                argument(variable, token);
                break;
                
            case LITERAL:
                literal(variable, token);
                break;
                
            default:
                throw new IllegalArgumentException("Invalid token type: \"" + token.type + "\", found when generating method");
        }
        
        return variable;
    }
    
    
    void argument(String variable, Token token) {
        builder.append(argument.format(new Object[] {
            variable,
            StringEscapeUtils.escapeJava(token.lexeme),
            parameter(token, Binding.TYPE, "null"),
            parameter(token, Binding.COMMAND, "null"),
            parameter(token, Binding.REQUIREMENT, "REQUIREMENT"),
            parameter(token, Binding.SUGGESTIONS, "null"),
        }));
    }
    
    void literal(String variable, Token token) {
        builder.append(literal.format(new Object[] {
            variable,
            StringEscapeUtils.escapeJava(token.lexeme),
            parameter(token, Binding.COMMAND, "null"),
            parameter(token, Binding.REQUIREMENT, "REQUIREMENT"),
        }));
        
        var parameters = new Object[2];
        for (var name : token.aliases) {
            parameters[0] = variable;
            parameters[1] = StringEscapeUtils.escapeJava(name);
            
            builder.append(alias.format(parameters));
        }
    }
    
    
    String parameter(Token token, Binding binding, String value) {
        var element = token.bindings.get(binding);
        if (element == null) {
            return value;
        }
      
        var name = element.getModifiers().contains(STATIC) ? type : "source";
        
        if (element instanceof ExecutableElement) {
            return name + "::" + element.getSimpleName();
            
        } else if (element instanceof VariableElement) {
            return name + "." + element.getSimpleName();
            
        } else {
            throw new IllegalArgumentException("Invalid bind target: \"" + element.getKind() + "\"");
        }
    }
    
    
    public void addChild(String variable, String child) {
        builder.append("        ").append(variable).append(".addChild(").append(child).append(");\n");
    }
    
    
    public void newLine() {
        builder.append("\n");
    }
    
    
    public String end(List<String> roots) {
        variables.clear();
        count = 0;
        
        for (var root : roots) {
            builder.append("        commands.put(").append(root).append(".getName(), ").append(root).append(");\n");
        }
        
        builder.append("        return commands;\n");
        builder.append("    }\n\n");
        
        return builder.toString();
    }
    
}
