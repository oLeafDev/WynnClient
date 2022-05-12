package wynn.ui.setting;

import wynn.ui.base.base.IBoolean;
import wynn.ui.base.setting.IStringSetting;

public class StringSetting extends Setting<String> implements IStringSetting {
	public StringSetting (String displayName, String configName, String description, IBoolean visible, String value) {
		super(displayName,configName,description,visible,value);
	}
}
