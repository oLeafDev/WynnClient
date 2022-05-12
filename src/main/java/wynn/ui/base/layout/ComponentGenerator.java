package wynn.ui.base.layout;

import java.util.function.IntPredicate;
import java.util.function.Supplier;

import wynn.ui.base.base.Animation;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.setting.IKeybindSetting;
import wynn.ui.base.setting.IStringSetting;
import wynn.ui.base.theme.ThemeTuple;
import wynn.ui.base.widget.ITextFieldKeys;
import wynn.ui.base.widget.KeybindComponent;
import wynn.ui.base.widget.TextField;

/**
 * Default implementation of the component generator.
 * @author lukflug
 */
public class ComponentGenerator implements IComponentGenerator {
	/**
	 * The scancode predicate for delete keybind key.
	 */
	protected final IntPredicate keybindKey;
	/**
	 * The text field character filter predicate. 
	 */
	protected final IntPredicate charFilter;
	/**
	 * The text field function key predicates.
	 */
	protected final ITextFieldKeys keys;
	
	/**
	 * Constructor.
	 * @param keybindKey the scancode predicate for delete keybind key
	 * @param charFilter the text field character filter predicate
	 * @param keys the text field function key predicates
	 */
	public ComponentGenerator (IntPredicate keybindKey, IntPredicate charFilter, ITextFieldKeys keys) {
		this.keybindKey=keybindKey;
		this.charFilter=charFilter;
		this.keys=keys;
	}
	
	@Override
	public IComponent getKeybindComponent (IKeybindSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
		return new KeybindComponent(setting,theme.getKeybindRenderer(isContainer)) {
			@Override
			public int transformKey (int scancode) {
				return keybindKey.test(scancode)?0:scancode;
			}
		};
	}
	
	@Override
	public IComponent getStringComponent (IStringSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
		return new TextField(setting,keys,0,new SimpleToggleable(false),theme.getTextRenderer(false,isContainer)) {
			@Override
			public boolean allowCharacter(char character) {
				return charFilter.test(character);
			}
		};
	}
}
