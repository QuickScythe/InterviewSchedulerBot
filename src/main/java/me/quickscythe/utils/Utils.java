/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package me.quickscythe.utils;

import me.quickscythe.logger.LoggerUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Utils {

    private static long interviewAcceptedRole;
    private static long interviewAcceptedChannel;
    private static long interviewerRole;

    private static JSONObject config;
    private static JDA jda;

    public static void initializeConfig() {
        StringBuilder stringBuilder = loadConfig();
        String config = stringBuilder.toString();
        Utils.config = config.isEmpty() ? new JSONObject() : new JSONObject(config);
        checkDefaults();
        initializeVariables();
    }

    private static void initializeVariables() {
        interviewAcceptedRole = config.getLong("interview_accepted_role_id");
        interviewAcceptedChannel = config.getLong("interview_accepted_announcement_channel_id");
        interviewerRole = config.getLong("interviewer_role_id");
    }

    public static Role INTERVIEW_ACCEPTED_ROLE(Guild guild){
        return guild.getRoleById(interviewAcceptedRole);
    }

    public TextChannel INTERVIEW_ACCEPTED_CHANNEL(Guild guild){
        return guild.getTextChannelById(interviewAcceptedChannel);
    }

    public static Role INTERVIEWER_ROLE(Guild guild){
        return guild.getRoleById(interviewerRole);
    }

    @NotNull
    private static StringBuilder loadConfig() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File config = new File("config.json");
            if (!config.exists()) if (config.createNewFile()) {
                LoggerUtils.error("Config file generated.", "=");
            }
            BufferedReader reader = new BufferedReader(new FileReader("config.json"));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();


        } catch (IOException ex) {
            LoggerUtils.error("Config", "Config File couldn't be generated or accessed. Please check console for more details.", ex);
        }
        return stringBuilder;
    }

    private static void checkDefaults() {
        boolean save = false;
        if (!config.has("token")) {
            config.put("token", "<token here>");
            save = true;
        }
        if (!config.has("interview_accepted_role_id")) {
            config.put("interview_accepted_role_id", 0L);
            save = true;
        }
        if (!config().has("interview_accepted_announcement_channel_id")) {
            config.put("interview_accepted_announcement_channel_id", 0L);
            save = true;
        }
        if (!config().has("interviewer_role_id")) {
            config.put("interviewer_role_id", 0L);
            save = true;
        }
        if (save) {
            saveConfig();
            throw new RuntimeException("Config file updated with default values. Please edit the config file and restart the bot.");
        }
    }

    public static void initializeJda(JDA api) {
        Utils.jda = api;
    }

    public static JDA jda() {
        return jda;
    }

    public static JSONObject config() {
        return config;
    }

    public static void saveConfig() {
        try {
            java.nio.file.Files.write(new File("config.json").toPath(), config.toString(2).getBytes());
            LoggerUtils.log("Config", "Config file saved.");
        } catch (IOException e) {
            LoggerUtils.error("Config", "Failed to save config file", e);
        }
    }
}
