package wynn.ui.setting;

import java.awt.Color;

import wynn.modules.other.ClickGUIModule;
import wynn.modules.other.ClickGUIModule.ColorModel;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.setting.IColorSetting;
import wynn.ui.base.theme.ITheme;

public class ColorSetting extends Setting<Color> implements IColorSetting {
	public final boolean hasAlpha,allowsRainbow;
	private boolean rainbow;
	
	public ColorSetting (String displayName, String configName, String description, IBoolean visible, boolean hasAlpha, boolean allowsRainbow, Color value, boolean rainbow) {
		super(displayName,configName,description,visible,value);
		this.hasAlpha=hasAlpha;
		this.allowsRainbow=allowsRainbow;
		this.rainbow=rainbow;
	}
	
	@Override
	public Color getValue() {
		if (rainbow) {
			int speed=ClickGUIModule.rainbowSpeed.getValue();
			return ITheme.combineColors(Color.getHSBColor((System.currentTimeMillis()%(360*speed))/(float)(360*speed),1,1),super.getValue());
		}
		else return super.getValue();
	}

	@Override
	public Color getColor() {
		return super.getValue();
	}

	@Override
	public boolean getRainbow() {
		return rainbow;
	}

	@Override
	public void setRainbow (boolean rainbow) {
		this.rainbow=rainbow;
	}
	
	@Override
	public boolean hasAlpha() {
		return hasAlpha;
	}
	
	@Override
	public boolean allowsRainbow() {
		return allowsRainbow;
	}
	
	@Override
	public boolean hasHSBModel() {
		return ClickGUIModule.colorModel.getValue()==ColorModel.HSB;
	}
}
