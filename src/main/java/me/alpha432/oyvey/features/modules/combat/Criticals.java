package me.yourclient.features.modules.combat;

import me.yourclient.event.impl.TickEvent;
import me.yourclient.event.system.Subscribe;
import me.yourclient.features.modules.Module;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class AutoAttack extends Module {

    public AutoAttack() {
        super("AutoAttack", "Attacks a player when you look at them", Category.COMBAT);
    }

    @Subscribe
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.level == null || mc.gameMode == null) return;

        // Respect 1.21 attack cooldown
        if (mc.player.getAttackStrengthScale(0.0F) < 1.0F) return;

        // Check what the crosshair is pointing at
        HitResult hitResult = mc.hitResult;
        if (!(hitResult instanceof EntityHitResult entityHit)) return;

        Entity target = entityHit.getEntity();

        // Only attack players
        if (!(target instanceof Player)) return;

        // Safety checks
        if (!target.isAlive()) return;
        if (target == mc.player) return;

        // Optional reach limit (vanilla-like)
        if (mc.player.distanceTo(target) > 3.0F) return;

        // Perform ONE normal vanilla attack
        mc.gameMode.attack(mc.player, target);
        mc.player.swing(InteractionHand.MAIN_HAND);
    }

    @Override
    public String getDisplayInfo() {
        return "Look";
    }
}
