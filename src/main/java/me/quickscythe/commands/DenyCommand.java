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
        channel.sendMessage("Hello again " + interviewee.getAsMention() + ". We regret to inform you that you did not pass your interview. This is always a bummer, but you'll be able to apply again after the upcoming event.");
    }
}
