package glowsand.woodbark;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.FoodComponent;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Woodbark implements ModInitializer {
    public static BarkConfig config;
    public static String modId = "bitsofbark";

    public static Gson configDataStuff = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
    public static Path pathForTheConfig = Paths.get("config/wood-barks.json");
    public static BarkConfig defaultConfig = BarkConfig.getDefaultConfig();
    public static BarkItem barkItem = null;
    public static List<Tag<Block>> blockTags = new ArrayList<>();
    public static Tag<Block> GIVE_BARK = null;

    @Override
    public void onInitialize() {

        GIVE_BARK= TagRegistry.block(new Identifier(modId,"give_bark"));
        initMod();
        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, serverResourceManager) -> initConfig());
    }


    public static void initConfig(){
        try{
            if (pathForTheConfig.toFile().exists()){
                config= configDataStuff.fromJson(new String(Files.readAllBytes(pathForTheConfig)),BarkConfig.class);
            }else{
                Files.write(pathForTheConfig, Collections.singleton(configDataStuff.toJson(defaultConfig)));
                config=defaultConfig;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void initMod(){
        initConfig();

            if (config.isFood) {
                barkItem = new BarkItem(new FoodComponent.Builder().hunger(config.hunger).saturationModifier(config.saturation).snack().build());
            } else {
                barkItem = new BarkItem();
            }

            Registry.register(Registry.ITEM, new Identifier(modId, "wood_bark"),
                    barkItem
            );
            if (config.isFuel) {
                FuelRegistry.INSTANCE.add(barkItem, config.fuelTicks);
            }

    }
}
