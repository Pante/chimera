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
package com.karuslabs.commons.world;

import com.karuslabs.commons.util.Weak;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.world.EntityLocation.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class EntityLocationTest extends VectorBase {
    
    Location raw = new Location(null, 2, 3, 4);
    Location entityLocation = new Location(null, 1, 2, 3);
    Entity entity = when(mock(Entity.class).getLocation()).thenReturn(entityLocation).getMock();
    EntityLocation<Entity> location = spy(builder(entity, raw).nullable(true).update(true).build());
    
    
    @Test
    void getOffset() {
        assertEquals(new PathVector(1, 1, 1, 0, 0), location.getOffset());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, true, true", "true, false, true", "false, true, true", "false, false, false"})
    void validate(boolean nullable, boolean present, boolean expected) {
        location.nullable = nullable;
        location.entity = present ? location.entity : new Weak<>(null);
        
        assertEquals(expected, location.validate());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, true, 1, 2, 3", "true, false, 2, 3, 4", "false, true, 2, 3, 4"})
    void update(boolean present, boolean update, int x, int y, int z) {
        location.entity = present ? location.entity : new Weak<>(null);
        location.update = update;
        
        doNothing().when(location).updateOffset();
        
        location.update();
        
        assertVector(from(x, y, z), location.location);
    }
    
}
