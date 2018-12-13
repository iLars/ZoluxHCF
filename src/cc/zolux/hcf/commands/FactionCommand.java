package cc.zolux.hcf.commands;

import cc.zolux.hcf.HCF;
import cc.zolux.hcf.faction.DTRChangeReason;
import cc.zolux.hcf.faction.Faction;
import cc.zolux.hcf.faction.FactionPlayer;
import cc.zolux.hcf.faction.FactionRole;
import cc.zolux.hcf.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class FactionCommand implements CommandExecutor {

    private static HashMap<String, String> SUBS = new HashMap<>();

    static {
        SUBS.put("create", "Create a faction");
        SUBS.put("join", "Join a faction");
        SUBS.put("leave", "Leave a faction");
        SUBS.put("who", "Shows info about a faction");
        SUBS.put("show", "Shows info about a faction");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FactionPlayer fp = HCF.getPlugin().factionManager.getPlayer(player.getUniqueId());
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                player.sendMessage(Color.c("&9&lHCF &7- &bHelp"));
                for (String s : SUBS.keySet()) {
                    player.sendMessage(Color.c("&e/" + label + " " + s + " &7- " + SUBS.get(s)));
                }
            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (fp.getFaction() != null) {
                        player.sendMessage(Color.c("&7You're already in a faction."));
                    } else {
                        if (args.length == 1) {
                            player.sendMessage(Color.c("&7You need to specify a name."));
                        } else {
                            if (args.length == 2) {
                                if (HCF.getPlugin().factionManager.getFaction(args[1]) != null) {
                                    player.sendMessage(Color.c("&7That name is already taken."));
                                    return true;
                                }
                                Faction f = new Faction(args[1], "Just a faction...");
                                fp.setFaction(f);
                                f.setCurrentDTR(f.getMaxDTR());
                                f.setRegening(false);
                                f.setDtrChangeReason(DTRChangeReason.NO_CHANGE);
                                HCF.getPlugin().factionManager.addFaction(f);
                                HCF.getPlugin().factionManager.insertFaction(f);
                                String msg = "&7Faction &b" + args[1] + " &7was created by " + fp.getName();
                                for (Player pl : Bukkit.getOnlinePlayers()) {
                                    if (pl != player)
                                        pl.sendMessage(Color.c(msg));
                                    player.sendMessage(Color.c("&7You created faction " + f.getName()));
                                    fp.setRole(FactionRole.LEADER);
                                }
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("who") && args.length == 1) {
                    Faction faction = fp.getFaction();
                    if(args.length != 1)
                        return false;
                    if (faction != null) {
                        player.sendMessage(Color.c("&7&m--------------------------------------------"));
                        player.sendMessage(Color.c("&9&l" + faction.getName() + " &7| Home " + (faction.getHome() != null ? faction.getHome().getX() + ", " + faction.getHome().getY() : "Not set.")));
                        player.sendMessage(Color.c("&eDTR: " + faction.getCurrentDTR()));
                        ArrayList<FactionPlayer> members = new ArrayList<>();
                        for (FactionPlayer fplayer : HCF.getPlugin().factionManager.PLAYERS) {
                            if (fplayer.getFaction() != null && fplayer.getFaction() == faction || fplayer.getFactionName().equals(faction.getName())) {
                                members.add(fplayer);
                            }
                        }
                        String member = "";
                        String moderators = "";
                        for (FactionPlayer factionPlayer : members) {
                            if (factionPlayer.getRole() == FactionRole.LEADER) {
                                player.sendMessage(Color.c("&eLeader: " + (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "&a" + factionPlayer.getName() : "&7" + factionPlayer.getName())));
                            }
                            if (factionPlayer == fp) continue;
                            if (factionPlayer.getRole() == FactionRole.MOD) {
                                moderators += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                            } else if (factionPlayer.getRole() == FactionRole.MEMBER) {
                                member += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                            }
                        }
                        if (member == "")
                            if (moderators != "")
                                player.sendMessage(Color.c("&eModerators: " + moderators));
                        if (moderators == "")
                            if (member != "")
                                player.sendMessage(Color.c("&eMembers: " + member));
                        player.sendMessage(Color.c("&7&m--------------------------------------------"));
                    } else {
                        player.sendMessage(Color.c("&7You're not in a faction."));
                    }
                } else if (args[0].equalsIgnoreCase("show") || args[0].equalsIgnoreCase("who") && args.length == 2) {
                    Faction faction = HCF.getPlugin().factionManager.getFaction(args[1]);
                    FactionPlayer target = HCF.getPlugin().factionManager.getPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
                    if (faction != null) {
                        player.sendMessage(Color.c("&7&m--------------------------------------------"));
                        player.sendMessage(Color.c("&9&l" + faction.getName() + " &7| Home ")); //todo: add f home
                        player.sendMessage(Color.c("&eDTR: " + faction.getCurrentDTR())); //todo: add dtr
                        ArrayList<FactionPlayer> members = new ArrayList<>();
                        for (FactionPlayer fplayer : HCF.getPlugin().factionManager.PLAYERS) {
                            if (fplayer.getFaction() != null && fplayer.getFaction() == faction || fplayer.getFactionName().equals(faction.getName())) {
                                members.add(fplayer);
                            }
                        }
                        String member = "";
                        String moderators = "";
                        for (FactionPlayer factionPlayer : members) {
                            if (factionPlayer.getRole() == FactionRole.LEADER) {
                                player.sendMessage(Color.c("&eLeader: " + (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "&a" + factionPlayer.getName() : "&7" + factionPlayer.getName())));
                            }
                            if (factionPlayer == fp) continue;
                            if (factionPlayer.getRole() == FactionRole.MOD) {
                                moderators += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                            } else if (factionPlayer.getRole() == FactionRole.MEMBER) {
                                member += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                            }
                        }
                        if (member == "")
                            if (moderators != "")
                                player.sendMessage(Color.c("&eModerators: " + moderators));
                        if (moderators == "")
                            if (member != "")
                                player.sendMessage(Color.c("&eMembers: " + member));
                        player.sendMessage(Color.c("&7&m--------------------------------------------"));
                    } else if (faction == null && target != null){
                        faction = target.getFaction();
                        if(faction != null) {
                            player.sendMessage(Color.c("&7&m--------------------------------------------"));
                            player.sendMessage(Color.c("&9&l" + faction.getName() + " &7| Home ")); //todo: add f home
                            player.sendMessage(Color.c("&eDTR: ")); //todo: add dtr
                            ArrayList<FactionPlayer> members = new ArrayList<>();
                            for (FactionPlayer fplayer : HCF.getPlugin().factionManager.PLAYERS) {
                                if (fplayer.getFaction() != null && fplayer.getFaction() == faction || fplayer.getFactionName().equals(faction.getName())) {
                                    members.add(fplayer);
                                }
                            }
                            String member = "";
                            String moderators = "";
                            for (FactionPlayer factionPlayer : members) {
                                if (factionPlayer.getRole() == FactionRole.LEADER) {
                                    player.sendMessage(Color.c("&eLeader: " + (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "&a" + factionPlayer.getName() : "&7" + factionPlayer.getName())));
                                }
                                if (factionPlayer == fp) continue;
                                if (factionPlayer.getRole() == FactionRole.MOD) {
                                    moderators += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                                } else if (factionPlayer.getRole() == FactionRole.MEMBER) {
                                    member += (Bukkit.getPlayer(factionPlayer.getName()).isOnline() ? "§a" + factionPlayer.getName() : "§7" + factionPlayer.getName() + ", ");
                                }
                            }
                            if (member == "")
                                if (moderators != "")
                                    player.sendMessage(Color.c("&eModerators: " + moderators));
                            if (moderators == "")
                                if (member != "")
                                    player.sendMessage(Color.c("&eMembers: " + member));
                            player.sendMessage(Color.c("&7&m--------------------------------------------"));
                        } else {
                            player.sendMessage(Color.c("&7That faction does not exist."));
                        }
                    } else {
                        player.sendMessage(Color.c("&7That faction does not exist."));
                    }
                }
            }
        }
        return false;
    }
}
