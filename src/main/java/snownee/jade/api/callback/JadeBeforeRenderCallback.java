package snownee.jade.api.callback;

import net.minecraft.client.gui.GuiGraphics;
import snownee.jade.api.Accessor;
import snownee.jade.api.ui.IBoxElement;
import snownee.jade.api.ui.TooltipRect;

@FunctionalInterface
public interface JadeBeforeRenderCallback {

	boolean beforeRender(IBoxElement rootElement, TooltipRect rect, GuiGraphics guiGraphics, Accessor<?> accessor);

}
