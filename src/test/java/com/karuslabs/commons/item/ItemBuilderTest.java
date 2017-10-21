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
package com.karuslabs.commons.item;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


@TestInstance(PER_CLASS)
class ItemBuilderTest {
    
    static ItemStack item = mock(ItemStack.class);
    static ItemBuilder builder = new ItemBuilder(item);
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void as(ItemMeta meta, Supplier<Builder> supplier) {
        when(item.getItemMeta()).thenReturn(meta);
        
        assertNotNull(supplier.get());
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(
            of(mock(BannerMeta.class), wrap(builder::asBanner)),
            of(mock(BlockStateMeta.class), wrap(builder::asBlockState)),
            of(mock(BookMeta.class), wrap(builder::asBook)),
            of(mock(EnchantmentStorageMeta.class), wrap(builder::asEnchantmentStorage)),
            of(mock(FireworkMeta.class), wrap(builder::asFirework)),
            of(mock(FireworkEffectMeta.class), wrap(builder::asFireworkEffect)),
            of(mock(KnowledgeBookMeta.class), wrap(builder::asKnowledgeBook)),
            of(mock(LeatherArmorMeta.class), wrap(builder::asLeatherArmor)),
            of(mock(MapMeta.class), wrap(builder::asMap)),
            of(mock(PotionMeta.class), wrap(builder::asPotion)),
            of(mock(SkullMeta.class), wrap(builder::asSkull)),
            of(mock(SpawnEggMeta.class), wrap(builder::asSpawnEgg))
        );
    }
    
    static Object wrap(Supplier<Builder> supplier) {
        return supplier;
    }
    
}
