/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.commands;

import junitparams.*;

import org.bukkit.command.CommandSender;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class ArgumentExecutorTest {
    
    private ArgumentExecutor executor;
    
    
    public ArgumentExecutorTest() {
        executor = new StubExecutor(2, mock(Argument.class));
        executor.getArguments()[0] = mock(Argument.class);
    }
    
    
    @Test
    @Parameters
    public void onTabComplete(String[] args, int argumentTimes, int defaultTimes) {
        executor.onTabComplete(null, null, null, args);
        
        verify(executor.getArguments()[0], times(argumentTimes)).complete(null, null, "arg");
        verify(executor.getDefaultArgument(), times(defaultTimes)).complete(null, null, "default");
    }
    
    protected Object[] parametersForOnTabComplete() {
        return new Object[] {
            new Object[] {new String[] {"arg"}, 1, 0},
            new Object[] {new String[] {}, 0, 0},
            new Object[] {new String[] {"arg", "default"}, 0, 1},
            new Object[] {new String[] {"arg", "subarg", "default"}, 0, 1}
        };
    }
    
    
    private static class StubExecutor extends ArgumentExecutor {

        public StubExecutor(int size, Argument defaultArgument) {
            super(size, defaultArgument);
        }
        

        @Override
        public void onExecute(CommandSender sender, Command command, String label, String[] args) {
            
        }
        
    }
    
}
