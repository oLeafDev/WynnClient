package wynn.ui.base.widget;

import wynn.ui.base.base.Context;
import wynn.ui.base.base.IInterface;
import wynn.ui.base.base.IToggleable;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.FocusableComponent;
import wynn.ui.base.setting.IBooleanSetting;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.theme.ISwitchRenderer;

/**
 * Switch widget that can control a toggleable.
 * @author lukflug
 */
public class ToggleSwitch extends FocusableComponent {
	/**
	 * Setting to be toggled by left click.
	 */
	protected IToggleable toggle;
	/**
	 * Renderer for this component.
	 */
	protected ISwitchRenderer<Boolean> renderer;
	
	/**
	 * Constructor.
	 * @param label the label for the component
	 * @param toggle the toggle
	 * @param renderer the renderer for this component
	 */
	public ToggleSwitch (ILabeled label, IToggleable toggle, ISwitchRenderer<Boolean> renderer) {
		super(label);
		this.toggle=toggle;
		this.renderer=renderer;
		if (this.toggle==null) this.toggle=new SimpleToggleable(false);
	}
	
	/**
	 * Constructor using boolean setting.
	 * @param setting the setting in question
	 * @param renderer the renderer for this component
	 */
	public ToggleSwitch (IBooleanSetting setting, ISwitchRenderer<Boolean> renderer) {
		this(setting,setting,renderer);
	}
	
	@Override
	public void render (Context context) {
		super.render(context);
		renderer.renderButton(context,getTitle(),hasFocus(context),toggle.isOn());
	}
	
	@Override
	public void handleButton (Context context, int button) {
		super.handleButton(context,button);
		if (button==IInterface.LBUTTON && context.isClicked(button)) {
			if (renderer.getOnField(context).contains(context.getInterface().getMouse()) && !toggle.isOn()) toggle.toggle();
			else if (renderer.getOffField(context).contains(context.getInterface().getMouse()) && toggle.isOn()) toggle.toggle();
		}
	}

	@Override
	protected int getHeight() {
		return renderer.getDefaultHeight();
	}
}
