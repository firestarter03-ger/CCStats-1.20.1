package net.firestarter03.ccstats;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class LoreUtils {
    public static List<String> getLore(ItemStack stack) {
        List<String> lore = new ArrayList<>();
        if (stack.hasNbt() && stack.getNbt().contains("Statistiken")) {
            NbtCompound displayTag = stack.getNbt().getCompound("Statistiken");
            if (displayTag.contains("Lore")) {
                NbtList loreList = displayTag.getList("Lore", 8);
                for (int i = 0; i < loreList.size(); i++) {
                    lore.add(loreList.getString(i).replace("\"", "").strip());
                }
            }
        }
        return lore;
    }
}
