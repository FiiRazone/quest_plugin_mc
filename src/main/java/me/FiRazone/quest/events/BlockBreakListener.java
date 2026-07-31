package me.FiRazone.quest.events;

import me.FiRazone.quest.manager.FarmingQuest;
import me.FiRazone.quest.manager.MiningQuest;
import me.FiRazone.quest.manager.QuestManager;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {

    private final QuestManager questManager;
    private final List<MiningQuest> miningQuests;
    private final List<FarmingQuest> farmingQuests;

    public BlockBreakListener(QuestManager questManager, List<MiningQuest> miningQuests, List<FarmingQuest> farmingQuests) {
        this.questManager = questManager;
        this.miningQuests = miningQuests;
        this.farmingQuests = farmingQuests;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (event.isCancelled()) return;

        for (MiningQuest quete : miningQuests) {

            if (event.getBlock().getType() != quete.getMaterial()) continue;

            var uuid = event.getPlayer().getUniqueId();
            String nom = quete.getNom();

            if (questManager.isTerminee(uuid, nom)) return;

            int nouvelleValeur = questManager.getProgression(uuid, nom) + 1;
            questManager.setProgression(uuid, nom, nouvelleValeur);

            if (nouvelleValeur >= quete.getObjectif()) {
                questManager.setTerminee(uuid, nom, true);
                event.getPlayer().sendMessage(quete.getMessageTerminee());
                event.getPlayer().getInventory().addItem(quete.getRecompense());
            } else {
                event.getPlayer().sendMessage(quete.getMessageProgression(nouvelleValeur));
            }

            return;
        }

        for (FarmingQuest quete : farmingQuests) {

            if (event.getBlock().getType() != quete.getMaterial()) continue;

            Material blockType = event.getBlock().getType();

            event.getPlayer().sendMessage("block type : " + blockType);

            // Si le bloc possède une croissance (Blé, Carottes, Patates, Verrues du Nether...)
            if (event.getBlock().getBlockData() instanceof Ageable ageable) {

                event.getPlayer().sendMessage("ageable  : " + ageable);

                // Si la plante N'EST PAS à son âge maximum, on ignore l'événement !
                if (ageable.getAge() < ageable.getMaximumAge()) {
                    continue;
                }
            }

            var uuid = event.getPlayer().getUniqueId();
            String nom = quete.getNom();
            if (questManager.isTerminee(uuid, nom)) continue;

            int nouvelleValeur = questManager.getProgression(uuid, nom) + 1;
            questManager.setProgression(uuid, nom, nouvelleValeur);

            if (nouvelleValeur >= quete.getObjectif()) {
                questManager.setTerminee(uuid, nom, true);
                event.getPlayer().sendMessage(quete.getMessageTerminee());
                event.getPlayer().getInventory().addItem(quete.getRecompense());
            } else {
                event.getPlayer().sendMessage(quete.getMessageProgression(nouvelleValeur));
            }

            return;
        }
    }
}