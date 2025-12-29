package net.hasagj.teamod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {

    public static final KeyMapping ABILITY = new KeyMapping(
            "key.teamod.ability",       // lang key
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_R,                // default key
            "key.categories.teamod"              // category
    );
}
