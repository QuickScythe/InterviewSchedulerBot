package me.quickscythe.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class DiscordCommand extends ListenerAdapter {

    String label;

    public DiscordCommand(Guild guild, String label, String desc, OptionData... options) {
        this.label = label;
        guild.upsertCommand(Commands.slash(label, desc).addOptions(options).setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))).queue();
//        guild.updateCommands().addCommands(Commands.slash(label, desc).addOptions(options)).queue();
    }

    @Override
    public abstract void onSlashCommandInteraction(SlashCommandInteractionEvent event);


    public String label() {
        return label;

    }
}

