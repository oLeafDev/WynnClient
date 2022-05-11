package wynn.ui.base.layout;

import java.awt.Point;
import java.util.function.Supplier;

import wynn.ui.base.base.AnimatedToggleable;
import wynn.ui.base.base.Animation;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.component.IResizable;
import wynn.ui.base.component.IScrollSize;
import wynn.ui.base.container.IContainer;
import wynn.ui.base.container.VerticalContainer;
import wynn.ui.base.layout.ChildUtil.ChildMode;
import wynn.ui.base.popup.IPopupPositioner;
import wynn.ui.base.popup.PopupTuple;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.setting.Labeled;
import wynn.ui.base.theme.ITheme;
import wynn.ui.base.theme.RendererTuple;
import wynn.ui.base.theme.ThemeTuple;
import wynn.ui.base.widget.Button;
import wynn.ui.base.widget.ResizableComponent;

/**
 * Component adder that stacks component on top of each other in a single panel.
 * @author lukflug
 */
public class StackedPanelAdder implements IComponentAdder,IScrollSize {
	/**
	 * The container to be used.
	 */
	protected IContainer<? super IFixedComponent> container;
	/**
	 * The way the components should be added.
	 */
	protected ChildMode mode;
	/**
	 * The content container containing the components.
	 */
	protected VerticalContainer content;
	/**
	 * The {@link ChildUtil} instance.
	 */
	protected ChildUtil util;
	/**
	 * The global visibility predicate.
	 */
	protected IBoolean isVisible;
	
	/**
	 * Constructor.
	 * @param container the container to be used
	 * @param label the label for the frame
	 * @param theme the theme to be used
	 * @param position the initial position of the frame
	 * @param width the initial width of the frame
	 * @param animation the animation to be used
	 * @param mode the way the components should be added
	 * @param popupPos the pop-up positioner to be used
	 * @param isVisible the global visibility predicate
	 * @param configName the config name of the frame
	 */
	public StackedPanelAdder (IContainer<? super IFixedComponent> container, ILabeled label, ITheme theme, Point position, int width, Supplier<Animation> animation, ChildMode mode, IPopupPositioner popupPos, IBoolean isVisible, String configName) {
		this.container=container;
		this.mode=mode;
		this.isVisible=isVisible;
		content=new VerticalContainer(label,theme.getContainerRenderer(-1,-1,true));
		IResizable size=getResizable(width);
		IScrollSize scrollSize=getScrollSize(size);
		container.addComponent(ResizableComponent.createResizableComponent(new Button<Void>(label,()->null,theme.getButtonRenderer(Void.class,-1,-1,true)),content,()->null,new AnimatedToggleable(new SimpleToggleable(true),animation.get()),new RendererTuple<Void>(Void.class,new ThemeTuple(theme,-1,-1)),theme.getResizeRenderer(),size,scrollSize,position,width,true,configName),isVisible);
		util=new ChildUtil(width,animation,new PopupTuple(popupPos,false,this));
	}
	
	@Override
	public <S extends IComponent,T extends IComponent> void addComponent (S title, T content, ThemeTuple theme, Point position, int width, Supplier<Animation> animation) {
		util.addContainer(new Labeled(content.getTitle(),null,()->content.isVisible()),title,content,()->null,Void.class,this.content,this,theme,mode);
	}

	@Override
	public void addPopup (IFixedComponent popup) {
		container.addComponent(popup,isVisible);
	}
	
	/**
	 * Frame resize behavior.
	 * @param width panel width
	 * @return resize behavior, null for non-resizable frame
	 */
	protected IResizable getResizable (int width) {
		return null;
	}
	
	/**
	 * Frame scroll behavior.
	 * @param size frame resize behavior
	 * @return the scroll behavior
	 */
	protected IScrollSize getScrollSize (IResizable size) {
		return new IScrollSize(){};
	}
}
