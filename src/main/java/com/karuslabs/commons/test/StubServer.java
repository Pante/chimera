/*
 * Copyright (C) 2017 Karus Labs
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
package com.karuslabs.commons.test;

import com.avaje.ebean.config.ServerConfig;

import com.karuslabs.commons.annotations.Implemented;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.*;
import org.bukkit.boss.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.*;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import org.bukkit.craftbukkit.v1_11_R1.inventory.*;

import static org.mockito.Mockito.*;


public class StubServer implements Server {
    
    public static final StubServer INSTANCE;
    
    static {
        INSTANCE = new StubServer();
        Bukkit.setServer(INSTANCE);
    }
    
    
    private BukkitScheduler scheduler;
    private PluginManager manager;
    private SimpleCommandMap commandMap;
    private Logger logger;
    
    
    public StubServer() {
        scheduler = new StubScheduler();
        manager = mock(PluginManager.class);
        commandMap = mock(SimpleCommandMap.class);
        logger = mock(Logger.class);
    }
    

    @Override
    @Implemented
    public String getName() {
        return "";
    }
    

    @Override
    @Implemented
    public String getVersion() {
        return "";
    }
    

    @Override
    @Implemented
    public String getBukkitVersion() {
        return "";
    }

    @Override
    public Player[] _INVALID_getOnlinePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Collection<? extends Player> getOnlinePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getMaxPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getPort() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getViewDistance() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getIp() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getServerId() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getWorldType() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean getGenerateStructures() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean getAllowEnd() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean getAllowNether() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean hasWhitelist() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setWhitelist(boolean value) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void reloadWhitelist() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int broadcastMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getUpdateFolder() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getUpdateFolderFile() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public long getConnectionThrottle() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getTicksPerAnimalSpawns() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getTicksPerMonsterSpawns() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Player getPlayer(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Player getPlayerExact(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<Player> matchPlayer(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Player getPlayer(UUID id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    

    @Override
    @Implemented
    public PluginManager getPluginManager() {
        return manager;
    }
    

    @Override
    @Implemented
    public BukkitScheduler getScheduler() {
        return scheduler;
    }

    @Override
    public ServicesManager getServicesManager() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    

    public SimpleCommandMap getSimpleCommandMap() {
        return commandMap;
    }

    @Override
    public List<World> getWorlds() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public World createWorld(WorldCreator creator) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean unloadWorld(String name, boolean save) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean unloadWorld(World world, boolean save) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public World getWorld(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public World getWorld(UUID uid) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public MapView getMap(short id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public MapView createMap(World world) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    

    @Override
    @Implemented
    public Logger getLogger() {
        return logger;
    }

    @Override
    public PluginCommand getPluginCommand(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void savePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void configureDbConfig(ServerConfig config) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean addRecipe(Recipe recipe) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Iterator<Recipe> recipeIterator() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void clearRecipes() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void resetRecipes() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Map<String, String[]> getCommandAliases() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getSpawnRadius() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setSpawnRadius(int value) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean getOnlineMode() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean getAllowFlight() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean isHardcore() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int broadcast(String message, String permission) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public OfflinePlayer getOfflinePlayer(UUID id) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<String> getIPBans() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void banIP(String address) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void unbanIP(String address) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public BanList getBanList(BanList.Type type) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<OfflinePlayer> getOperators() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public GameMode getDefaultGameMode() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setDefaultGameMode(GameMode mode) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ConsoleCommandSender getConsoleSender() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public File getWorldContainer() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Messenger getMessenger() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public HelpMap getHelpMap() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    

    @Override
    @Implemented
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        return new CraftInventoryCustom(owner, type);
    }
    
    @Override
    @Implemented
    public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
        return new CraftInventoryCustom(owner, type, title);
    }
    
    @Override
    @Implemented
    public Inventory createInventory(InventoryHolder owner, int size) {
        return new CraftInventoryCustom(owner, size);
    }
    
    @Override
    @Implemented
    public Inventory createInventory(InventoryHolder owner, int size, String title) {
        return new CraftInventoryCustom(owner, size, title);
    }

    @Override
    public Merchant createMerchant(String title) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getMonsterSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getAnimalSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getWaterAnimalSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getAmbientSpawnLimit() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public boolean isPrimaryThread() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getMotd() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String getShutdownMessage() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Warning.WarningState getWarningState() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
    @Override
    @Implemented
    public ItemFactory getItemFactory() {
        return CraftItemFactory.instance();
    }

    @Override
    public ScoreboardManager getScoreboardManager() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public CachedServerIcon getServerIcon() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void setIdleTimeout(int threshold) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public int getIdleTimeout() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public ChunkGenerator.ChunkData createChunkData(World world) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public BossBar createBossBar(String title, BarColor color, BarStyle style, BarFlag... flags) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Entity getEntity(UUID uuid) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public UnsafeValues getUnsafe() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Spigot spigot() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
