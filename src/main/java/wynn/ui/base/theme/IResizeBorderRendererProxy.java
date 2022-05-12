package wynn.ui.base.theme;

import wynn.ui.base.base.Context;

/**
 * Proxy redirecting calls
 * @author lukflug
 */
@FunctionalInterface
public interface IResizeBorderRendererProxy extends IResizeBorderRenderer {
	@Override
	public default void drawBorder (Context context, boolean focus) {
		getRenderer().drawBorder(context,focus);
	}
	
	@Override
	public default int getBorder() {
		return getRenderer().getBorder();
	}
	
	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
	public IResizeBorderRenderer getRenderer();
}
