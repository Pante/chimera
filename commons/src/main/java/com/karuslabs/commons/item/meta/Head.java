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

import java.util.UUID;

import static java.util.UUID.fromString;


public enum Head {

    ALEX("MHF_ALEX", "6ab43178-89fd-4905-97f6-0f67d9d76fd9"),
    BLAZE("MHF_BLAZE", "4c38ed11-596a-4fd4-ab1d-26f386c1cbac"),
    CAVE_SPIDER("MHF_CaveSpider", "cab28771-f0cd-4fe7-b129-02c69eba79a5"),
    CHICKEN("MHF_Chicken", "92deafa9-4307-42d9-b003-88601598d6c0"),
    COW("MHF_COW", "f159b274-c22e-4340-b7c1-52abde147713"),
    CREEPER("MHF_Creeper", "057b1c47-1321-4863-a6fe-8887f9ec265f"),
    ENDERMAN("MHF_Enderman", "40ffb372-12f6-4678-b3f2-2176bf56dd4b"),
    GHAST("MHF_Ghast", "063085a6-797f-4785-be1a-21cd7580f752"),
    GOLEM("MHF_Golem", "757f90b2-2344-4b8d-8dac-824232e2cece"),
    HEROBRINE("MHF_Herobrine", "9586e5ab-157a-4658-ad80-b07552a9ca63"),
    MAGMA_CUBE("MHF_LavaSlime", "0972bdd1-4b86-49fb-9ecc-a353f8491a51"),
    MUSHROOM_COW("MHF_MushroomCow", "a46817d6-73c5-4f3f-b712-af6b3ff47b96"),
    OCELOT("MHF_Ocelot", "1bee9df5-4f71-42a2-bf52-d97970d3fea3"),
    PIG("MHF_Pig", "8b57078b-f1bd-45df-83c4-d88d16768fbe"),
    SHEEP("MHF_Sheep", "dfaad551-4e7e-45a1-a6f7-c6fc5ec823ac"),
    SKELETON("MHF_Skeleton", "a3f427a8-18c5-49c5-a4fb-64c6e0e1e0a8"),
    SLIME("MHF_Slime", "870aba93-40e8-48b3-89c5-32ece00d6630"),
    SPIDER("MHF_Spider", "5ad55f34-41b6-4bd2-9c32-18983c635936"),
    SQUID("MHF_Squid", "72e64683-e313-4c36-a408-c66b64e94af5"),
    STEVE("MHF_Steve", "c06f8906-4c8a-4911-9c29-ea1dbd1aab82"),
    VILLAGER("MHF_Villager", "bd482739-767c-45dc-a1f8-c33c40530952"),
    WITHER_SKELETON("MHF_WSkeleton", "7ed571a5-9fb8-416c-8b9d-fb2f446ab5b2"),
    ZOMBIE("MHF_Zombie", "daca2c3d-719b-41f5-b624-e4039e6c04bd"),
    ZOMBIE_PIGMAN("MHF_PigZombie", "18a2bb50-334a-4084-9184-2c380251a24b"),
    
    CACTUS("MHF_Cactus", "1d9048db-e836-4b9a-a108-55014922f1ae"),
    CAKE("MHF_Cake", "afb489c4-9fc8-48a4-98f2-dd7bea414c9a"),
    CHEST("MHF_CHEST", "73d4e068-3a6d-4c8b-8f85-3323546955c4"),
    BROWN_COCONUT("MHF_CoconutB", "62efa973-f626-4092-aede-57ffbe84ff2b"),
    GREEN_COCONUT("MHF_CoconutG", "74556fea-28ed-4458-8db2-9a8220da0c12"),
    MELON("MHF_Melon", "1c7d9784-47ea-4bf3-bc23-acf260b436e6"),
    OAK_LOG("MHF_OakLog", "e224e5ec-e299-4005-ae22-3b0f77a57714"),
    GREEN_PRESENT("MHF_Present1", "156b251b-12e0-4829-a130-a61b53ba7720"),
    RED_PRESENT("MHF_Present2", "f1eb7cad-e2c0-4e9e-8aad-1eae21d5fd95"),
    PUMPKIN("MHF_Pumpkin", "f44d355b-b6ae-4ba8-8e62-ae6441854785"),
    DERPY_TNT("MHF_TNT", "d43af93c-c330-4a3d-bab8-ee74234a011a"),
    TNT("MHF_TNT2", "55e73380-a973-4a52-9bb5-1efa5256125c"),
    
    UP_ARROW("MHF_ArrowUp", "fef039ef-e6cd-4987-9c84-26a3e6134277"),
    DOWN_ARROW("MHF_ArrowDown", "68f59b9b-5b0b-4b05-a9f2-e1d1405aa348"),
    LEFT_ARROW("MHF_ArrowLeft", "a68f0b64-8d14-4000-a95f-4b9ba14f8df9"),
    RIGHT_ARROW("MHF_ArrowRight", "50c8510b-5ea0-4d60-be9a-7d542d6cd156"),
    EXCLAMATION("MHF_Exclamation", "d3c47f6f-ae3a-45c1-ad7c-e2c762b03ae6"),
    QUESTION_MARK("MHF_Question", "606e2ff0-ed77-4842-9d6c-e1d3321c7838");

    
    private final String name;
    private final UUID id;
    
    
    private Head(String name, String id) {
        this.name = name;
        this.id = fromString(id);
    }

    
    public String getName() {
        return name;
    }
    
    public UUID getID() {
        return id;
    }

}
