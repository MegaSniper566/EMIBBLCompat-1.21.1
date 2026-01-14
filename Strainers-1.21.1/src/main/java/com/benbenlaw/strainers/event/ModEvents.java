package com.benbenlaw.strainers.event;

//@EventBusSubscriber(modid = Strainers.MOD_ID)
public class ModEvents {


    /*
    public static Fluid targetedFluid;

    //Horrible way of setting fluid kinda works but HORRIBLE
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = event.getEntity().level();

        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.is(ModBlocks.STRAINER_TANK.asItem())) {

            double reachDistance = 5.0;

            Vec3 eyePosition = player.getEyePosition(1.0F);
            Vec3 lookDirection = player.getLookAngle().scale(reachDistance);

            HitResult hitResult = level.clip(new ClipContext(
                    eyePosition,
                    eyePosition.add(lookDirection),
                    ClipContext.Block.OUTLINE,
                    ClipContext.Fluid.ANY,
                    player
            ));

            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                BlockPos blockPos = blockHitResult.getBlockPos();
                BlockState blockState = level.getBlockState(blockPos);

                if (!blockState.getFluidState().isEmpty() ) {
                    Fluid fluid = blockState.getFluidState().getType();

                    if (blockState.getFluidState().isSourceOfType(fluid)) {
                        targetedFluid = fluid;
                    }
                }
            }
        }


    }

     */


}
