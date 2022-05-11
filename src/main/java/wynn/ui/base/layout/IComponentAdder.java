package wynn.ui.base.layout;

import java.awt.Point;
import java.util.function.Supplier;

import wynn.ui.base.base.Animation;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.theme.ThemeTuple;

/**
 * Interface representing thing that can have panels added to it. 
 * @author lukflug
 */
public interface IComponentAdder {
	/**
	 * Add a panel.
	 * @param <S> the title component type
	 * @param <T> the content component type
	 * @param title the title component
	 * @param content the content component
	 * @param theme the theme to be used
	 * @param position the initial position
	 * @param width the panel width
	 * @param animation the animation supplier
	 */
	public <S extends IComponent,T extends IComponent> void addComponent (S title, T content, ThemeTuple theme, Point position, int width, Supplier<Animation> animation);
	
	/**
	 * Add a pop-up.
	 * @param popup the pop-up to be added
	 */
	public void addPopup (IFixedComponent popup);
}
