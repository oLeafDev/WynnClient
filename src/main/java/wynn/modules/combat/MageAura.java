package wynn.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wynn.modules.Module;
import wynn.ui.base.base.IToggleable;
import wynn.ui.setting.IntegerSetting;

import static wynn.modules.combat.TriggerBot.mc;

public class MageAura extends Module {

    public static MageAura instance;

    public static IntegerSetting range = new IntegerSetting("Range", "Range", "Range", ()->true, 6, 20, 6);

    public MageAura() {
        super("MageAura", "MageAura", ()->true, true);
        instance = this;
        settings.add(range);
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if(getToggle().get()) {
                if (mc.player != null) {
                    if(mc.world != null) {
                        for (int i = 0; i < mc.world.loadedEntityList.size(); i++) {
                            if (mc.world.loadedEntityList.get(i) instanceof net.minecraft.entity.EntityLivingBase) {
                                net.minecraft.entity.EntityLivingBase entity = (net.minecraft.entity.EntityLivingBase) mc.world.loadedEntityList.get(i);
                                if (entity.getDistance(mc.player) <= range.getValue()) {
                                    if (entity instanceof EntityPlayer) return;
                                    if (!entity.getName().equals(mc.player.getName())) {
                                        double diffX = entity.posX - mc.player.posX;
                                        double diffY = entity.posY + entity.getEyeHeight() - mc.player.posY - mc.player.getEyeHeight();
                                        double diffZ = entity.posZ - mc.player.posZ;
                                        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);

                                        float pitch = (float) -Math.atan2(dist, diffY);
                                        float yaw = (float) Math.atan2(diffZ, diffX);
                                        pitch = (float) Math.toDegrees(pitch);
                                        yaw = (float) Math.toDegrees(yaw);

                                        mc.player.rotationPitch = pitch;
                                        mc.player.rotationYaw = yaw;
                                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
