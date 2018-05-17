package com.axevillager.blacksmith.utilities;

import org.bukkit.Material;

/**
 * StringUtils created by AxeVillager on 2018/04/22.
 */

public class StringUtils {

    public static String fixMaterialName(final Material material) {
        return material.name().toLowerCase().replaceAll("_", " ");
    }

    // Doesn't work perfectly, for it does not take sound into consideration.
    // Example: "A university" is correct, because the 'u' in "university"
    // is pronounced as a 'j'. This method thinks it is "an university".
    public static String determineAnOrA(final String word) {
        final char[] vowels = {'a', 'e', 'i', 'o', 'u', 'y'};
        for (final char vowel : vowels)
            if (word.charAt(0) == vowel)
                return "an";
        return "a";
    }
}