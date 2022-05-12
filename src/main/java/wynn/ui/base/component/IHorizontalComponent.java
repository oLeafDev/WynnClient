package wynn.ui.base.component;

import wynn.ui.base.base.IInterface;

/**
 * Component to be included in horizontal containers.
 * @author lukflug
 */
public interface IHorizontalComponent extends IComponent {
	/**
	 * Get the component width.
	 * @param inter current interface
	 * @return component width
	 */
	public int getWidth (IInterface inter);
	
	/**
	 * Get the component weight.
	 * @return component weight
	 */
	public int getWeight();
}
