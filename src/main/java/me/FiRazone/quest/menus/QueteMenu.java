package me.FiRazone.quest.menus;

import me.FiRazone.quest.manager.MiningQuest;
import me.FiRazone.quest.manager.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class QueteMenu {

    private final QuestManager questManager;
    private final List<MiningQuest> miningQuests;

    public QueteMenu(QuestManager questManager, List<MiningQuest> miningQuests) {
        this.questManager = questManager;
        this.miningQuests = miningQuests;
    }

    // --- MENU PRINCIPAL (Choix de la catégorie) ---
    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Catégories de quêtes");

        // Bouton Minage
        ItemStack miningItem = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta miningMeta = miningItem.getItemMeta();
        if (miningMeta != null) {
            miningMeta.setDisplayName(ChatColor.GOLD + "⛏ Quêtes de Minage");
            List<String> lore = new ArrayList<>();
            lore.add("§7Cliquez pour voir les quêtes");
            lore.add("§7de minage disponibles.");
            miningMeta.setLore(lore);
            miningItem.setItemMeta(miningMeta);
        }

        // Bouton Farming
        ItemStack farmingItem = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta farmingMeta = farmingItem.getItemMeta();
        if (farmingMeta != null) {
            farmingMeta.setDisplayName(ChatColor.GREEN + "🌾 Quêtes de Farming");
            List<String> lore = new ArrayList<>();
            lore.add("§7Cliquez pour voir les quêtes");
            lore.add("§7de farming disponibles.");
            farmingMeta.setLore(lore);
            farmingItem.setItemMeta(farmingMeta);
        }

        // Placement dans l'inventaire
        inv.setItem(11, miningItem);
        inv.setItem(15, farmingItem);

        player.openInventory(inv);
    }

    // --- SOUS-MENU MINAGE ---
    public void openMiningMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Quêtes : Minage");

        int slot = 10;
        for (MiningQuest quete : miningQuests) {
            ItemStack item = new ItemStack(quete.getMaterial());
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + "Quête : " + quete.getNom());

                int progression = questManager.getProgression(player.getUniqueId(), quete.getNom());
                boolean isTerminee = questManager.isTerminee(player.getUniqueId(), quete.getNom());

                List<String> lore = new ArrayList<>();
                lore.add("");
                lore.add("§7Objectif : Miner " + quete.getObjectif() + " " + quete.getNom());
                lore.add("");

                if (isTerminee) {
                    lore.add("§a✔ Quête terminée !");
                } else {
                    lore.add("§eProgression : §f" + progression + "/" + quete.getObjectif());
                }

                lore.add("");
                lore.add("§aRécompense : §f" + quete.getRecompense().getAmount() + "x " + quete.getRecompense().getType());

                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            inv.setItem(slot, item);
            slot += 2;
            if (slot >= 27) break;
        }

        // Ajout du bouton de retour au slot 18 (en bas à gauche)
        addBackButton(inv);

        player.openInventory(inv);
    }

    // --- SOUS-MENU FARMING ---
    public void openFarmingMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Quêtes : Farming");

        ItemStack sampleCrop = new ItemStack(Material.WHEAT);
        ItemMeta meta = sampleCrop.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.YELLOW + "Quête Blé");
            sampleCrop.setItemMeta(meta);
        }
        inv.setItem(13, sampleCrop);

        // Ajout du bouton de retour au slot 18 (en bas à gauche)
        addBackButton(inv);

        player.openInventory(inv);
    }

    // Méthode privée pour créer et ajouter le bouton retour au slot 18 (coin bas-gauche)
    private void addBackButton(Inventory inv) {
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta meta = back.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.RED + "⬅ Retour au menu principal");
            back.setItemMeta(meta);
        }
        inv.setItem(18, back); // Slot 18 = Première case de la 3ème ligne
    }
}