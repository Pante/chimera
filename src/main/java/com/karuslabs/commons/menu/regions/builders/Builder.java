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
package com.karuslabs.commons.menu.regions.builders;

import com.karuslabs.commons.menu.buttons.Button;
import com.karuslabs.commons.menu.regions.Region;
import java.util.HashMap;

import java.util.Map;


public abstract class Builder<GenericBuilder extends Builder, GenericRegion extends Region, GenericButton extends Button> {
    
    protected String permission;
    protected Map<Integer, Button> buttons;
    
    
    public Builder() {
        permission = "";
        buttons = new HashMap<>();
    }
    
    
    public GenericBuilder permission(String permission) {
        this.permission = permission;
        return getThis();
    }
    
    public GenericBuilder button(int slot, GenericButton button) {
        buttons.put(slot, button);
        return getThis();
    }
    
    
    protected abstract GenericBuilder getThis();
    
    public abstract GenericRegion build();
    
}
