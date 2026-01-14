package com.benbenlaw.strainers.util;

import com.benbenlaw.strainers.fluid.StrainersFluids;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;

public final class StrainersColorHandler {


    @SubscribeEvent
    public void onItemColors(RegisterColorHandlersEvent.Item event) {

        var buckets = new Item[] {
                StrainersFluids.ERODING_WATER.getBucket(),
                StrainersFluids.PURIFYING_WATER.getBucket(),
        };

        event.register((stack, tint) -> {
            var fluid = ((BucketItem) stack.getItem()).content;
            return tint == 1 ? IClientFluidTypeExtensions.of(fluid).getTintColor() : -1;
        }, buckets);

    }

}
