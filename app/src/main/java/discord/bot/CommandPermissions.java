package discord.bot;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;

public class CommandPermissions {
    
    private static final String userRoleName = "bot-user";
    private static final String moderationRoleName = "bot-moderation";
    public static enum Permission {
        NONE,
        USER,
        MODERATION
    }

    public static Permission getMemberPermission(Guild guild, Member member) {
        var userRoles = guild.getRolesByName(userRoleName, true);
        Role userRole = userRoles.size() > 0 ? userRoles.get(0) : null;
        
        var modRoles = guild.getRolesByName(moderationRoleName, true);
        Role modRole = modRoles.size() > 0 ? modRoles.get(0) : null;

        if (modRole != null && member.getRoles().contains(modRole)) return Permission.MODERATION;
        if (userRole != null && member.getRoles().contains(userRole)) return Permission.USER;
        return Permission.NONE;
    }

    public static boolean hasPermission(Guild guild, Member member, Permission permission) {
        Permission memberPermission = getMemberPermission(guild, member);
        return switch(permission) {
            case MODERATION -> memberPermission == Permission.MODERATION;
            case USER -> memberPermission == Permission.MODERATION || memberPermission == Permission.USER;
            case NONE -> true;
        };
    }

    public static void sendLackOfAccessMsg(MessageChannel channel) {
        channel.sendMessage("Nie masz uprawnien do uzycia tej funkcji").queue();
    }
}
