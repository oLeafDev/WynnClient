package wynn.ui.base.component;

import java.awt.Point;
import java.awt.Rectangle;

import wynn.ui.base.base.IInterface;
import wynn.ui.base.config.IPanelConfig;
import wynn.ui.base.popup.IPopupPositioner;

/**
 * Combination of {@link IComponentProxy} and {@link IFixedComponent}.
 * @author lukflug
 * @param <T> the component type
 */
@FunctionalInterface
public interface IFixedComponentProxy<T extends IFixedComponent> extends IComponentProxy<T>,IFixedComponent {
	@Override
	public default Point getPosition (IInterface inter) {
		return getComponent().getPosition(inter);
	}
	
	@Override
	public default void setPosition (IInterface inter, Point position) {
		getComponent().setPosition(inter,position);
	}
	
	@Override
	public default void setPosition (IInterface inter, Rectangle component, Rectangle panel, IPopupPositioner positioner) {
		getComponent().setPosition(inter,component,panel,positioner);
	}
	
	@Override
	public default int getWidth (IInterface inter) {
		return getComponent().getWidth(inter);
	}
	
	@Override
	public default boolean savesState() {
		return getComponent().savesState();
	}
	
	@Override
	public default void saveConfig (IInterface inter, IPanelConfig config) {
		getComponent().saveConfig(inter,config);
	}
	
	@Override
	public default void loadConfig (IInterface inter, IPanelConfig config) {
		getComponent().loadConfig(inter,config);
	}
	
	@Override
	public default String getConfigName() {
		return getComponent().getConfigName();
	}
}
