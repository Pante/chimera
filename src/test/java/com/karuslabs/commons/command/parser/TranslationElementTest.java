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
package com.karuslabs.commons.command.parser;

import com.karuslabs.commons.locale.BundledTranslation;
import com.karuslabs.commons.locale.resources.*;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.configuration.Yaml.COMMANDS;
import static org.junit.jupiter.api.Assertions.*;


public class TranslationElementTest {
    
    private static final ConfigurationSection TRANSLATION = COMMANDS.getConfigurationSection("define.translations.translation");
    
    
    private File folder = new File("");
    private TranslationElement element = new TranslationElement(folder);
    
    
    @Test
    public void parseConfigurationSection() {
        BundledTranslation translation = element.parseConfigurationSection(TRANSLATION);

        Resource[] resources = translation.getControl().getResources();
                
        assertEquals("Resource", translation.getBundleName());
        assertEquals(new File(folder, "path2").getPath(), ((FileResource) resources[0]).getPath());
        assertEquals("path1/", ((EmbeddedResource) resources[1]).getPath());
    }
    
    
    @Test
    public void parseConfigurationSection_ThrowsException() {
        assertEquals(
            "Bundle name undefined for: ", 
            assertThrows(IllegalArgumentException.class, () -> element.parseConfigurationSection(COMMANDS)).getMessage()
        );
    }
    
}
