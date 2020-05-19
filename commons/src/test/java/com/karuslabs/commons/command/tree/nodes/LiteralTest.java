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

import static org.junit.jupiter.api.Assertions.*;


class LiteralTest {
    
    static final Command<String> ARGUMENT = val -> 0;
    static final Command<String> COMMAND = val -> 0;
    
    
    Literal<String> literal = Literal.<String>builder("literal").alias("alias").build();
    Literal<String> child = Literal.<String>builder("child").alias("child_alias").executes(COMMAND).build();
    Literal<String> grandchild = Literal.<String>builder("grandchild").alias("grandchild_alias").executes(COMMAND).build();
    Literal<String> extensive = Literal.<String>builder("child").alias("child_alias", "child_alias_other").then(grandchild).executes(COMMAND).build();
    Argument<String, String> argument = Argument.<String, String>builder("child", StringArgumentType.string()).then(Literal.<String>builder("a").build()).executes(ARGUMENT).build();
    
    
    @Test
    void alias() {
        literal.addChild(argument);
        
        var alias = Literal.alias(literal, "alias");
        
        assertEquals("alias", alias.getName());
        assertTrue(alias.isAlias());
        assertEquals(literal.getCommand(), alias.getCommand());
        assertEquals(1, alias.getChildren().size());
        assertTrue(literal.aliases().contains(alias));
    }
    
    
    @Test
    void addChild_child() {
        literal.addChild(child);
        var alias = literal.aliases().get(0);
        
        assertEquals(2, literal.getChildren().size());
        assertEquals(2, alias.getChildren().size());
        
        assertEquals(1, ((Aliasable<String>) literal.getChild("child")).aliases().size());
        assertSame(this.child, literal.getChild("child"));
        assertSame(this.child.aliases().get(0), literal.getChild("child_alias"));
    }
    
    
    @Test
    void addChild_child_extensive() {
        literal.addChild(child);
        literal.addChild(extensive);
        
        var alias = literal.aliases().get(0);
        
        assertEquals(3, literal.getChildren().size());
        assertEquals(3, alias.getChildren().size());
        
        var child = (Literal<String>) literal.getChild("child");
        
        assertSame(extensive, child);
        assertEquals(2, child.aliases().size());
        assertEquals(2, child.aliases().get(0).getChildren().size());
        assertTrue(((Aliasable<String>) child.aliases().get(0)).aliases().isEmpty());
        assertEquals(2, child.getChildren().size());
    }
    
    
    @Test
    void addChild_extensive_child() {
        literal.addChild(extensive);
        literal.addChild(child);
        
        var alias = literal.aliases().get(0);
        
        assertEquals(2, literal.getChildren().size());
        assertEquals(2, alias.getChildren().size());
        
        var child = (Literal<String>) literal.getChild("child");
        
        assertSame(this.child, child);
        assertEquals(1, child.aliases().size());
        assertEquals(2, child.aliases().get(0).getChildren().size());
        assertTrue(((Aliasable<String>) child.aliases().get(0)).aliases().isEmpty());
        assertEquals(2, child.getChildren().size());
    }
    
    
    @Test
    void addChild_argument() {
        literal.addChild(argument);
        
        var alias = literal.aliases().get(0);
        
        assertEquals(1, literal.getChildren().size());
        assertEquals(1, alias.getChildren().size());
        assertSame(this.argument, literal.getChild("child"));
    }
    
    
    @Test
    void addChild_argument_extensive() {
        literal.addChild(argument);
        literal.addChild(extensive);
        
        var alias = literal.aliases().get(0);
        
        assertEquals(3, literal.getChildren().size());
        assertEquals(3, alias.getChildren().size());
        
        assertSame(extensive, literal.getChild("child"));
        assertEquals(3, extensive.getChildren().size());
    }
    
    
    @Test
    void addChild_extensive_argument() {
        literal.addChild(extensive);
        literal.addChild(argument);
        
        var alias = literal.aliases().get(0);
        
        assertEquals(1, literal.getChildren().size());
        assertEquals(1, alias.getChildren().size());
        
        assertSame(argument, literal.getChild("child"));
        assertEquals(3, argument.getChildren().size());
    }

} 
