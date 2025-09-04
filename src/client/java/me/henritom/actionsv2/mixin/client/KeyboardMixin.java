package me.henritom.actionsv2.mixin.client;

import me.henritom.actionsv2.axn.trigger.TriggerRegistry;
import me.henritom.actionsv2.util.KeyUtil;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(at = @At("HEAD"), method = "onKey")
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        String callArgs = "key=" + key + "; action=" + action + "; modifiers=" + modifiers + "; scancode=" + scancode + ";";

        Map<String, Object> currentValues = Map.of("key", key, "action", action, "modifiers", modifiers, "scancode", scancode);

        for (var trigger : TriggerRegistry.INSTANCE.getTriggersByType("keybind")) {
            var data = trigger.getData();

            if (data.isEmpty()) {
                trigger.trigger(callArgs);
                continue;
            }

            boolean matches = true;
            for (var entry : data.entrySet()) {
                if (currentValues.get(entry.getKey()) == null) {
                    matches = false;
                    break;
                }

                if (entry.getKey().equals("key")) {
                    if (KeyUtil.INSTANCE.parseKeyString(String.valueOf(entry.getValue())) != ((Number) currentValues.get(entry.getKey())).intValue()) {
                        matches = false;
                        break;
                    }

                    continue;
                }

                try {
                    double expectedNum = Double.parseDouble(String.valueOf(entry.getValue()));
                    double actualNum = Double.parseDouble(String.valueOf(currentValues.get(entry.getKey())));

                    if (expectedNum != actualNum) {
                        matches = false;
                        break;
                    }
                } catch (NumberFormatException e) {
                    if (!String.valueOf(entry.getValue()).equals(String.valueOf(currentValues.get(entry.getKey())))) {
                        matches = false;
                        break;
                    }
                }
            }

            if (matches)
                trigger.trigger(callArgs);
        }
    }
}
