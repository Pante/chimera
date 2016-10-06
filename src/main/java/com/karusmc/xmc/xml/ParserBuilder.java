/*
 * Copyright (C) 2016 PanteLegacy @ karusmc.com
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
package com.karusmc.xmc.xml;

import com.karusmc.xmc.xml.tags.*;
import javax.xml.namespace.QName;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class ParserBuilder {
    
    public static Parser buildCommandParser() {
        BlockTag tag = new BlockTag("command");
        tag.register(new QName("meta"), new MetaTag());
        tag.register(new QName("permission"), new PermissionTag());
        tag.register(new QName("commands"), new CommandsTag(tag));
        tag.register(new QName("configuration"), buildConfigurationBlock());
        
        return new Parser(tag, "commands.dtd", "dtd/commands.dtd");
    }
    
    public static Parser buildConfigurationParser() {
        BlockTag tag = new BlockTag("command");
        tag.register(new QName("configuration"), buildConfigurationBlock());
        
        return new Parser(tag, "configuration.dtd", "dtd/configuration.dtd");
    }
    
    
    private static Tag buildConfigurationBlock() {
        BlockTag tag = new BlockTag("configuration");
        tag.register(new QName("cooldown"), new CooldownTag());
        tag.register(new QName("worlds"), new WorldsTag());
        
        return tag;
    }
    
}
