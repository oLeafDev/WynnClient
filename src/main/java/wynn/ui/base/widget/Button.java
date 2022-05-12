package wynn.ui.base.widget;

import java.util.function.Supplier;

import wynn.ui.base.base.Context;
import wynn.ui.base.component.FocusableComponent;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.theme.IButtonRenderer;

/**
 * Button widget class.
 * @author lukflug
 */
public class Button<T> extends FocusableComponent {
	/**
	 * The button state supplier.
	 */
	protected Supplier<T> state;
	/**
	 * Renderer for this component.
	 */
	protected IButtonRenderer<T> renderer;

	/**
	 * Constructor.
	 * @param label the label for the component
	 * @param state the button state supplier
	 * @param renderer the renderer for this component
	 */
	public Button (ILabeled label, Supplier<T> state, IButtonRenderer<T> renderer) {
		super(label);
		this.renderer=renderer;
		this.state=state;
	}
	
	@Override
	public void render (Context context) {
		super.render(context);
		renderer.renderButton(context,getTitle(),hasFocus(context),state.get());
	}

	@Override
	protected int getHeight() {
		return renderer.getDefaultHeight();
	}
}
