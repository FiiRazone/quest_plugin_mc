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

    public QuestConfigLoader(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public List<MiningQuest> loadMiningQuests() {

        List<MiningQuest> quetes = new ArrayList<>();

        FileConfiguration config = plugin.getConfig();
        ConfigurationSection section = config.getConfigurationSection("mining-quests");

        if (section == null) {
            plugin.getLogger().warning("Aucune section 'mining-quests' trouvée dans config.yml");
            return quetes;
        }

        // Chaque clé de la section (ex: "diamant", "or", "fer") = une quête
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
                // Se déclenche si le "material" dans le yml n'existe pas (faute de frappe, etc.)
                plugin.getLogger().warning("Quête '" + nom + "' invalide dans config.yml : " + e.getMessage());
            }
        }

        return quetes;
    }
}