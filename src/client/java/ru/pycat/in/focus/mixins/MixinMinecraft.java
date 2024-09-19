package ru.pycat.in.focus.mixins;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ru.pycat.in.focus.InFocusClient.CONFIG;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "displayInGameMenu", at = @At("HEAD"), cancellable = true)
    private void cancelDisplayInGameMenu(CallbackInfo ci) {
        if (CONFIG.isEnabled && !Display.isActive()) {
            ci.cancel();
        }
    }

    // Injects into `Display.isActive` to toggle thing that resize window on lost focus
    @Redirect(method = "run",
              at = @At(
                      value = "INVOKE",
                      target = "Lorg/lwjgl/opengl/Display;isActive()Z"
              ))
    private boolean isDisplayActive() {
        return CONFIG.isEnabled || Display.isActive();
    }

    @Inject(method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/src/client/player/EntityPlayerSP;handleKeyPress(IZ)V",
                    shift = At.Shift.AFTER
            ))
    private void afterPlayerKeyHandler(CallbackInfo ci) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_P)) {
                CONFIG.isEnabled = !CONFIG.isEnabled;

                Minecraft.getInstance().thePlayer.displayChatMessage(
                        String.format("[F3+P]: Pause on lost focus: %s",
                                     (!CONFIG.isEnabled ? "enabled" : "disable"))
                );
            }
        }
    }
}
