package me.FiRazone.quest.events;

import me.FiRazone.quest.manager.MiningQuest;
import me.FiRazone.quest.manager.QuestManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {

    private final QuestManager questManager;
    private final List<MiningQuest> miningQuests;

    public BlockBreakListener(QuestManager questManager, List<MiningQuest> miningQuests) {
        this.questManager = questManager;
        this.miningQuests = miningQuests;
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
    }
}