package aot.generation;

import com.karuslabs.commons.command.tree.nodes.*;

import com.mojang.brigadier.tree.CommandNode;

import java.util.*;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;


/**
 * This file was generated at test-time using Chimera 4.9.0-SNAPSHOT
 */
public class Commands {

    private static final Predicate<CommandSender> REQUIREMENT = s -> true;

    public static Map<String, CommandNode<CommandSender>> of(aot.generation.TellCommand source) {
        var commands = new HashMap<String, CommandNode<CommandSender>>();

        var command = new Argument<>("players", source.a, null, REQUIREMENT, aot.generation.TellCommand.suggestions);

        var command0 = new Literal<>("tell", aot.generation.TellCommand::b, REQUIREMENT);
        Literal.alias(command0, "t");
        command0.addChild(command);

        commands.put(command0.getName(), command0);
        return commands;
    }

}
