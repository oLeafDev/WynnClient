package wynn.ui.base.widget;

import wynn.ui.base.base.Context;
import wynn.ui.base.base.IInterface;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.HorizontalComponent;
import wynn.ui.base.container.HorizontalContainer;
import wynn.ui.base.container.VerticalContainer;
import wynn.ui.base.setting.INumberSetting;
import wynn.ui.base.setting.IStringSetting;
import wynn.ui.base.setting.Labeled;
import wynn.ui.base.theme.IContainerRenderer;
import wynn.ui.base.theme.ITheme;
import wynn.ui.base.theme.ThemeTuple;

/**
 * A spinner for fine tuning numerical settings.
 * @author lukflug
 */
public class Spinner extends HorizontalContainer {
	/**
	 * Constructor.
	 * @param setting the number setting to be used
	 * @param theme the theme to be used
	 * @param container whether this is a title bar
	 * @param allowInput whether text input is allowed
	 * @param keys the keyboard predicates for the text box
	 */
	public Spinner (INumberSetting setting, ThemeTuple theme, boolean container, boolean allowInput, ITextFieldKeys keys) {
		super(setting,new IContainerRenderer(){});
		TextField textField=new TextField(new IStringSetting() {
			private String value=null;
			private long lastTime;
			
			@Override
			public String getDisplayName() {
				return setting.getDisplayName();
			}

			@Override
			public String getValue() {
				if (value!=null && System.currentTimeMillis()-lastTime>500) {
					if (value.isEmpty()) value="0";
					if (value.endsWith(".")) value+='0';
					double number=Double.parseDouble(value);
					if (number>setting.getMaximumValue()) number=setting.getMaximumValue();
					else if (number<setting.getMinimumValue()) number=setting.getMinimumValue();
					setting.setNumber(number);
					value=null;
				}
				if (value==null) return setting.getSettingState();
				else return value;
			}

			@Override
			public void setValue (String string) {
				if (value==null) lastTime=System.currentTimeMillis();
				value=new String(string);
			}
		},keys,0,new SimpleToggleable(false),theme.getTextRenderer(true,container)) {
			@Override
			public boolean allowCharacter(char character) {
				if (!allowInput) return false;
				return (character>='0' && character<='9') || (character=='.'&&!setting.getSettingState().contains("."));
			}
		};
		addComponent(new HorizontalComponent<>(textField,0,1));
		VerticalContainer buttons=new VerticalContainer(setting,new IContainerRenderer(){});
		buttons.addComponent(new Button<Void>(new Labeled(null,null,()->true),()->null,theme.getSmallButtonRenderer(ITheme.UP,container)) {
			@Override
			public void handleButton (Context context, int button) {
				super.handleButton(context,button);
				if (button==IInterface.LBUTTON && context.isClicked(button)) {
					double number=setting.getNumber();
					number+=Math.pow(10,-setting.getPrecision());
					if (number<=setting.getMaximumValue()) setting.setNumber(number);
				}
			}
			
			@Override
			public int getHeight() {
				return textField.getHeight()/2;
			}
		});
		buttons.addComponent(new Button<Void>(new Labeled(null,null,()->true),()->null,theme.getSmallButtonRenderer(ITheme.DOWN,container)) {
			@Override
			public void handleButton (Context context, int button) {
				super.handleButton(context,button);
				if (button==IInterface.LBUTTON && context.isClicked(button)) {
					double number=setting.getNumber();
					number-=Math.pow(10,-setting.getPrecision());
					if (number>=setting.getMinimumValue()) setting.setNumber(number);
				}
			}
			
			@Override
			public int getHeight() {
				return textField.getHeight()/2;
			}
		});
		addComponent(new HorizontalComponent<VerticalContainer>(buttons,textField.getHeight(),0) {
			@Override
			public int getWidth (IInterface inter) {
				return textField.getHeight();
			}
		});
	}
}
