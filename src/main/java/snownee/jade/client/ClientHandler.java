package snownee.jade.client;

import java.awt.*;

import mcp.mobius.waila.api.RenderContext;
import mcp.mobius.waila.api.event.WailaRenderEvent;
import mcp.mobius.waila.api.impl.config.PluginConfig;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import snownee.jade.JadePlugin;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public final class ClientHandler {
	private static float savedProgress;
	private static float progressAlpha;

	@SubscribeEvent
	public static void post(WailaRenderEvent.Post event) {
		if (!PluginConfig.INSTANCE.get(JadePlugin.BREAKING_PROGRESS)) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		PlayerController playerController = mc.playerController;
		if (playerController == null || playerController.currentBlock == null) {
			return;
		}
		BlockState state = mc.world.getBlockState(playerController.currentBlock);
		boolean canHarvest = ForgeHooks.canHarvestBlock(state, mc.player, mc.world, playerController.currentBlock);
		int color = canHarvest ? 0x88FFFFFF : 0x88FF4444;
		Color fadeColor = new Color(color);
		Color alphaColor = new Color(fadeColor.getRed(), fadeColor.getGreen(), fadeColor.getBlue(), (int) MathHelper.clamp(progressAlpha, 0, 200));
		Rectangle rect = event.getPosition();
		progressAlpha = (progressAlpha > 200 ? 200 : progressAlpha);
		progressAlpha = (progressAlpha < 0 ? 0 : progressAlpha);
		if (playerController.getIsHittingBlock()) {
			float progress = state.getPlayerRelativeBlockHardness(mc.player, mc.player.world, playerController.currentBlock);
			progress = playerController.curBlockDamageMP + mc.getRenderPartialTicks() * progress;
			progress = MathHelper.clamp(progress, 0, 1);
			progressAlpha = (playerController.curBlockDamageMP < 0.01 ? (progressAlpha - (savedProgress * 100)  * mc.getTickLength()) : progressAlpha);
			progressAlpha += (savedProgress * mc.getTickLength()) * 100;
			AbstractGui.fill(RenderContext.matrixStack, rect.x + 1, rect.y + rect.height, rect.x + 1 + (int) (rect.width * progress), rect.y + rect.height + 1, alphaColor.getRGB());
			savedProgress = progress;
		} else {
			progressAlpha -= (savedProgress * mc.getTickLength()) * 50;
			AbstractGui.fill(RenderContext.matrixStack, rect.x + 1, rect.y + rect.height, rect.x + 1 + (int) (rect.width * savedProgress), rect.y + rect.height + 1, alphaColor.getRGB());
		}
	}

}
