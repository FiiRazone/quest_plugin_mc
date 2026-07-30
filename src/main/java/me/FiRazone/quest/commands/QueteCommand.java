package me.FiRazone.quest.commands;

import me.FiRazone.quest.manager.QuestManager;
import me.FiRazone.quest.menus.QueteMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Cette classe gère ce qui se passe quand un joueur tape la commande "/quetes"
// Elle doit "implements CommandExecutor" pour être reconnue par Bukkit comme
// une classe capable d'exécuter une commande
public class QueteCommand implements CommandExecutor {

    // On stocke une référence vers le menu des quêtes.
    // "private" = seule cette classe peut y accéder directement
    // "final" = une fois assignée dans le constructeur, cette variable ne peut plus changer
    private final QueteMenu queteMenu;
    private final QuestManager questManager;


    // Le constructeur : appelé quand on fait "new QueteCommand(queteMenu)"
    // Il reçoit une instance de QueteMenu déjà créée ailleurs (dans Quest.java)
    // et la stocke dans le champ ci-dessus, pour pouvoir s'en servir plus tard
    public QueteCommand(QueteMenu queteMenu, QuestManager questManager) {
        this.queteMenu = queteMenu;
        this.questManager = questManager;
    }

    // Cette méthode est appelée automatiquement par Bukkit à chaque fois
    // qu'un joueur (ou la console) tape la commande "/quetes"
    //
    // Paramètres :
    // - sender  : celui qui a tapé la commande (peut être un joueur OU la console)
    // - command : représente la commande elle-même
    // - label   : le nom exact tapé (utile si la commande a des alias)
    // - args    : les arguments après la commande, ex "/quetes reset" -> args[0] = "reset"
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande réservée aux joueurs.");
            return true;
        }

        // Pas d'argument -> on ouvre le menu par défaut
        if (args.length == 0) {
            queteMenu.openMainMenu(player);
            return true;
        }

        // On regarde le premier argument pour choisir le comportement
        switch (args[0].toLowerCase()) {
            case "reset" -> {
                // ex: /quetes reset
                if (!player.hasPermission("quest.admin")) {
                    player.sendMessage("§cTu n'as pas la permission d'utiliser cette commande.");
                    return true;
                }

                player.sendMessage("§cTes quêtes ont été réinitialisées.");
                questManager.resetAll(player.getUniqueId());
            }
            default -> {
                player.sendMessage("§cCommande inconnue. Utilise /quetes.");
            }
        }

        return true;
    }
}