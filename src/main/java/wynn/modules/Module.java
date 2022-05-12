package wynn.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import wynn.ui.setting.Setting;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.base.IToggleable;
import wynn.ui.base.setting.IModule;
import wynn.ui.base.setting.ISetting;

public class Module implements IModule {
	public final String displayName,description;
	public final IBoolean visible;
	public final List<Setting<?>> settings=new ArrayList<Setting<?>>();
	public final boolean toggleable;
	private boolean enabled=false;
	
	public Module (String displayName, String description, IBoolean visible, boolean toggleable) {
		this.displayName=displayName;
		this.description=description;
		this.visible=visible;
		this.toggleable=toggleable;
	}
	
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public IBoolean isVisible() {
		return visible;
	}

	@Override
	public IToggleable isEnabled() {
		if (!toggleable) return null;
		return new IToggleable() {
			@Override
			public boolean isOn() {
				return enabled;
			}

			@Override
			public void toggle() {
				enabled=!enabled;
			}
		};
	}

	@Override
	public Stream<ISetting<?>> getSettings() {
		return settings.stream().filter(setting->setting instanceof ISetting).sorted((a,b)->a.displayName.compareTo(b.displayName)).map(setting->(ISetting<?>)setting);
	}

}
