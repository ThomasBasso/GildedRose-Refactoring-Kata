package com.gildedrose;

// Main class GildedRose that manages the updates of items.
public class GildedRose {
    private final UpdatableItem[] updatableItems;  // Using polymorphism to simplify logic.

    // Constructor for the GildedRose class, which receives an array of UpdatableItems.
    public GildedRose(UpdatableItem[] updatableItems) {
        this.updatableItems = updatableItems;
    }

    // updateQuality method that delegates the update of each item to its own class.
    public void updateQuality() {
        for (UpdatableItem updatableItem : updatableItems) {
            updatableItem.update();  // Each item type implements its own update logic.
        }
    }
}

// Common interface for all item types, allowing for a unified update method.
interface UpdatableItem {
    void update();
}

// Abstract base class to share common behavior among different item types.
abstract class BaseItem implements UpdatableItem {
    protected Item item;

    public BaseItem(Item item) {
        this.item = item;
    }

    // Common method to decrease the sellIn value.
    protected void decreaseSellIn() {
        item.sellIn--;
    }

    // Common method to ensure quality is within bounds (0 to 50).
    protected void ensureQualityIsWithinBounds() {
        if (item.quality < 0) {
            item.quality = 0;
        } else if (item.quality > 50) {
            item.quality = 50;
        }
    }

    // Common method to decrease an item's quality normally.
    protected void decreaseQuality() {
        item.quality--;
    }
}

// Implementation of the update for "Aged Brie" which improves its quality over time.
class AgedBrieItem extends BaseItem {

    public AgedBrieItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.quality < 50) {
            item.quality++;  // "Aged Brie" sees its quality increase over time.
        }
        decreaseSellIn();

        if (item.sellIn < 0 && item.quality < 50) {
            item.quality++;  // If the expiration date has passed, quality increases even more.
        }
        ensureQualityIsWithinBounds();
    }
}

// Implementation of the update for "Backstage passes".
class BackstagePassItem extends BaseItem {

    public BackstagePassItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.sellIn > 10) {
            item.quality++;  // Quality increases normally.
        } else if (item.sellIn > 5) {
            item.quality += 2;  // Less than 10 days left, quality increases by 2.
        } else if (item.sellIn > 0) {
            item.quality += 3;  // Less than 5 days left, quality increases by 3.
        } else {
            item.quality = 0;  // After the concert, quality drops to 0.
        }

        decreaseSellIn();
        ensureQualityIsWithinBounds();  // Ensures that quality remains within accepted limits.
    }
}

// Implementation of the update for "Sulfuras" (legendary item).
class SulfurasItem extends BaseItem {

    public SulfurasItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        // "Sulfuras" is a legendary item, so nothing changes: neither quality nor sellIn.
        // Do nothing here because "Sulfuras" quality never changes.
    }
}

// Implementation of the update for "Conjured" items (quality degrades faster).
class ConjuredItem extends BaseItem {

    public ConjuredItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        if (item.quality > 0) {
            item.quality -= 2;  // Quality degrades twice as fast.
        }
        decreaseSellIn();

        if (item.sellIn < 0 && item.quality > 0) {
            item.quality -= 2;  // After the expiration date, quality degrades even faster.
        }
        ensureQualityIsWithinBounds();  // Ensures that quality remains positive and within bounds.
    }
}

// Base class for standard items, used by default if the item has no specific behavior.
class StandardItem extends BaseItem {

    public StandardItem(Item item) {
        super(item);
    }

    @Override
    public void update() {
        decreaseQuality();  // Quality decreases normally.
        decreaseSellIn();

        if (item.sellIn < 0) {
            decreaseQuality();  // After the expiration date, quality decreases even faster.
        }
        ensureQualityIsWithinBounds();  // Ensures that quality remains within limits (0 to 50).
    }
}

/*  STARTING CODE **
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
