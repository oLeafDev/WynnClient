package wynn.ui.base.widget;

import wynn.ui.base.base.Animation;
import wynn.ui.base.base.Context;
import wynn.ui.base.base.IInterface;
import wynn.ui.base.component.FocusableComponent;
import wynn.ui.base.setting.AnimatedEnum;
import wynn.ui.base.setting.IEnumSetting;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.theme.IRadioRenderer;

/**
 * Widget for radio button list.
 * @author lukflug
 */
public abstract class RadioButton extends FocusableComponent {
	/**
	 * The enum setting to be used.
	 */
	protected IEnumSetting setting;
	/**
	 * The radio renderer to be used.
	 */
	protected IRadioRenderer renderer;
	/**
	 * The animation for state transitions.
	 */
	protected AnimatedEnum animation;
	/**
	 * Whether list is horizontal.
	 */
	protected final boolean horizontal;

	/**
	 * Constructor.
	 * @param setting the enum setting to be used
	 * @param renderer the radio renderer to be used
	 * @param animation the animation for state transitions
	 * @param horizontal whether list is horizontal
	 */
	public RadioButton (IEnumSetting setting, IRadioRenderer renderer, Animation animation, boolean horizontal) {
		super(setting);
		this.setting=setting;
		this.renderer=renderer;
		this.animation=new AnimatedEnum(setting,animation);
		this.horizontal=horizontal;
	}
	
	@Override
	public void render (Context context) {
		super.render(context);
		ILabeled[] values=IEnumSetting.getVisibleValues(setting);
		String compare=setting.getValueName();
		int value=-1;
		for (int i=0;i<values.length;i++) {
			if (values[i].getDisplayName().equals(compare)) {
				value=i;
				break;
			}
		}
		renderer.renderItem(context,values,hasFocus(context),value,animation.getValue(),horizontal);
	}
	
	@Override
	public void handleButton (Context context, int button) {
		super.handleButton(context,button);
		if (button==IInterface.LBUTTON && context.isClicked(button)) {
			int index=0;
			ILabeled[] values=setting.getAllowedValues();
			ILabeled[] visibleValues=IEnumSetting.getVisibleValues(setting);
			for (int i=0;i<values.length;i++) {
				if (values[i].isVisible().isOn()) {
					if (renderer.getItemRect(context,visibleValues,index,horizontal).contains(context.getInterface().getMouse())) {
						setting.setValueIndex(i);
						return;
					}
					index++;
				}
			}
		}
	}
	
	@Override
	public void handleKey (Context context, int key) {
		super.handleKey(context,key);
		if (context.hasFocus()) {
			if (isUpKey(key)) setting.decrement();
			else if (isDownKey(key)) setting.increment();
		}
	}

	@Override
	protected int getHeight() {
		return renderer.getDefaultHeight(IEnumSetting.getVisibleValues(setting),horizontal);
	}
	
	/**
	 * Predicate for up key.
	 * @param key key scancode
	 * @return whether key is up key
	 */
	protected abstract boolean isUpKey (int key);
	
	/**
	 * Predicate for down key.
	 * @param key key scancode
	 * @return whether key is down key
	 */
	protected abstract boolean isDownKey (int key);
}
