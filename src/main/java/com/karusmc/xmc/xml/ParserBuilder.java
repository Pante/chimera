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

import com.karusmc.xmc.xml.nodes.*;
import com.karusmc.xmc.xml.tags.*;

/**
 *
 * @author PanteLegacy @ karusmc.com
 */
public class ParserBuilder {
    
    public static Parser buildCommandsParser(String path) {
        GenericNode node = new GenericNode("command");
        node.getNodes().put("configuration", buildConfigurationNode());
        
        node.getTags().put("meta", new MetaTag());
        node.getTags().put("permission", new PermissionTag());
        
        return new Parser(new CommandsNode(node), "commands.", path);
    }
    
    
    public static Parser buildConfigurationParser(String path) {
        GenericNode node = new GenericNode("command");
        node.getNodes().put("configuration", buildConfigurationNode());
        
        return new Parser(new CommandsNode(node), "configuration.dtd", path);
    }
    
    
    public static Node buildConfigurationNode() {
        GenericNode configurationNode = new GenericNode("configuration");
        
        configurationNode.getTags().put("cooldown", new CooldownTag());
        configurationNode.getTags().put("worlds", new WorldsTag());
        
        return configurationNode;
    }
    
}
