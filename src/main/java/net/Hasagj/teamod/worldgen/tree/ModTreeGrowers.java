package net.hasagj.teamod.worldgen.tree;
import net.hasagj.teamod.TeaMod;
import net.hasagj.teamod.worldgen.ModConfiguredFeatures;
import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower HIBISCUS = new TreeGrower(TeaMod.MOD_ID + ":hibiscus",
            Optional.empty(), Optional.of(ModConfiguredFeatures.HIBISCUS_KEY), Optional.empty());

}