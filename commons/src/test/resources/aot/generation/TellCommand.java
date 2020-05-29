package aot.generation;

import org.bukkit.command.CommandSender;

import com.karuslabs.commons.command.types.PlayersType;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;


public class TellCommand {
    
    public final PlayersType a = new PlayersType();
    public static final SuggestionProvider<CommandSender> suggestions = null;
    
    
    public static int b(CommandContext<CommandSender> context) {
        context.getSource().sendMessage("Hello darkness my old friend");
        return 1;
    }
    
}
