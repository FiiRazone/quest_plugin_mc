package me.FiRazone.quest.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.UUID;

public class QuestManager {

    private final PlayerDataManager playerDataManager;

    public QuestManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    private String progressionPath(String quete) {
        return "quetes." + quete + ".progression";
    }

    private String termineePath(String quete) {
        return "quetes." + quete + ".terminee";
    }

    public int getProgression(UUID uuid, String quete) {
        return playerDataManager.getPlayerData(uuid).getInt(progressionPath(quete), 0);
    }

    public boolean isTerminee(UUID uuid, String quete) {
        return playerDataManager.getPlayerData(uuid).getBoolean(termineePath(quete), false);
    }

    public void resetAll(UUID uuid) {
        YamlConfiguration data = playerDataManager.getPlayerData(uuid);
        data.set("quetes", null); // "null" supprime complètement la section
        playerDataManager.savePlayerData(uuid, data);
    }

    public void setProgression(UUID uuid, String quete, int valeur) {
        YamlConfiguration data = playerDataManager.getPlayerData(uuid);
        data.set(progressionPath(quete), valeur);
        playerDataManager.savePlayerData(uuid, data);
    }

    public void setTerminee(UUID uuid, String quete, boolean valeur) {
        YamlConfiguration data = playerDataManager.getPlayerData(uuid);
        data.set(termineePath(quete), valeur);
        playerDataManager.savePlayerData(uuid, data);
    }

    public void ajouterProgression(UUID uuid, String quete, int montant) {
        YamlConfiguration data = playerDataManager.getPlayerData(uuid);
        int actuel = data.getInt(progressionPath(quete), 0);
        data.set(progressionPath(quete), actuel + montant);
        playerDataManager.savePlayerData(uuid, data);
    }
}