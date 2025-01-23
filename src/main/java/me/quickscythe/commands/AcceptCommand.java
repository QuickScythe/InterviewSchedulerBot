package me.quickscythe.commands;

import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AcceptCommand extends InterviewSubcommand {
    public AcceptCommand(Guild guild, String label, String desc, OptionData... options) {
        super(guild, label, desc, options);
    }

    @Override
    public void run(TextChannel channel, User interviewee) {
        //todo
        // - Send a message to the interviewee
        // - Send a message to the interviewers
        // - Set the interviewee's role to Squire
        interviewee.openPrivateChannel().queue((privateChannel) -> {
            privateChannel.sendMessage("Congratulations! You have been accepted into the guild!").queue();
        });
        channel.getGuild().addRoleToMember(interviewee, Utils.INTERVIEW_ACCEPTED_ROLE(channel.getGuild())).queue();
        channel.delete().queue();
    }
}
