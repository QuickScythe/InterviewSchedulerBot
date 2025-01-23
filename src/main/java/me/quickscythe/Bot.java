package me.quickscythe;

import me.quickscythe.commands.AcceptCommand;
import me.quickscythe.commands.DenyCommand;
import me.quickscythe.commands.InterviewCommand;
import me.quickscythe.timer.ChannelTimer;
import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.HashMap;
import java.util.Map;

public class Bot {


    private static final Map<Guild, ChannelTimer> channelTimers = new HashMap<>();


    public static void main(String[] args) throws InterruptedException {
        Utils.initializeConfig();
        JDA api = JDABuilder.createDefault(Utils.config().getString("token"), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        api.awaitReady();
        Utils.initializeJda(api);
        for (Guild guild : api.getGuilds()) {
            api.addEventListener(new InterviewCommand(guild, "interview", "Start the interview process",
                    new OptionData(
                            OptionType.USER,
                            "user",
                            "User to start the interview with",
                            true)));
            api.addEventListener(new AcceptCommand(guild, "accept", "Accept a user into the guild"));
            api.addEventListener(new DenyCommand(guild, "deny", "Deny an interviewee"));
            ChannelTimer timer = new ChannelTimer();
            timer.start(guild);
            channelTimers.put(guild, timer);
        }
    }

    public static ChannelTimer timer(Guild guild) {
        return channelTimers.get(guild);
    }

    public static Map<Guild, ChannelTimer> timers() {
        return channelTimers;
    }
}