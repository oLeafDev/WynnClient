package wynn.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import wynn.modules.Module;
import wynn.ui.base.base.IToggleable;

public class TriggerBot extends Module {

    private static TriggerBot instance;
    public static Minecraft mc = Minecraft.getMinecraft();

    public TriggerBot() {
        super("TriggerBot", "Does Not Work", ()->true, true);
        instance = this;

    }

    public static IToggleable getToggle() {
        return instance.isEnabled();
    }


    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!getToggle().get()) return;
        if (event.phase == TickEvent.Phase.START) {
            if (mc.player != null) {
                if(mc.world != null) {
                    if (mc.objectMouseOver != null) {
                        if (mc.objectMouseOver.entityHit != null) {
                            if (mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer) {
                                if (!mc.player.getName().equals(mc.objectMouseOver.entityHit.getName())) {
                                    if (mc.player.getDistance(mc.objectMouseOver.entityHit) < 5) {
                                        mc.player.attackTargetEntityWithCurrentItem(mc.objectMouseOver.entityHit);
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
