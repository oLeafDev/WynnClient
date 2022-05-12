package wynn.ui.base.widget;

import wynn.ui.base.component.IComponent;
import wynn.ui.base.setting.IBooleanSetting;
import wynn.ui.base.setting.IColorSetting;
import wynn.ui.base.setting.INumberSetting;
import wynn.ui.base.theme.ThemeTuple;

/**
 * Returns color component consisting of sliders.
 * @author lukflug
 */
public class ColorSliderComponent extends ColorComponent {
	/**
	 * Constructor.
	 * @param setting the setting in question
	 * @param theme the theme to be used
	 */
	public ColorSliderComponent (IColorSetting setting, ThemeTuple theme) {
		super(setting, theme);
	}

	@Override
	public void populate (ThemeTuple theme) {
		addComponent(getRainbowComponent(theme,new RainbowToggle()));
		addComponent(getColorComponent(theme,0,new ColorNumber(0,()->setting.hasHSBModel())));
		addComponent(getColorComponent(theme,1,new ColorNumber(1,()->setting.hasHSBModel())));
		addComponent(getColorComponent(theme,2,new ColorNumber(2,()->setting.hasHSBModel())));
		addComponent(getColorComponent(theme,3,new ColorNumber(3,()->setting.hasHSBModel())));
	}
	
	/**
	 * Returns the component for the rainbow setting.
	 * @param theme the theme to be used
	 * @param toggle the boolean setting to be used
	 * @return the component
	 */
	public IComponent getRainbowComponent (ThemeTuple theme, IBooleanSetting toggle) {
		return new ToggleButton(toggle,theme.getButtonRenderer(Boolean.class,false));
	}
	
	/**
	 * Returns the component for the color component setting.
	 * @param theme the theme to be used
	 * @param value the index of the component 
	 * @param number the number setting to be used
	 * @return the component
	 */
	public IComponent getColorComponent (ThemeTuple theme, int value, INumberSetting number) {
		return new NumberSlider(number,theme.getSliderRenderer(false));
	}
}
