package com.gildedrose;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions.assertEquals;

//Test class for the GildedRose system
public class GildedRoseTest {

    private Item[] items; // Array of items to test
    private GildedRose gildedRose; // Instance of the GildedRose class

    @BeforeEach
    public void setUp() {
        // Initialize items with different types for testing
        items = new Item[]{
                new Item("Aged Brie", 10, 20),  // Item whose quality increases over time
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),  // Special item with unique quality rules
                new Item("Sulfuras, Hand of Ragnaros", 0, 80),  // Legendary item that never changes
                new Item("Conjured Mana Cake", 3, 6),  // Item that degrades faster
                new Item("Normal Item", 5, 10)  // Standard item
        };
        // Create the GildedRose instance with the converted items to UpdatableItems
        gildedRose = new GildedRose(convertToUpdatableItems(items));
    }

    // Method to convert the array of Items to UpdatableItems
    private UpdatableItem[] convertToUpdatableItems(Item[] items) {
        UpdatableItem[] updatableItems = new UpdatableItem[items.length];  // Create an array of UpdatableItems
        for (int i = 0; i < items.length; i++) {
            // Determine the type of item and create the corresponding class
            switch (items[i].name) {
                case "Aged Brie":
                    updatableItems[i] = new AgedBrieItem(items[i]);
                    break;
                case "Backstage passes to a TAFKAL80ETC concert":
                    updatableItems[i] = new BackstagePassItem(items[i]);
                    break;
                case "Sulfuras, Hand of Ragnaros":
                    updatableItems[i] = new SulfurasItem(items[i]);
                    break;
                case "Conjured Mana Cake":
                    updatableItems[i] = new ConjuredItem(items[i]);
                    break;
                default:
                    updatableItems[i] = new StandardItem(items[i]);  // Use StandardItem class for normal items
            }
        }
        return updatableItems;  // Return the array of UpdatableItems
    }

    @Test
    public void testAgedBrieIncreasesQuality() {
        gildedRose.updateQuality();  // Update quality for all items
        assertEquals(21, items[0].quality);  // Verify that the quality of "Aged Brie" increases
    }

    @Test
    public void testBackstagePassesQualityIncreases() {
        gildedRose.updateQuality();  // Update quality for all items
        assertEquals(21, items[1].quality);  // Verify that the quality of "Backstage passes" increases normally
    }

    @Test
    public void testBackstagePassesQualityIncreasesByTwoWhenTenDaysLeft() {
        items[1].sellIn = 10;  // Set days left to 10
        gildedRose.updateQuality();  // Update quality
        assertEquals(22, items[1].quality);  // Verify that the quality increases by 2
    }

    @Test
    public void testBackstagePassesQualityIncreasesByThreeWhenFiveDaysLeft() {
        items[1].sellIn = 5;  // Set days left to 5
        gildedRose.updateQuality();  // Update quality
        assertEquals(23, items[1].quality);  // Verify that the quality increases by 3
    }

    @Test
    public void testBackstagePassesQualityDropsToZeroAfterConcert() {
        items[1].sellIn = 0;  // Set days left to 0
        gildedRose.updateQuality();  // Update quality
        assertEquals(0, items[1].quality);  // Verify that the quality drops to 0 after the concert
    }

    @Test
    public void testSulfurasQualityRemainsConstant() {
        gildedRose.updateQuality();  // Update quality for all items
        assertEquals(80, items[2].quality);  // Verify that the quality of "Sulfuras" remains constant at 80
    }

    @Test
    public void testConjuredItemDegradesQualityFaster() {
        gildedRose.updateQuality();  // Update quality for all items
        assertEquals(4, items[3].quality);  // Verify that the quality of "Conjured" decreases by 2
    }

    @Test
    public void testNormalItemDegradesQuality() {
        gildedRose.updateQuality();  // Update quality for all items
        assertEquals(9, items[4].quality);  // Verify that the quality of the normal item decreases by 1
    }

    @Test
    public void testQualityNeverNegative() {
        items[3].quality = 1;  // Set quality to 1 for this test
        gildedRose.updateQuality();  // Update quality
        assertEquals(0, items[3].quality);  // Verify that the quality does not go negative
    }

    @Test
    public void testQualityNeverExceedsFifty() {
        items[0].quality = 50;  // Set quality to 50 for this test
        gildedRose.updateQuality();  // Update quality
        assertEquals(50, items[0].quality);  // Verify that the quality never exceeds 50
    }
}
