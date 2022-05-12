package wynn.modules.movement;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wynn.modules.Module;
import wynn.ui.base.base.IToggleable;
import wynn.ui.setting.EnumSetting;

import static wynn.modules.movement.Fly.mc;

public class Sprint extends Module {

    public static Sprint instance;

    public final EnumSetting<SprintMode> mode = new EnumSetting<>("Mode", "mode", "Mode of the sprinting", ()->true, SprintMode.LEGIT, SprintMode.class);

    public Sprint() {
        super("Sprint", "Automatically Sprints.", ()->true, true);
        settings.add(mode);
        instance = this;
    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }

    public enum SprintMode {
        RAGE,LEGIT
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(getToggle().get()) {
            if(mc.world != null) {
                if (mode.getValue() == SprintMode.LEGIT) {
                    if (mc.player.movementInput.moveForward > 0) {
                        mc.player.setSprinting(true);

                    }
                } else if(mode.getValue() == SprintMode.RAGE) {
                    if (mc.player.movementInput.moveForward < 0) {
                        mc.player.setSprinting(true);
                    }
                }
            }
        }
    }


}
