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
package com.karuslabs.spigot.plugin.processor.processors;

import org.apache.maven.plugin.logging.Log;

import org.bukkit.configuration.ConfigurationSection;


/**
 * A {@code Processor} implementation which processes the value for version in a plugin.yml.
 */
public class VersionProcessor implements Processor {
    
    private Log log;
    private String version;
    private boolean override;
    
    
    /**
     * Constructs a {@code VersionProcessor} wiht the specified log, version and override
     * 
     * @param log the log
     * @param version the version
     * @param override the override
     */
    public VersionProcessor(Log log, String version, boolean override) {
        this.log = log;
        this.version = version;
        this.override = override;
    }
    
    
    /**
     * Sets the version if no version was specified or overriding is allowed.
     * 
     * @param config the ConfigurationSection
     * @param key the key
     */
    @Override
    public void execute(ConfigurationSection config, String key) {
        String specified = config.getString(key);
        if (specified == null || override) {
            log.info("Detected and setting version to: " + version);
            config.set("version", version);
            
        } else {
            log.info("Skipping as version already exists and overriding is disabled");
        }
    }
    
}
