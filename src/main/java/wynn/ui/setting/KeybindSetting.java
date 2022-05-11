package wynn.ui.setting;

import org.lwjgl.input.Keyboard;

import wynn.ui.base.base.IBoolean;
import wynn.ui.base.setting.IKeybindSetting;

public class KeybindSetting extends Setting<Integer> implements IKeybindSetting {
	public KeybindSetting (String displayName, String configName, String description, IBoolean visible, Integer value) {
		super(displayName,configName,description,visible,value);
	}

	@Override
	public int getKey() {
		return getValue();
	}

	@Override
	public void setKey (int key) {
		setValue(key);
	}

	@Override
	public String getKeyName() {
		return Keyboard.getKeyName(getKey());
	}
}
