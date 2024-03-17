package me.engo;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;
public class TargetHud extends Plugin {

    @Override
    public void onLoad() {
        final TargetHudElement targetHudElement = new TargetHudElement();
        RusherHackAPI.getHudManager().registerFeature(targetHudElement);
    }

    @Override
    public void onUnload() {}
}