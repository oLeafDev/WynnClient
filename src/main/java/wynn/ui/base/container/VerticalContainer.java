package wynn.ui.base.container;

import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;

import wynn.ui.base.base.Context;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.setting.ILabeled;
import wynn.ui.base.theme.IContainerRenderer;

/**
 * Container with contents arranged vertically.
 * @author lukflug
 */
public class VerticalContainer extends Container<IComponent> {
	/**
	 * Constructor.
	 * @param label the label for the component
	 * @param renderer the renderer for this container
	 */
	public VerticalContainer (ILabeled label, IContainerRenderer renderer) {
		super(label,renderer);
	}
	
	@Override
	protected void doContextSensitiveLoop (Context context, ContextSensitiveConsumer<IComponent> function) {
		AtomicInteger posy=new AtomicInteger(renderer.getTop());
		doContextlessLoop(component->{
			Context subContext=getSubContext(context,posy.get());
			function.accept(subContext,component);
			if (subContext.focusReleased()) context.releaseFocus();
			else if (subContext.foucsRequested()) context.requestFocus();
			posy.addAndGet(subContext.getSize().height+renderer.getBorder());
		});
		context.setHeight(posy.get()-renderer.getBorder()+renderer.getBottom());
	}
	
	/**
	 * Create sub-context for child component.
	 * @param context the current context
	 * @param posy the vertical position of the child component
	 * @return the context for the child component
	 */
	protected Context getSubContext (Context context, int posy) {
		return new Context(context,context.getSize().width-renderer.getLeft()-renderer.getRight(),new Point(renderer.getLeft(),posy),hasFocus(context),true);
	}
	
	protected boolean hasFocus (Context context) {
		return context.hasFocus();
	}
}
