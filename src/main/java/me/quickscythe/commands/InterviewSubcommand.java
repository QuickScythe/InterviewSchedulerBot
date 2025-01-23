package me.quickscythe.commands;

import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public abstract class InterviewSubcommand extends DiscordCommand {
    public InterviewSubcommand(Guild guild, String label, String desc, OptionData... options) {
        super(guild, label, desc, options);
    }

    public abstract void run(TextChannel channel, User interviewee);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (label().equalsIgnoreCase(event.getName())) {
            if (event.getChannel().asTextChannel().getParentCategory() != null && event.getChannel().asTextChannel().getParentCategory().getName().equalsIgnoreCase("Interviews")) {
                event.reply(label() + "!").queue();
                event.getChannel().asTextChannel().getMembers().forEach(member -> {
                    if(!member.getRoles().contains(Utils.INTERVIEWER_ROLE(event.getGuild()))){
                        if(member.getUser().isBot()) return;
                        run(event.getChannel().asTextChannel(), member.getUser());
                    }
                });



            } else event.reply("This command can only be used in the Interviews category!").queue();
        }
    }
}
