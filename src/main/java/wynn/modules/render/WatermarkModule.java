package wynn.modules.render;

import java.awt.Color;
import java.awt.Point;

import wynn.ui.setting.BooleanSetting;
import wynn.ui.setting.ColorSetting;
import wynn.ui.setting.StringSetting;
import wynn.ui.base.base.IToggleable;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.hud.HUDList;
import wynn.ui.base.hud.ListComponent;
import wynn.modules.Module;

public class WatermarkModule extends Module {
	private static WatermarkModule instance;
	private static final ColorSetting color=new ColorSetting("Text Color","color","The color of the displayed text.",()->true,false,true,new Color(255, 0, 0),false);
	
	public WatermarkModule() {
		super("Watermark","Module that displays text on HUD.",()->true,true);
		instance=this;
		settings.add(color);
	}

	public static IFixedComponent getComponent() {
		return new ListComponent(()->"Watermark",new Point(300,10),"watermark",new HUDList() {
			@Override
			public int getSize() {
				return 1;
			}

			@Override
			public String getItem(int index) {
				if (index==0) return "WynnClient v1.0";
				else return "WynnClient v1.0";
			}

			@Override
			public Color getItemColor(int index) {
				return color.getValue();
			}

			@Override
			public boolean sortUp() {
				return false;
			}

			@Override
			public boolean sortRight() {
				return false;
			}

		},9,2);
	}
	
	public static IToggleable getToggle() {
		return instance.isEnabled();
	}
}
