package me.FiRazone.quest.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {

    private final QueteMenu queteMenu;

    public MenuClickListener(QueteMenu queteMenu) {
        this.queteMenu = queteMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 1. Vérifie si un inventaire valide est ouvert
        if (event.getView() == null) return;

        String title = event.getView().getTitle();

        // 2. Vérifie si l'inventaire cliqué est un de tes menus
        if (title.startsWith("§8")) {
            event.setCancelled(true); // Bloque le retrait des items

            // 3. Sécurité : vérifie que le joueur a cliqué sur un vrai item
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                return;
            }

            Player player = (Player) event.getWhoClicked();

            // Action du bouton RETOUR (Flèche)
            if (event.getCurrentItem().getType() == Material.ARROW) {
                queteMenu.openMainMenu(player);
                return;
            }

            // Navigation depuis le Menu Principal
            if (title.equals("§8Catégories de quêtes")) {
                switch (event.getCurrentItem().getType()) {
                    case DIAMOND_PICKAXE:
                        queteMenu.openMiningMenu(player);
                        break;
                    case GOLDEN_HOE:
                        queteMenu.openFarmingMenu(player);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}