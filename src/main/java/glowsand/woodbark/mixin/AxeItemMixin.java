package glowsand.woodbark.mixin;


import glowsand.woodbark.Woodbark;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Optional;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin {
    @Shadow @Final protected static Map<Block, Block> STRIPPED_BLOCKS;

    @Inject(method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",at = @At("HEAD"))
    public void useOnBlockInject(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        World world = context.getWorld();

        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (Woodbark.blockTags.isEmpty()){
            Woodbark.config.giveBark.stream().filter((s)-> s.charAt(0) == '#').forEach((s)->{
                Tag<Block> blockTag = BlockTags.getTagGroup().getTag(new Identifier(s.substring(1)));
                if (blockTag!=null){
                    Woodbark.blockTags.add(blockTag);
                }
            });
        }

        boolean isInTags = false;
        for (Tag<Block> blockTag : Woodbark.blockTags) {
            isInTags = blockTag.contains(block);
            if (isInTags){
                break;
            }
        }

        boolean shouldGiveBark = Woodbark.config.giveBark.contains(blockState.toString())
                ||Woodbark.config.giveBark.contains(Registry.BLOCK.getId(blockState.getBlock()).toString())
                ||  isInTags;
        if (shouldGiveBark && playerEntity instanceof ServerPlayerEntity){
            playerEntity.giveItemStack(Woodbark.barkItem.getDefaultStack());
        }
    }
}
