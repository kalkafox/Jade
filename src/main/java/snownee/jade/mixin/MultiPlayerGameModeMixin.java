package snownee.jade.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;

import net.minecraft.core.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import snownee.jade.JadeClient;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

	@Inject(method = "destroyBlock", at = @At("RETURN"))
	private void jade$onDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			JadeClient.setBreakProgress(1);
		}
	}

}
