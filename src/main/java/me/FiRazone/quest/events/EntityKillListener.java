package me.FiRazone.quest.events;

import me.FiRazone.quest.manager.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityKillListener implements Listener {

    private final QuestManager questManager;

    public EntityKillListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {

        if (event.getEntityType() != EntityType.ZOMBIE) {
            return;
        }

        Player killer = event.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        // Si la quête est déjà terminée, on ne fait rien de plus
        if (questManager.isTerminee(killer.getUniqueId(), "zombie")) {
            return;
        }

        int current = questManager.getProgression(killer.getUniqueId(), "zombie");
        int nouvelleValeur = current + 1;

        questManager.setProgression(killer.getUniqueId(), "zombie", nouvelleValeur);

        if (nouvelleValeur >= 20) {
            questManager.setTerminee(killer.getUniqueId(), "zombie", true);
            killer.sendMessage("§6Quête terminée ! Tu as tué 20 zombies !");
            killer.getInventory().addItem(new ItemStack(Material.IRON_SWORD, 1));
        } else {
            killer.sendMessage("§aTu as tué " + nouvelleValeur + " zombies");
        }
    }
}