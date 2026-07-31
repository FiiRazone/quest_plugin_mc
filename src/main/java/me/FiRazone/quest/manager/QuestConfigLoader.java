package me.FiRazone.quest.manager;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class QuestConfigLoader {

    private final JavaPlugin plugin;
    private final List<MiningQuest> miningQuests = new ArrayList<>();
    private final List<FarmingQuest> farmingQuests = new ArrayList<>();

    public QuestConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
        reloadAllQuests(); // Charge les quêtes dès l'instanciation
    }

    /**
     * Recharge le fichier config.yml et met à jour les listes en mémoire.
     */
    public void reloadAllQuests() {
        plugin.reloadConfig();

        miningQuests.clear();
        miningQuests.addAll(loadMiningQuests());

        farmingQuests.clear();
        farmingQuests.addAll(loadFarmingQuests());

        plugin.getLogger().info("Quêtes chargées : " + miningQuests.size() + " minage, " + farmingQuests.size() + " farming.");
    }

    public List<MiningQuest> getMiningQuests() {
        return miningQuests;
    }

    public List<FarmingQuest> getFarmingQuests() {
        return farmingQuests;
    }

    private List<MiningQuest> loadMiningQuests() {

        List<MiningQuest> quetes = new ArrayList<>();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("mining-quests");

        if (section == null) {
            plugin.getLogger().warning("Aucune section 'mining-quests' trouvée dans config.yml");
            return quetes;
        }

        for (String nom : section.getKeys(false)) {

            ConfigurationSection questSection = section.getConfigurationSection(nom);

            if (questSection == null) continue;

            try {
                Material material = Material.valueOf(questSection.getString("material", "").toUpperCase());
                int objectif = questSection.getInt("objectif", 1);

                Material recompenseMaterial = Material.valueOf(
                        questSection.getString("recompense.material", "STONE").toUpperCase()
                );
                int recompenseAmount = questSection.getInt("recompense.amount", 1);
                ItemStack recompense = new ItemStack(recompenseMaterial, recompenseAmount);

                String messageProgression = questSection.getString("message-progression", "Progression : %progression%/%objectif%");
                String messageTerminee = questSection.getString("message-terminee", "Quête terminée !");

                quetes.add(new MiningQuest(nom, material, objectif, recompense, messageProgression, messageTerminee));

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Quête '" + nom + "' invalide dans config.yml : " + e.getMessage());
            }
        }

        return quetes;
    }

    private List<FarmingQuest> loadFarmingQuests() {

        List<FarmingQuest> quetes = new ArrayList<>();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("farming-quests");

        if (section == null) {
            plugin.getLogger().warning("Aucune section 'farming-quests' trouvée dans config.yml");
            return quetes;
        }

        for (String nom : section.getKeys(false)) {
            ConfigurationSection questSection = section.getConfigurationSection(nom);
            if (questSection == null) continue;

            try {
                Material material = Material.valueOf(questSection.getString("material", "").toUpperCase());
                int objectif = questSection.getInt("objectif", 1);

                Material recompenseMaterial = Material.valueOf(
                        questSection.getString("recompense.material", "DIRT").toUpperCase()
                );
                int recompenseAmount = questSection.getInt("recompense.amount", 1);
                ItemStack recompense = new ItemStack(recompenseMaterial, recompenseAmount);

                String messageProgression = questSection.getString("message-progression", "Progression : %progression%/%objectif%");
                String messageTerminee = questSection.getString("message-terminee", "Quête terminée !");

                quetes.add(new FarmingQuest(nom, material, objectif, recompense, messageProgression, messageTerminee));

            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Quête de farming '" + nom + "' invalide dans config.yml : " + e.getMessage());
            }
        }

        return quetes;
    }
}