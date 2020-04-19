/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
package com.karuslabs.commons.command.tree.nodes;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ArgumentTest {
    
    static final Command<String> ARGUMENT = val -> 0;
    static final Command<String> COMMAND = val -> 0;
    
    
    Argument<String, String> argument = Argument.<String, String>builder("argument", StringArgumentType.string()).executes(ARGUMENT).build();
    Argument<String, String> child = Argument.<String, String>builder("child", StringArgumentType.string()).executes(COMMAND).build();
    Literal<String> grandchild = Literal.<String>builder("grandchild").alias("grandchild_alias").executes(COMMAND).build();
    Literal<String> extensive = Literal.<String>builder("child").alias("child_alias", "child_alias_other").then(grandchild).executes(COMMAND).build();
    Literal<String> literal = Literal.<String>builder("child").alias("alias").build();
    
    
    @Test
    void addChild_child() {
        argument.addChild(child);
        
        assertEquals(1, argument.getChildren().size());
        assertSame(this.child, argument.getChild("child"));
    }
    
    
    @Test
    void addChild_child_extensive() {
        argument.addChild(child);
        argument.addChild(extensive);
        
        assertEquals(3, argument.getChildren().size());
        
        var child = (Literal<String>) argument.getChild("child");
        
        assertSame(this.extensive, child);
        assertEquals(2, child.getChildren().size());
        assertEquals(2, child.aliases().size());
        assertEquals(2, child.aliases().get(0).getChildren().size());
        assertTrue(((Aliasable<String>) child.aliases().get(0)).aliases().isEmpty());
    }
    
    
    @Test
    void addChild_extensive_child() {
        argument.addChild(extensive);
        argument.addChild(child);
        
        assertEquals(1, argument.getChildren().size());
        var child = (Argument<String, String>) argument.getChild("child");
        
        assertSame(this.child, child);
        assertEquals(2, child.getChildren().size());
    }
    
    
    @Test
    void addChild_literal() {
        argument.addChild(literal);
        
        assertEquals(2, argument.getChildren().size());
        assertSame(this.literal, argument.getChild("child"));
    }
    
    
    @Test
    void addChild_literal_extensive() {
        argument.addChild(literal);
        argument.addChild(extensive);
        
        assertEquals(3, argument.getChildren().size());
        
        assertSame(extensive, argument.getChild("child"));
        assertEquals(2, extensive.getChildren().size());
    }
    
    
    @Test
    void addChild_literal_argument() {
        argument.addChild(extensive);
        argument.addChild(literal);
        
        assertEquals(2, argument.getChildren().size());
        
        assertSame(literal, argument.getChild("child"));
        assertEquals(2, extensive.getChildren().size());
    }
    
} 
