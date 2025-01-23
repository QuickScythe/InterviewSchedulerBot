package me.quickscythe.commands;

import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InterviewCommand extends DiscordCommand {
    public InterviewCommand(Guild guild, String label, String desc, OptionData... options) {
        super(guild, label, desc, options);
    }

    // /interview <user>

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals(label())) {
            User user = event.getOption("user").getAsUser();
            Guild guild = event.getGuild();
            Category category = null;
            if(!guildContainsInterviewCategory(guild)) {
                category = guild.createCategory("Interviews").complete();
            } else {
                for(Category c : guild.getCategories()) {
                    if(c.getName().equalsIgnoreCase("Interviews")) {
                        category = c;
                        break;
                    }
                }
            }
            TextChannel channel = guild.createTextChannel("Interview-" + user.getName(), category).complete();
            Role everyoneRole = channel.getGuild().getPublicRole();
            channel.upsertPermissionOverride(everyoneRole).deny(Permission.VIEW_CHANNEL).queue();

            // Grant VIEW_CHANNEL permission to allowed members
            for (Member member : guild.getMembers()) {
                if(member.getRoles().contains(Utils.INTERVIEWER_ROLE(guild))) {
                    channel.upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL).queue();
                }
            }
            channel.upsertPermissionOverride(guild.getMember(user)).grant(Permission.VIEW_CHANNEL).queue();

            channel.sendMessage("Interview with " + user.getAsMention()).queue();
            event.reply("Interview started with " + user.getAsMention()).setEphemeral(true).queue();

        }
    }

    private boolean guildContainsInterviewCategory(Guild guild) {
        for(Category category : guild.getCategories()) {
            if(category.getName().equalsIgnoreCase("Interviews")) {
                return true;
            }
        }
        return false;
    }
}
