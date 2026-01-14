package com.benbenlaw.strainers.event.client;

import com.benbenlaw.strainers.Strainers;
import com.benbenlaw.strainers.block.entity.ModBlockEntities;
import com.benbenlaw.strainers.block.entity.client.StrainerMeshEntityRenderer;
import com.benbenlaw.strainers.block.entity.client.StrainerTankBlockEntityRenderer;
import com.benbenlaw.strainers.fluid.StrainersFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Strainers.MOD_ID, value = Dist.CLIENT)
public class ClientListener {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {


        event.registerBlockEntityRenderer(ModBlockEntities.STRAINER_TANK_BLOCK_ENTITY.get(),
                StrainerTankBlockEntityRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.WOODEN_STRAINER_BLOCK_ENTITY.get(),
                StrainerMeshEntityRenderer::new);

    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(StrainersFluids.ERODING_WATER.getStillFluid(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(StrainersFluids.ERODING_WATER.getFlowingFluid(), RenderType.translucent());

            ItemBlockRenderTypes.setRenderLayer(StrainersFluids.PURIFYING_WATER.getStillFluid(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(StrainersFluids.PURIFYING_WATER.getFlowingFluid(), RenderType.translucent());
        });
    }

}
