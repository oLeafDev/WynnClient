package wynn.ui.base.layout;

import java.awt.Point;
import java.util.function.Supplier;

import wynn.ui.base.base.AnimatedToggleable;
import wynn.ui.base.base.Animation;
import wynn.ui.base.base.Context;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.HorizontalComponent;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.component.IResizable;
import wynn.ui.base.component.IScrollSize;
import wynn.ui.base.container.HorizontalContainer;
import wynn.ui.base.container.IContainer;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.theme.ITheme;
import wynn.ui.base.theme.RendererTuple;
import wynn.ui.base.theme.ThemeTuple;
import wynn.ui.base.widget.ResizableComponent;
import wynn.ui.base.widget.ScrollBarComponent;

/**
 * Component adder that adds panels side-by-side in a single frame.
 * @author lukflug
 */
public class SinglePanelAdder implements IComponentAdder {
	/**
	 * The container to be used.
	 */
	protected IContainer<? super IFixedComponent> container;
	/**
	 * Global visibility predicate.
	 */
	protected IBoolean isVisible;
	/**
	 * Title bar container.
	 */
	protected HorizontalContainer title;
	/**
	 * Content container.
	 */
	protected HorizontalContainer content;
	/**
	 * The scroll behavior of the frame.
	 */
	protected final IScrollSize size;
	
	/**
	 * Constructor.
	 * @param container the container to be used
	 * @param label the label for the frame
	 * @param theme the theme to be used
	 * @param position the initial position of the frame
	 * @param width the initial width of the frame
	 * @param animation the animation to be used
	 * @param isVisible the global visibility predicate
	 * @param configName the config name of the frame
	 */
	public SinglePanelAdder (IContainer<? super IFixedComponent> container, ILabeled label, ITheme theme, Point position, int width, Supplier<Animation> animation, IBoolean isVisible, String configName) {
		this.container=container;
		this.isVisible=isVisible;
		title=new HorizontalContainer(label,theme.getContainerRenderer(-1,-1,true));
		content=new HorizontalContainer(label,theme.getContainerRenderer(-1,-1,true));
		AnimatedToggleable toggle=new AnimatedToggleable(new SimpleToggleable(true),animation.get());
		RendererTuple<Void> renderer=new RendererTuple<Void>(Void.class,new ThemeTuple(theme,-1,-1));
		IResizable size=getResizable(width);
		this.size=getScrollSize(size);
		container.addComponent(ResizableComponent.createResizableComponent(title,content,()->null,toggle,renderer,theme.getResizeRenderer(),size,new IScrollSize() {
			@Override
			public int getComponentWidth(Context context) {
				return SinglePanelAdder.this.size.getComponentWidth(context);
			}
		},position,width,true,configName),isVisible);
	}
	
	@Override
	public <S extends IComponent,T extends IComponent> void addComponent (S title, T content, ThemeTuple theme, Point position, int width, Supplier<Animation> animation) {
		this.title.addComponent(new HorizontalComponent<S>(title,0,1));
		this.content.addComponent(new HorizontalComponent<>(new ScrollBarComponent<Void,T>(content,theme.getScrollBarRenderer(Void.class),theme.getEmptySpaceRenderer(Void.class,false),theme.getEmptySpaceRenderer(Void.class,true)) {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return size.getScrollHeight(context,componentHeight);
			}

			@Override
			protected Void getState() {
				return null;
			}
		},0,1));
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
