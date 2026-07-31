package me.FiRazone.quest.manager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FarmingQuest {

    private final String nom;
    private final Material material;
    private final int objectif;
    private final ItemStack recompense;
    private final String messageProgression;
    private final String messageTerminee;

    public FarmingQuest(String nom, Material material, int objectif, ItemStack recompense,
                       String messageProgression, String messageTerminee) {
        this.nom = nom;
        this.material = material;
        this.objectif = objectif;
        this.recompense = recompense;
        this.messageProgression = messageProgression;
        this.messageTerminee = messageTerminee;
    }

    public String getNom() { return nom; }
    public Material getMaterial() { return material; }
    public int getObjectif() { return objectif; }
    public ItemStack getRecompense() { return recompense; }

    // On remplace les placeholders %progression% et %objectif% par les vraies valeurs
    public String getMessageProgression(int progression) {
        return messageProgression
                .replace("%progression%", String.valueOf(progression))
                .replace("%objectif%", String.valueOf(objectif));
    }

    public String getMessageTerminee() {
        return messageTerminee.replace("%objectif%", String.valueOf(objectif));
    }
}