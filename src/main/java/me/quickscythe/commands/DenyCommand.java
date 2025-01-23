package me.quickscythe.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DenyCommand extends InterviewSubcommand {

    public DenyCommand(Guild guild, String label, String desc, OptionData... options) {
        super(guild, label, desc, options);
    }

    @Override
    public void run(TextChannel channel, User interviewee) {

    }
}
