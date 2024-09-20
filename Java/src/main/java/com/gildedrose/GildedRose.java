package com.gildedrose;

// Classe principale GildedRose qui gère les mises à jour des items.
public class GildedRose {
    private UpdatableItem[] updatableItems;  // Utilisation du polymorphisme pour simplifier la logique.

    // Constructeur de la classe GildedRose, qui reçoit un tableau d'UpdatableItems.
    public GildedRose(UpdatableItem[] updatableItems) {
        this.updatableItems = updatableItems;
    }

    // Méthode updateQuality qui délègue la mise à jour de chaque item à sa propre classe.
    public void updateQuality() {
        for (UpdatableItem updatableItem : updatableItems) {
            updatableItem.update();  // Chaque type d'item implémente sa propre logique de mise à jour.
        }
    }
}

// Interface commune pour tous les types d'items, permettant une méthode de mise à jour unifiée.
interface UpdatableItem {
    void update();
}

// Classe de base abstraite pour partager du comportement commun entre différents types d'items.
abstract class BaseItem implements UpdatableItem {
    protected Item item;

    public BaseItem(Item item) {
        this.item = item;
    }

    // Méthode commune pour diminuer la valeur sellIn.
    protected void decreaseSellIn() {
        item.sellIn--;
    }

    // Méthode commune pour vérifier que la qualité est dans les limites (0 à 50).
    protected void ensureQualityIsWithinBounds() {
        if (item.quality < 0) {
            item.quality = 0;
        } else if (item.quality > 50) {
            item.quality = 50;
        }
    }

    // Méthode commune pour diminuer la qualité d'un item de manière classique.
    protected void decreaseQuality() {
        item.quality--;
    }
}

// Implémentation de la mise à jour pour "Aged Brie" qui améliore sa qualité avec le temps.
class AgedBrieItem extends BaseItem {

    public AgedBrieItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.quality < 50) {
            item.quality++;  // "Aged Brie" voit sa qualité augmenter avec le temps.
        }
        decreaseSellIn();

        if (item.sellIn < 0 && item.quality < 50) {
            item.quality++;  // Si la date de péremption est passée, la qualité augmente encore plus.
        }
        ensureQualityIsWithinBounds();
    }
}

// Implémentation de la mise à jour pour "Backstage passes".
class BackstagePassItem extends BaseItem {

    public BackstagePassItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.sellIn > 10) {
            item.quality++;  // La qualité augmente normalement.
        } else if (item.sellIn > 5) {
            item.quality += 2;  // Moins de 10 jours restants, la qualité augmente de 2.
        } else if (item.sellIn > 0) {
            item.quality += 3;  // Moins de 5 jours restants, la qualité augmente de 3.
        } else {
            item.quality = 0;  // Après le concert, la qualité tombe à 0.
        }

        decreaseSellIn();
        ensureQualityIsWithinBounds();  // Assure que la qualité reste dans les limites acceptées.
    }
}

// Implémentation de la mise à jour pour "Sulfuras" (objet légendaire).
class SulfurasItem extends BaseItem {

    public SulfurasItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        // "Sulfuras" est un objet légendaire, donc rien ne change : ni qualité, ni sellIn.
        // On ne fait rien ici car la qualité de "Sulfuras" ne change jamais.
    }
}

// Implémentation de la mise à jour pour les objets "Conjured" (qualité se dégrade plus vite).
class ConjuredItem extends BaseItem {

    public ConjuredItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.quality > 0) {
            item.quality -= 2;  // La qualité se dégrade deux fois plus vite.
        }
        decreaseSellIn();

        if (item.sellIn < 0 && item.quality > 0) {
            item.quality -= 2;  // Après la date de péremption, la qualité se dégrade encore plus rapidement.
        }
        ensureQualityIsWithinBounds();  // Assure que la qualité reste positive et dans les limites.
    }
}

// Classe de base pour les items standards, utilisée par défaut si l'item n'a pas de comportement spécifique.
class StandardItem extends BaseItem {

    public StandardItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        decreaseQuality();  // La qualité diminue normalement.
        decreaseSellIn();

        if (item.sellIn < 0) {
            decreaseQuality();  // Après la date de péremption, la qualité diminue encore plus vite.
        }
        ensureQualityIsWithinBounds();  // Assure que la qualité reste dans les limites (0 à 50).
    }
}

/* CODE DE DÉPART **
********************
class GildedRose {
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (int i = 0; i < items.length; i++) {
            if (!items[i].name.equals("Aged Brie")
                    && !items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                if (items[i].quality > 0) {
                    if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                        items[i].quality = items[i].quality - 1;
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].name.equals("Aged Brie")) {
                    if (!items[i].name.equals("Backstage passes to a TAFKAL80ETC concert")) {
                        if (items[i].quality > 0) {
                            if (!items[i].name.equals("Sulfuras, Hand of Ragnaros")) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }
    }
}
*********************
*/
