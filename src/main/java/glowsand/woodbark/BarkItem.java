package glowsand.woodbark;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Random;

public class BarkItem extends Item {
    public BarkItem(FoodComponent component) {
        super(new Settings().food(component).group(ItemGroup.MATERIALS));

    }


    public BarkItem() {
        super(new Settings().group(ItemGroup.MATERIALS));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (!world.isClient && Woodbark.config.woodTypes.containsKey(Registry.BLOCK.getId(block).toString())){

            Block secondBlock = Registry.BLOCK.get(new Identifier( Woodbark.config.woodTypes.get(Registry.BLOCK.getId(block).toString())));
            BlockState stateofDeez;
            if (secondBlock instanceof PillarBlock) {
                stateofDeez = secondBlock.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS));

            }else {
                stateofDeez= secondBlock.getDefaultState();
            }
            world.setBlockState(blockPos,stateofDeez);
            if (playerEntity!=null && !playerEntity.getAbilities().creativeMode){
                        context.getStack().decrement(1);
                    }
            for (int a = 1; a <= 10; a++) {
                Random random = world.getRandom();
                ((ServerWorld)world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK,stateofDeez), Vec3d.ofCenter(blockPos).getX() + (random.nextDouble()- random.nextDouble())*2 , Vec3d.ofCenter(blockPos).getY() + (random.nextDouble()- random.nextDouble())*2, Vec3d.ofCenter(blockPos).getZ() + (random.nextDouble()- random.nextDouble())*2, 1,  (random.nextDouble()*1) * .15,   (random.nextDouble()) * .15, (world).getRandom().nextDouble() * .15, (world).getRandom().nextDouble() * 0.1);
            }
        }

        return super.useOnBlock(context);
    }
}
