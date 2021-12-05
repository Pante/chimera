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

import com.karuslabs.commons.MockBukkit;

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
    @MethodSource("builder_parameters")
    void builder(Class<? extends ItemMeta> type, Function<ItemBuilder, Builder> function, Class<? extends Builder> expected) {
        MockBukkit.meta(type);
        
        var old = ItemBuilder.of(Material.WATER).self();
        var builder = function.apply(old);
        
        assertEquals(expected, builder.getClass());
        assertNotNull(old.item);
        assertNotNull(old.meta);
        assertNotNull(builder.item);
        assertNotNull(builder.meta);
    }
    
    static Stream<Arguments> builder_parameters() {
        return Stream.of(
            of(AxolotlBucketMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::axolotlBucket, AxolotlBucketBuilder.class),
            of(BannerMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::banner, BannerBuilder.class),
            of(BlockDataMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::blockData, BlockDataBuilder.class),
            of(BlockStateMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::blockState, BlockStateBuilder.class),
            of(BookMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::book, BookBuilder.class),
            of(BundleMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::bundle, BundleBuilder.class),
            of(CompassMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::compass, CompassBuilder.class),
            of(CrossbowMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::crossbow, CrossbowBuilder.class),
            of(EnchantmentStorageMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::enchantmentStorage, EnchantmentStorageBuilder.class),
            of(FireworkMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::firework, FireworkBuilder.class),
            of(FireworkEffectMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::fireworkEffect, FireworkEffectBuilder.class),
            of(KnowledgeBookMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::knowledgeBook, KnowledgeBookBuilder.class),
            of(LeatherArmorMeta.class, (Function<ItemBuilder,  Builder>) ItemBuilder::leatherArmour, LeatherArmourBuilder.class),
            of(MapMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::map, MapBuilder.class),
            of(PotionMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::potion, PotionBuilder.class),
            of(SkullMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::head, HeadBuilder.class),
            of(SuspiciousStewMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::stew, SuspiciousStewBuilder.class),
            of(TropicalFishBucketMeta.class, (Function<ItemBuilder, Builder>) ItemBuilder::tropicalFishBucket, TropicalFishBucketBuilder.class)
        );
    }
    
}
