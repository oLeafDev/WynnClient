package wynn.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import wynn.modules.Category;
import wynn.modules.combat.TriggerBot;
import wynn.modules.movement.Fly;
import wynn.modules.other.ClickGUIModule;
import wynn.modules.other.HUDEditorModule;
import wynn.modules.render.LogoModule;
import wynn.modules.render.WatermarkModule;
import wynn.ui.ClickGUI;



@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "wynnclient";
    public static final String NAME = "WynnClient";
    public static final String VERSION = "1.0.0";
    public static Logger logger;
    private static ClickGUI gui;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Display.setTitle("Wynn Client | v1.0 | Username: " + Minecraft.getMinecraft().getSession().getUsername() + " | Made By Leaf");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new TriggerBot());
        MinecraftForge.EVENT_BUS.register(new Fly());
        Category.init();
        Category.OTHER.modules.add(new ClickGUIModule());
        Category.OTHER.modules.add(new HUDEditorModule());
	  Category.MOVEMENT.modules.add(new Fly());
	  Category.COMBAT.modules.add(new TriggerBot());
        Category.HUD.modules.add(new WatermarkModule());
        Category.HUD.modules.add(new LogoModule());
        gui=new ClickGUI();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender (RenderGameOverlayEvent.Post event) {
        if (event.getType()==RenderGameOverlayEvent.ElementType.HOTBAR) gui.render();
    }

    @SubscribeEvent
    public void onKeyInput (InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(ClickGUIModule.keybind.getKey())) gui.enterGUI();
        if (Keyboard.isKeyDown(HUDEditorModule.keybind.getKey())) gui.enterHUDEditor();
        if (Keyboard.getEventKeyState()) gui.handleKeyEvent(Keyboard.getEventKey());
    }


}