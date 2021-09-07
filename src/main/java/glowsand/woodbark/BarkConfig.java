package glowsand.woodbark;

import glowsand.woodbark.mixin.AxeItemAccessor;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BarkConfig {
    public final int hunger;
    public final float saturation;
    public final boolean isFuel;
    public final int fuelTicks;
    public HashMap<String, String> woodTypes;
    public List<String> giveBark;
    public boolean isFood;

    public BarkConfig(HashMap<String, String> woodTypes, List<String> giveBark, boolean isFood, int hunger, float saturation, boolean isFuel, int fuelTicks){
        this.woodTypes = woodTypes;
        this.giveBark = giveBark;
        this.isFood = isFood;
        this.hunger=hunger;
        this.saturation=saturation;
        this.isFuel = isFuel;
        this.fuelTicks = fuelTicks;
    }

    public static BarkConfig getDefaultConfig(){
        HashMap<String,String> defaultHashMap = new HashMap<>();
        List<String> giveBarkAt = new ArrayList<>();
        giveBarkAt.add("#"+Woodbark.modId +":give_bark");
        giveBarkAt.add("#moretags:dressed_wood");
        AxeItemAccessor.getStrippedBlocks().forEach(((block, block2) -> {
            defaultHashMap.put(Registry.BLOCK.getId(block2).toString(),Registry.BLOCK.getId(block).toString());

                giveBarkAt.add(Registry.BLOCK.getId(block).toString());

        }));
        return new BarkConfig(defaultHashMap,giveBarkAt,true,2,0.1F,true,75);
    }
}
