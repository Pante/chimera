/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.item.builders;

import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.inventory.meta.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.of;


class ItemBuilderTest {    
    
    @ParameterizedTest
    @MethodSource({"as_provider"})
    void as(Class<? extends ItemMeta> type, Function<ItemBuilder, Builder> function, Class<? extends Builder> expected) {
        StubBukkit.meta(type);
        var old = ItemBuilder.of(Material.WATER).self();
        var builder = function.apply(old);
        
        assertEquals(expected, builder.getClass());
        assertNull(old.item);
        assertNull(old.meta);
        assertNotNull(builder.item);
        assertNotNull(builder.meta);
    }
    
    static Stream<Arguments> as_provider() {
        return Stream.of(
            of(BannerMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asBanner(), BannerBuilder.class),
            of(BlockStateMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asBlockState(), BlockStateBuilder.class),
            of(BookMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asBook(), BookBuilder.class),
            of(CrossbowMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asCrossbow(), CrossbowBuilder.class),
            of(EnchantmentStorageMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asEnchantmentStorage(), EnchantmentStorageBuilder.class),
            of(FireworkMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asFirework(), FireworkBuilder.class),
            of(FireworkEffectMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asFireworkEffect(), FireworkEffectBuilder.class),
            of(KnowledgeBookMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asKnowledgeBook(), KnowledgeBookBuilder.class),
            of(LeatherArmorMeta.class, (Function<ItemBuilder,  Builder>) builder -> builder.asLeatherArmour(), LeatherArmourBuilder.class),
            of(MapMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asMap(), MapBuilder.class),
            of(PotionMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asPotion(), PotionBuilder.class),
            of(SkullMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asSkull(), SkullBuilder.class),
            of(SuspiciousStewMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asStew(), SuspiciousStewBuilder.class),
            of(TropicalFishBucketMeta.class, (Function<ItemBuilder, Builder>) builder -> builder.asBucket(), TropicalFishBucketBuilder.class)
        );
    }
    
}
