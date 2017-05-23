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

import com.karuslabs.commons.menu.buttons.RadioButton;
import com.karuslabs.commons.menu.regions.RadioRegion;


public class RadioRegionBuilder extends Builder<RadioRegionBuilder, RadioRegion, RadioButton> {
    
    
    @Override
    protected RadioRegionBuilder getThis() {
        return this;
    }

    @Override
    public RadioRegion build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
