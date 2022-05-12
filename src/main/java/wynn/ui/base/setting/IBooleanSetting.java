package wynn.ui.base.setting;

import wynn.ui.base.base.IToggleable;

/**
 * Interface representing boolean setting.
 * @author lukflug
 */
public interface IBooleanSetting extends ISetting<Boolean>,IToggleable {
	@Override
	public default Boolean getSettingState() {
		return isOn();
	}
	
	@Override
	public default Class<Boolean> getSettingClass() {
		return Boolean.class;
	}
}
