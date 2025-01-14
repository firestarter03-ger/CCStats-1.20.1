package net.firestarter03.ccstats.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
	/**
	 * Diese Methode wird injiziert, bevor der Hintergrund gefüllt wird.
	 */
	@Inject(method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("HEAD"), cancellable = true)
	private void disableDarkBackground(DrawContext context, CallbackInfo info) {
		// Wenn wir das Inventar mit dem speziellen Namen haben, brechen wir den Hintergrund ab.
		Screen self = (Screen) (Object) this;
		if (self.getTitle().getString().contains("㚝")) {
			// Den Standard-Hintergrund nicht rendern.
			info.cancel();
		}
	}
}
