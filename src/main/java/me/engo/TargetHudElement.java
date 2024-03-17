package me.engo;

import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.hud.ResizeableHudElement;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.setting.EnumSetting;

import java.awt.*;


public class TargetHudElement extends ResizeableHudElement {

    // lord above please forgive me for this code, second project using java :pray:

    /*
        TODO: possibly add slider to change how wide the element is (to the right) because i want that to be constant but also changing bc i am bad ui designer
        TODO: actual targeting targets =(
        TODO: add graphic square players head and add animal support
     */

    enum Show {
        TARGETING,
        ALWAYS
    }

    enum BackgroundMode {
        MARGINED,
        NO_MARGIN,
        NONE,
    }

    private final EnumSetting<Show> showMode = new EnumSetting<>("Show", Show.TARGETING);
    private final EnumSetting<BackgroundMode> backgroundMode = new EnumSetting<>("Background", BackgroundMode.MARGINED);
    private final ColorSetting backgroundColor = new ColorSetting("Color", new Color(0, 0, 0, 0.5f));

    private LivingEntity currentTarget;

    public TargetHudElement() {
        super("Target");

        this.registerSettings(
                this.showMode,
                this.backgroundMode,
                this.backgroundColor
        );
    }

    public void setTarget(LivingEntity target) {
         this.currentTarget = target;
    }

    public LivingEntity getTarget() {
        if (mc.crosshairPickEntity != null) {
            return (LivingEntity) mc.crosshairPickEntity;
        }
        return null;
    }

    Screen hudEditorScreen = RusherHackAPI.getThemeManager().getHudEditorScreen();
    double squareSideLen = 40;

    @Override
    public void renderContent(RenderContext context, int mouseX, int mouseY) {
        LivingEntity target = this.getTarget();
        if (target == null && showMode.getValue() == Show.TARGETING && mc.screen != hudEditorScreen) {
            return;
        }
            
        // base background rectangle
        int backgroundColorValue = backgroundColor.getValueRGB();
        if (backgroundMode.getValue() != BackgroundMode.NONE) {
            this.getRenderer().drawRectangle(0, 0, this.getWidth(), this.getHeight(), backgroundColorValue);
        }


        double xGraphic = this.getWidth() / squareSideLen * 3;
        double yGraphic = (this.getHeight() / 2) - (squareSideLen / 2);
        if (backgroundMode.getValue() != BackgroundMode.MARGINED) {
            xGraphic = 0;
        }

        if (target != null) {
            PlayerFaceRenderer.draw(context.graphics(), ((AbstractClientPlayer) target).getSkin().texture(), (int) xGraphic, (int) yGraphic, (int) squareSideLen);
        } else {
            PlayerFaceRenderer.draw(context.graphics(), mc.player.getSkin().texture(), (int) xGraphic, (int) yGraphic, (int) squareSideLen);
        }

        // there has to be a better way of doing this. I am not a java programmer, please forgive me...
        String targetName;
        double health;
        double distance;
        String itemName;

        if (target != null) {
            targetName = target.getName().getString();
            health = target.getHealth();
            distance = (double) Math.round(target.distanceTo(mc.player) * 100) / 100;
            itemName = target.getMainHandItem().getDisplayName().getString();
        } else {
            targetName = "No Target";
            health = 0;
            distance = 0;
            itemName = "No Item";
        }

        // Name string
        double offset = 3;
        double fHeight = this.getFontRenderer().getFontHeight();
        this.getFontRenderer().drawString(targetName, xGraphic + (squareSideLen + 5), yGraphic + offset, 0xffffffff); //TODO: show text colour as enemy/friend?
        this.getFontRenderer().drawString(health + " Health", xGraphic + (squareSideLen + 5), yGraphic + ((fHeight) * 1) + offset, 0xffffffff);
        this.getFontRenderer().drawString(distance + " Meters", xGraphic + (squareSideLen + 5), yGraphic + ((fHeight) * 2) + offset, 0xffffffff);
        this.getFontRenderer().drawString(itemName, xGraphic + (squareSideLen + 5), yGraphic + ((fHeight) * 3) + offset, 0xffffffff);
    }

    @Override
    public double getWidth() {
        return 160;
    }

    @Override
    public double getHeight() {
        if (backgroundMode.getValue() == BackgroundMode.MARGINED) {
            return 60;
        }

        return squareSideLen;
    }
}