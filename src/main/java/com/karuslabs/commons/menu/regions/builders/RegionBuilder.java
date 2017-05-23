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


public class RegionBuilder extends Builder<RegionBuilder, Region, Button> {

    @Override
    protected RegionBuilder getThis() {
        return this;
    }

    @Override
    public Region build() {
        return new Region(buttons, permission);
    }

}
