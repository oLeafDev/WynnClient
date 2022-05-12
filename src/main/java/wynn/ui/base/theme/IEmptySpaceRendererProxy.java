package wynn.ui.base.theme;

import wynn.ui.base.base.Context;

/**
 * Proxy redirecting calls.
 * @author lukflug
 * @param <T> type representing state of scroll bar
 */
@FunctionalInterface
public interface IEmptySpaceRendererProxy<T> extends IEmptySpaceRenderer<T> {
	@Override
	public default void renderSpace (Context context, boolean focus, T state) {
		getRenderer().renderSpace(context,focus,state);
	}

	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
	public IEmptySpaceRenderer<T> getRenderer();
}
