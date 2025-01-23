package me.quickscythe.timer;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ChannelTimer {

    private final ScheduledExecutorService check = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService process = Executors.newScheduledThreadPool(1);
    private final BlockingQueue<List<Long>> deleteQueue = new LinkedBlockingQueue<>();


    public void start(Guild guild) {
        check.scheduleAtFixedRate(() -> {
            // Check dates and create a list of items to be deleted
            List<Long> itemsToDelete = check(guild);
            try {
                // Queue the list of items to be deleted
                deleteQueue.put(itemsToDelete);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, 0, 1, TimeUnit.HOURS);
        process(guild);
    }

    public void process(Guild guild) {
        process.scheduleAtFixedRate(() -> {
            try {
                List<Long> itemsToDelete = deleteQueue.take();
                for(Long item : itemsToDelete){
                    guild.getChannelById(TextChannel.class, item).delete().queue();
                    System.out.println("Deleting item: " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

            }
        }, 0, 1, TimeUnit.HOURS);


    }

    private List<Long> check(Guild guild) {
        List<Long> channelsToDelete = new ArrayList<>();
        for(Category category : guild.getCategories()){
            if(category.getName().equalsIgnoreCase("Interviews")){
                for(TextChannel channel : category.getTextChannels()){
                    if(channel.getName().startsWith("interview-")){
                        long creationEpoch = channel.getTimeCreated().toInstant().toEpochMilli();
                        long currentEpoch = Instant.now().toEpochMilli();

                        long diff = currentEpoch - creationEpoch;
                        long check = TimeUnit.MILLISECONDS.convert(72, TimeUnit.HOURS);
                        if(diff > check){
                            channelsToDelete.add(channel.getIdLong());
                        }
                    }
                }
            }
        }
        return channelsToDelete;
    }

    private void delete(List<String> itemsToDelete) {
        // Implement your logic to delete the items
        for (String item : itemsToDelete) {
            System.out.println("Deleting item: " + item);
        }
    }
}