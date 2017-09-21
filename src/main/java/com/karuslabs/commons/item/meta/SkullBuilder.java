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
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import static org.bukkit.Bukkit.getOfflinePlayer;


public class SkullBuilder extends Builder<SkullBuilder, SkullMeta> {
    
    public SkullBuilder(ItemStack item) {
        super(item);
    }
    
    public SkullBuilder(Builder builder) {
        super(builder);
    }
    
    
    public SkullBuilder owner(UUID id) {
        return owner(getOfflinePlayer(id));
    }
    
    public SkullBuilder owner(OfflinePlayer player) {
        meta.setOwningPlayer(player);
        return this;
    }
    
    
    public SkullBuilder owner(String name) {
        meta.setOwner(name);
        return this;
    }
    
    public SkullBuilder owner(Name name) {
        meta.setOwner(name.getName());
        return this;
    }
    
    
    @Override
    protected SkullBuilder getThis() {
        return this;
    }
    
    
    public static enum Name {

        ALEX("MHF_ALEX"),
        BLAZE("MHF_BLAZE"),
        CAVE_SPIDER("MHF_CaveSpider"),
        CHICKEN("MHF_Chicken"),
        COW("MHF_COW"),
        CREEPER("MHF_Creeper"),
        ENDERMAN("MHF_Enderman"),
        GHAST("MHF_Ghast"),
        GOLEM("MHF_Golem"),
        HEROBRINE("MHF_Herobrine"),
        LAVASLIME("MHF_LavaSlime"),
        MUSHROOM_COW("MHF_MushroomCow"),
        OCELOT("MHF_Ocelot"),
        PIG("MHF_Pig"),
        PIG_ZOMBIE("MHF_PigZombie"),
        SHEEP("MHF_Sheep"),
        SKELETON("MHF_Skeleton"),
        SLIME("MHF_Slime"),
        SPIDER("MHF_Spider"),
        SQUID("MHF_Squid"),
        STEVE("MHF_Steve"),
        VILLAGER("MHF_Villager"),
        WITHER_SKELETON("MHF_WSkeleton"),
        ZOMBIE("MHF_Zombie"),
        CACTUS("MHF_Cactus"),
        
        CAKE("MHF_Cake"),
        CHEST("MHF_CHEST"),
        BROWN_COCONUT("MHF_CoconutB"),
        GREEN_COCONUT("MHF_CoconutG"),
        MELON("MHF_Melon"),
        OAK_LOG("MHF_OakLog"),
        GREEN_PRESENT("MHF_Present1"),
        RED_PRESENT("MHF_Present2"),
        PUMPKIN("MHF_Pumpkin"),
        DERPY_TNT("MHF_TNT"),
        TNT("MHF_TNT2"),
        
        UP_ARROW("MHF_ArrowUp"),
        DOWN_ARROW("MHF_ArrowDown"),
        LEFT_ARROW("MHF_ArrowLeft"),
        RIGHT_ARROW("MHF_ArrowRight"),
        EXCLAMATION("MHF_Exclamation"),
        QUESTION_MARK("MHF_Question");

        
        private final String name;

        private Name(String name) {
            this.name = name;
        }

        
        public String getName() {
            return name;
        }

    }
    
}
