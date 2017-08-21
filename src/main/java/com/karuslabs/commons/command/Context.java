/*
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
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
package com.karuslabs.commons.command;

import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Context {
    
    private CommandSender sender;
    private String label;
    
    private Command parent;
    private Command calling;
    
    
    public Context(CommandSender sender, String label, Command parent, Command calling) {
        this.sender = sender;
        this.label = label;
        this.parent = parent;
        this.calling = calling;
    }
    
    
    public CommandSender getSender() {
        return sender;
    }
    
    public boolean isPlayer() {
        return sender instanceof Player;
    }
    
    public @Nullable Player getPlayer() {
        if (sender instanceof Player) {
            return (Player) sender;
            
        } else {
            return null;
        }
    }
    
    
    public void set(String label, Command calling) {
        this.label = label;
        parent = this.calling;
        this.calling = calling;
    }

            
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
         
    
    public @Nullable Command getParent() {
        return parent;
    }
    
    public Command getCalling() {
        return calling;
    }
    
}
