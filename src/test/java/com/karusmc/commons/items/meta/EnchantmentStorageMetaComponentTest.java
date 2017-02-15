/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.items.meta;

import com.karusmc.commons.core.test.XMLResource;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import org.junit.*;

import static org.mockito.Mockito.*;


public class EnchantmentStorageMetaComponentTest {
    
    @Rule
    public XMLResource xml = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("items/meta/StorageMeta.xml"), null);
    
    @Rule
    public ItemMetaResource<EnchantmentStorageMeta> resource = new ItemMetaResource(new EnchantmentStorageMetaComponent(), mock(EnchantmentStorageMeta.class));
    
    
    @Test
    public void parse() {
        resource.parse(xml.getRoot());
        resource.assertMeta();
        
        EnchantmentStorageMeta meta = resource.getMeta();
        
        verify(meta, times(1)).addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
    }
    
}
