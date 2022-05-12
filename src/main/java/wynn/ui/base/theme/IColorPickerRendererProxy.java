package wynn.ui.base.theme;

import java.awt.Color;
import java.awt.Point;

import wynn.ui.base.base.Context;

/**
 * Proxy redirecting calls
 * @author lukflug
 */
@FunctionalInterface
public interface IColorPickerRendererProxy extends IColorPickerRenderer {
	@Override
	public default void renderPicker (Context context, boolean focus, Color color) {
		getRenderer().renderPicker(context,focus,color);
	}
	
	@Override
	public default Color transformPoint (Context context, Color color, Point point) {
		return getRenderer().transformPoint(context,color,point);
	}
	
	@Override
	public default int getDefaultHeight (int width) {
		return getRenderer().getDefaultHeight(width);
	}
	
	/**
	 * The renderer to be redirected to.
	 * @return the renderer
	 */
	public IColorPickerRenderer getRenderer();
}
