package wynn.ui.base.layout;

import java.awt.Point;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import wynn.ui.base.base.AnimatedToggleable;
import wynn.ui.base.base.Animation;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.component.IResizable;
import wynn.ui.base.component.IScrollSize;
import wynn.ui.base.container.IContainer;
import wynn.ui.base.theme.RendererTuple;
import wynn.ui.base.theme.ThemeTuple;
import wynn.ui.base.widget.ResizableComponent;

/**
 * Component adder that adds components a simple panels.
 * @author lukflug
 */
public class PanelAdder implements IComponentAdder {
	/**
	 * The container to be used.
	 */
	protected IContainer<? super IFixedComponent> container;
	/**
	 * Whether panels should be initialized as open.
	 */
	protected boolean open;
	/**
	 * Global visibility predicate.
	 */
	protected IBoolean isVisible;
	/**
	 * Map from display name to config name.
	 */
	protected UnaryOperator<String> configName;
	
	/**
	 * Constructor.
	 * @param container the container to be used
	 * @param open whether panels should be initialized as open
	 * @param isVisible global visibility predicate
	 * @param configName map from display name to config name
	 */
	public PanelAdder (IContainer<? super IFixedComponent> container, boolean open, IBoolean isVisible, UnaryOperator<String> configName) {
		this.container=container;
		this.open=open;
		this.isVisible=isVisible;
		this.configName=configName;
	}
	
	@Override
	public <S extends IComponent,T extends IComponent> void addComponent (S title, T content, ThemeTuple theme, Point position, int width, Supplier<Animation> animation) {
		AnimatedToggleable toggle=new AnimatedToggleable(new SimpleToggleable(open),animation.get());
		RendererTuple<Void> renderer=new RendererTuple<Void>(Void.class,theme);
		IResizable size=getResizable(width);
		container.addComponent(ResizableComponent.createResizableComponent(title,content,()->null,toggle,renderer,theme.theme.getResizeRenderer(),size,getScrollSize(size),position,width,true,configName.apply(content.getTitle())),isVisible);
	}

	@Override
	public void addPopup (IFixedComponent popup) {
		container.addComponent(popup,isVisible);
	}
	
	/**
	 * Panel resize behavior.
	 * @param width panel width
	 * @return resize behavior, null for non-resizable panels
	 */
	protected IResizable getResizable (int width) {
		return null;
	}
	
	/**
	 * Panel scroll behavior.
	 * @param size panel resize behavior
	 * @return the scroll behavior
	 */
	protected IScrollSize getScrollSize (IResizable size) {
		return new IScrollSize(){};
	}
}
