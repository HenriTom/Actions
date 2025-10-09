package de.henritom.actions.mixin.client;

import de.henritom.actions.triggers.impl.KeyTrigger;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

	@Inject(at = @At("HEAD"), method = "onKey")
	private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
		if (action == 1)
			new KeyTrigger().trigger(input.key());
	}
}