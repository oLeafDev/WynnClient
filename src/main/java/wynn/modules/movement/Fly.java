package wynn.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wynn.modules.Module;
import wynn.modules.Modules;
import wynn.ui.base.base.IToggleable;
import wynn.ui.setting.IntegerSetting;

public class Fly extends Module {

    public static Fly instance;
    public static Minecraft mc = Minecraft.getMinecraft();

    public final IntegerSetting speed = new IntegerSetting("Speed", "Speed", "Speed", ()->true, 1, 10, 1);

    public Fly() {
        super("Fly", "Fly", ()->true, true);
        instance=this;
        settings.add(speed);
    }

    public static IToggleable getToggle() {
	  return instance.isEnabled();
    }
    
    @SubscribeEvent
    public void onEnable(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START)
        super.isEnabled();
        if (getToggle().getAsBoolean()) {
		    if(mc.world == null) return;

            float yaw = Minecraft.getMinecraft().player.rotationYaw;
            int dx = 0, dy = 0, dz = 0;

            if (mc.gameSettings.keyBindForward.isKeyDown())
                dz--;
            if (mc.gameSettings.keyBindBack.isKeyDown())
                dz++;

            if (mc.gameSettings.keyBindLeft.isKeyDown())
                dx--;
            if (mc.gameSettings.keyBindRight.isKeyDown())
                dx++;

            if (mc.gameSettings.keyBindJump.isKeyDown())
                dy++;
            if (mc.gameSettings.keyBindSneak.isKeyDown())
                dy--;

            double tempSpeed = speed.getValue() * 2;

            // FORWARD & BACKWARD MOVEMENT
            Minecraft.getMinecraft().player.motionX = tempSpeed * dz * Math.sin(Math.toRadians(yaw));
            Minecraft.getMinecraft().player.motionZ = tempSpeed * dz * -Math.cos(Math.toRadians(yaw));
            // LEFT & RIGHT MOVEMENT
            Minecraft.getMinecraft().player.motionX += tempSpeed * dx * -Math.cos(Math.toRadians(yaw));
            Minecraft.getMinecraft().player.motionZ += tempSpeed * dx * -Math.sin(Math.toRadians(yaw));
            // UP & DOWN MOVEMENT
            Minecraft.getMinecraft().player.motionY = tempSpeed * dy;
        }

    }
}
