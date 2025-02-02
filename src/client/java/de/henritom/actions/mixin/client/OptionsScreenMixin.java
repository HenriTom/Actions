package de.henritom.actions.mixin.client;

import de.henritom.actions.ui.impl.MainScreen;
import de.henritom.actions.ui.impl.SettingsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void addButton(CallbackInfo ci) {
        if (SettingsScreen.addOptionsScreenButton) {
            ButtonWidget button = ButtonWidget.builder(Text.translatable("actions.ui.button"), (buttonWidget) -> {
                if (this.client != null)
                    this.client.setScreen(new MainScreen(this));
                    })
                    .dimensions(4, 4, textRenderer.getWidth(Text.translatable("actions.ui.button")) + 16, textRenderer.fontHeight + 8)
                    .build();

            this.addDrawableChild(button);
        }
    }

}
