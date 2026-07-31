package me.FiRazone.quest;

import me.FiRazone.quest.commands.QueteCommand;
import me.FiRazone.quest.events.BlockBreakListener;
import me.FiRazone.quest.events.EntityKillListener;
import me.FiRazone.quest.manager.*;
import me.FiRazone.quest.menus.MenuClickListener;
import me.FiRazone.quest.menus.QueteMenu;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


// Classe principale du plugin : c'est le "point d'entrée" que Bukkit charge
// au démarrage du serveur. Elle DOIT hériter de JavaPlugin (extends JavaPlugin)
// pour être reconnue comme un plugin valide.
// "final" = cette classe ne peut pas être héritée par une autre classe
public final class Quest extends JavaPlugin {

    // onEnable() est appelée automatiquement par Bukkit quand le plugin démarre
    // (au lancement du serveur, ou avec /reload)
    @Override
    public void onEnable() {

        saveDefaultConfig();

        QuestConfigLoader configLoader = new QuestConfigLoader(this);
        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        QuestManager questManager = new QuestManager(playerDataManager);

        // On crée ICI, au niveau de la classe (donc dès la création de l'objet Quest,
        // avant même onEnable()), UNE SEULE instance de BlockBreakListener.
        // C'est cette instance UNIQUE qui sera utilisée PARTOUT dans le plugin,
        // pour éviter le bug qu'on a corrigé (deux Map différentes = incohérence)
        BlockBreakListener blockBreakListener = new BlockBreakListener(
                questManager,
                configLoader.getMiningQuests(),
                configLoader.getFarmingQuests()
        );
        EntityKillListener entityKillListener = new EntityKillListener(questManager);

        // On crée aussi ICI l'instance de QueteMenu, en lui passant directement
        // la même instance "blockBreakListener" créée juste au-dessus.
        // Comme ça, QueteMenu et le futur registerEvents() partageront
        // exactement la même Map de progression.
        QueteMenu queteMenu = new QueteMenu(
                questManager,
                configLoader.getMiningQuests(),
                configLoader.getFarmingQuests()
        );
        getLogger().info(configLoader.getMiningQuests().size() + " quêtes de minage chargées.");
        getLogger().info(configLoader.getFarmingQuests().size() + " quêtes de farming chargées.");

        // MESSAGE DANS LA CONSOLE POUR DIRE QUE LE PLUGIN A CHARGER
        // getLogger() = le logger officiel du plugin, affiche un message
        // dans la console du serveur avec le préfixe du plugin
        getLogger().info("Le plugin est chargerr");


        //CHARGER LES COMMANDES
        // getCommand("quetes") récupère la commande "quetes" déclarée
        // dans le fichier plugin.yml
        // .setExecutor(...) associe cette commande à une classe qui va
        // la gérer : ici, un NOUVEL objet QueteCommand, à qui on passe
        // notre instance unique "queteMenu" (créée plus haut)
        Objects.requireNonNull(getCommand("quetes")).setExecutor(new QueteCommand(queteMenu, questManager));

        //CHARGER LES EVENTS
        // On enregistre "blockBreakListener" (notre instance unique, PAS un "new")
        // auprès du PluginManager de Bukkit, pour qu'il commence à écouter
        // les événements (BlockBreakEvent) et appelle onBreak() automatiquement
        // "this" = référence vers le plugin actuel (obligatoire pour l'enregistrement)
        getServer().getPluginManager().registerEvents(blockBreakListener, this);
        getServer().getPluginManager().registerEvents(entityKillListener, this);
        getServer().getPluginManager().registerEvents(new MenuClickListener(queteMenu), this);

    }

    // onDisable() est appelée automatiquement par Bukkit quand le plugin s'arrête
    // (arrêt du serveur, ou /reload)
    @Override
    public void onDisable() {

        // MESSAGE DANS LA CONSOLE POUR DIRE QUE LE PLUGIN A DECHARGER
        getLogger().info("Le plugin est décharger");

    }
}