package wynn.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

import org.lwjgl.input.Keyboard;

import wynn.modules.Category;
import wynn.modules.other.ClickGUIModule;
import wynn.modules.other.ClickGUIModule.Theme;
import wynn.modules.other.HUDEditorModule;
import wynn.modules.render.LogoModule;
import wynn.modules.render.WatermarkModule;
import wynn.ui.setting.BooleanSetting;
import wynn.ui.setting.ColorSetting;
import wynn.ui.setting.IntegerSetting;
import wynn.ui.base.base.Animation;
import wynn.ui.base.base.Context;
import wynn.ui.base.base.IBoolean;
import wynn.ui.base.base.IInterface;
import wynn.ui.base.base.IToggleable;
import wynn.ui.base.base.SettingsAnimation;
import wynn.ui.base.base.SimpleToggleable;
import wynn.ui.base.component.IComponent;
import wynn.ui.base.component.IFixedComponent;
import wynn.ui.base.component.IResizable;
import wynn.ui.base.component.IScrollSize;
import wynn.ui.base.container.IContainer;
import wynn.ui.base.hud.HUDGUI;
import wynn.ui.base.layout.CSGOLayout;
import wynn.ui.base.layout.ChildUtil.ChildMode;
import wynn.ui.base.layout.ComponentGenerator;
import wynn.ui.base.layout.IComponentAdder;
import wynn.ui.base.layout.IComponentGenerator;
import wynn.ui.base.layout.ILayout;
import wynn.ui.base.layout.PanelAdder;
import wynn.ui.base.layout.PanelLayout;
import wynn.ui.base.layout.SearchableLayout;
import wynn.ui.base.layout.SinglePanelAdder;
import wynn.ui.base.layout.StackedPanelAdder;
import wynn.ui.base.mc12.MinecraftHUDGUI;
import wynn.ui.base.popup.CenteredPositioner;
import wynn.ui.base.popup.IPopupPositioner;
import wynn.ui.base.popup.MousePositioner;
import wynn.ui.base.popup.PanelPositioner;
import wynn.ui.base.popup.PopupTuple;
import wynn.ui.base.setting.IBooleanSetting;
import wynn.ui.base.setting.IClient;
import wynn.ui.base.setting.IColorSetting;
import wynn.ui.base.setting.IEnumSetting;
import wynn.ui.base.setting.INumberSetting;
import wynn.ui.base.setting.Labeled;
import wynn.ui.base.theme.ClearTheme;
import wynn.ui.base.theme.GameSenseTheme;
import wynn.ui.base.theme.IColorScheme;
import wynn.ui.base.theme.ITheme;
import wynn.ui.base.theme.IThemeMultiplexer;
import wynn.ui.base.theme.ImpactTheme;
import wynn.ui.base.theme.OptimizedTheme;
import wynn.ui.base.theme.RainbowTheme;
import wynn.ui.base.theme.ThemeTuple;
import wynn.ui.base.theme.Windows31Theme;
import wynn.ui.base.widget.ColorPickerComponent;
import wynn.ui.base.widget.CycleSwitch;
import wynn.ui.base.widget.DropDownList;
import wynn.ui.base.widget.ITextFieldKeys;
import wynn.ui.base.widget.Spinner;
import wynn.ui.base.widget.ToggleSwitch;

import net.minecraft.util.text.TextFormatting;

public class ClickGUI extends MinecraftHUDGUI {
	private final GUIInterface inter;
	private final HUDGUI gui;
	public static final int WIDTH=120,HEIGHT=12,DISTANCE=6,BORDER=2;
	
	public ClickGUI() {
		// Getting client structure ...
		IClient client=Category.getClient();
		/* Set to false to disable horizontal clipping, this may cause graphical glitches,
		 * but will let you see long text, even if it is too long to fit in the panel. */
		inter=new GUIInterface(true) {
			@Override
			protected String getResourcePrefix() {
				return "WynnClient:";
			}
		};
		// Instantiating theme ...
		ITheme theme=new OptimizedTheme(new ThemeSelector(inter));
		// Instantiating GUI ...
		IToggleable guiToggle=new SimpleToggleable(false);
		IToggleable hudToggle=new SimpleToggleable(false) {
			@Override
			public boolean isOn() {
				return guiToggle.isOn()?HUDEditorModule.showHUD.isOn():super.isOn();
			}
		};
		gui=new HUDGUI(inter,theme.getDescriptionRenderer(),(IPopupPositioner)new MousePositioner(new Point(10,10)),guiToggle,hudToggle);
		// Creating animation ...
		Supplier<Animation> animation=()->new SettingsAnimation(()->ClickGUIModule.animationSpeed.getValue(),()->inter.getTime());
		// Populating HUD ...

		gui.addHUDComponent(WatermarkModule.getComponent(),WatermarkModule.getToggle(),animation.get(),theme,BORDER);
		gui.addHUDComponent(LogoModule.getComponent(inter),LogoModule.getToggle(),animation.get(),theme,BORDER);
		
		// Creating popup types ...
		BiFunction<Context,Integer,Integer> scrollHeight=(context,componentHeight)->Math.min(componentHeight,Math.max(HEIGHT*4,ClickGUI.this.height-context.getPos().y-HEIGHT));
		PopupTuple popupType=new PopupTuple(new PanelPositioner(new Point(0,0)),false,new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return scrollHeight.apply(context,componentHeight);
			}
		});
		PopupTuple colorPopup=new PopupTuple(new CenteredPositioner(()->new Rectangle(new Point(0,0),inter.getWindowSize())),true,new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return scrollHeight.apply(context,componentHeight);
			}
		});
		// Defining resize behavior ...
		IntFunction<IResizable> resizable=width->new IResizable() {
			Dimension size=new Dimension(width,320);
			
			@Override
			public Dimension getSize() {
				return new Dimension(size);
			}

			@Override
			public void setSize (Dimension size) {
				this.size.width=size.width;
				this.size.height=size.height;
				if (size.width<75) this.size.width=75;
				if (size.height<50) this.size.height=50;
			}
		};
		// Defining scroll behavior ...
		Function<IResizable,IScrollSize> resizableHeight=size->new IScrollSize() {
			@Override
			public int getScrollHeight (Context context, int componentHeight) {
				return size.getSize().height;
			}
		};
		// Defining function keys ...
		IntPredicate keybindKey=scancode->scancode==Keyboard.KEY_DELETE;
		IntPredicate charFilter=character->{
			return character>=' ';
		};
		ITextFieldKeys keys=new ITextFieldKeys() {
			@Override
			public boolean isBackspaceKey (int scancode) {
				return scancode==Keyboard.KEY_BACK;
			}

			@Override
			public boolean isDeleteKey (int scancode) {
				return scancode==Keyboard.KEY_DELETE;
			}

			@Override
			public boolean isInsertKey (int scancode) {
				return scancode==Keyboard.KEY_INSERT;
			}

			@Override
			public boolean isLeftKey (int scancode) {
				return scancode==Keyboard.KEY_LEFT;
			}

			@Override
			public boolean isRightKey (int scancode) {
				return scancode==Keyboard.KEY_RIGHT;
			}

			@Override
			public boolean isHomeKey (int scancode) {
				return scancode==Keyboard.KEY_HOME;
			}

			@Override
			public boolean isEndKey (int scancode) {
				return scancode==Keyboard.KEY_END;
			}

			@Override
			public boolean isCopyKey (int scancode) {
				return scancode==Keyboard.KEY_C;
			}

			@Override
			public boolean isPasteKey (int scancode) {
				return scancode==Keyboard.KEY_V;
			}

			@Override
			public boolean isCutKey (int scancode) {
				return scancode==Keyboard.KEY_X;
			}

			@Override
			public boolean isAllKey (int scancode) {
				return scancode==Keyboard.KEY_A;
			}			
		};
		
		// Normal generator
		IComponentGenerator generator=new ComponentGenerator(keybindKey,charFilter,keys);
		// Use cycle switches instead of buttons
		IComponentGenerator cycleGenerator=new ComponentGenerator(keybindKey,charFilter,keys) {
			@Override
			public IComponent getEnumComponent (IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new CycleSwitch(setting,theme.getCycleSwitchRenderer(isContainer));
			}
		};
		// Use all the fancy widgets with text boxes
		IComponentGenerator csgoGenerator=new ComponentGenerator(keybindKey,charFilter,keys) {
			@Override
			public IComponent getBooleanComponent (IBooleanSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new ToggleSwitch(setting,theme.getToggleSwitchRenderer(isContainer));
			}
			
			@Override
			public IComponent getEnumComponent (IEnumSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new DropDownList(setting,theme,isContainer,false,keys,new IScrollSize(){},adder::addPopup) {
					@Override
					protected Animation getAnimation() {
						return animation.get();
					}

					@Override
					public boolean allowCharacter (char character) {
						return charFilter.test(character);
					}

					@Override
					protected boolean isUpKey (int key) {
						return key==Keyboard.KEY_UP;
					}

					@Override
					protected boolean isDownKey (int key) {
						return key==Keyboard.KEY_DOWN;
					}

					@Override
					protected boolean isEnterKey (int key) {
						return key==Keyboard.KEY_RETURN;
					}
				};
			}
			
			@Override
			public IComponent getNumberComponent (INumberSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new Spinner(setting,theme,isContainer,true,keys);
			}
			
			@Override
			public IComponent getColorComponent (IColorSetting setting, Supplier<Animation> animation, IComponentAdder adder, ThemeTuple theme, int colorLevel, boolean isContainer) {
				return new ColorPickerComponent(setting,new ThemeTuple(theme.theme,theme.logicalLevel,colorLevel));
			}
		};

		IComponentAdder popupPanelAdder=new PanelAdder(gui,false,()->ClickGUIModule.layout.getValue()==ClickGUIModule.Layout.PopupPanel,title->"popupPanel_"+title) {
			@Override
			protected IResizable getResizable (int width) {
				return resizable.apply(width);
			}

			@Override
			protected IScrollSize getScrollSize (IResizable size) {
				return resizableHeight.apply(size);
			}
		};
		ILayout popupPanelLayout=new PanelLayout(WIDTH,new Point(DISTANCE,DISTANCE),(WIDTH+DISTANCE)/2,HEIGHT+DISTANCE,animation,level->ChildMode.POPUP,level->ChildMode.DOWN,popupType);
		popupPanelLayout.populateGUI(popupPanelAdder,generator,client,theme);
	}

	@Override
	protected HUDGUI getGUI() {
		return gui;
	}

	@Override
	protected GUIInterface getInterface() {
		return inter;
	}

	@Override
	protected int getScrollSpeed() {
		return ClickGUIModule.scrollSpeed.getValue();
	}

	
	private class ThemeSelector implements IThemeMultiplexer {
		protected Map<ClickGUIModule.Theme,ITheme> themes=new EnumMap<ClickGUIModule.Theme,ITheme>(ClickGUIModule.Theme.class);
		
		public ThemeSelector (IInterface inter) {
			BooleanSetting clearGradient=new BooleanSetting("Gradient","gradient","Whether the title bars should have a gradient.",()->ClickGUIModule.theme.getValue()==Theme.Clear,true);
			BooleanSetting ignoreDisabled=new BooleanSetting("Ignore Disabled","ignoreDisabled","Have the rainbow drawn for disabled containers.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,false);
			BooleanSetting buttonRainbow=new BooleanSetting("Button Rainbow","buttonRainbow","Have a separate rainbow for each component.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,false);
			IntegerSetting rainbowGradient=new IntegerSetting("Rainbow Gradient","rainbowGradient","How fast the rainbow should repeat.",()->ClickGUIModule.theme.getValue()==Theme.Rainbow,150,50,300);
			ClickGUIModule.theme.subSettings.add(clearGradient);
			ClickGUIModule.theme.subSettings.add(ignoreDisabled);
			ClickGUIModule.theme.subSettings.add(buttonRainbow);
			ClickGUIModule.theme.subSettings.add(rainbowGradient);
			addTheme(Theme.Clear,new ClearTheme(new ThemeScheme(Theme.Clear),()->clearGradient.getValue(),9,3,1,": "+TextFormatting.GRAY));
			addTheme(Theme.GameSense,new GameSenseTheme(new ThemeScheme(Theme.GameSense),9,4,5,": "+TextFormatting.GRAY));
			addTheme(Theme.Rainbow,new RainbowTheme(new ThemeScheme(Theme.Rainbow),()->ignoreDisabled.getValue(),()->buttonRainbow.getValue(),()->rainbowGradient.getValue(),9,3,": "+TextFormatting.GRAY));
		}
		
		@Override
		public ITheme getTheme() {
			return themes.getOrDefault(ClickGUIModule.theme.getValue(),themes.get(Theme.GameSense));
		}
		
		private void addTheme (Theme key, ITheme value) {
			themes.put(key,new OptimizedTheme(value));
			value.loadAssets(inter);
		}
		
		
		private class ThemeScheme implements IColorScheme {
			private final Theme themeValue;
			private final String themeName;
			
			public ThemeScheme (Theme themeValue) {
				this.themeValue=themeValue;
				this.themeName=themeValue.toString().toLowerCase();
			}
			
			@Override
			public void createSetting (ITheme theme, String name, String description, boolean hasAlpha, boolean allowsRainbow, Color color, boolean rainbow) {
				ClickGUIModule.theme.subSettings.add(new ColorSetting(name,themeName+"-"+name,description,()->ClickGUIModule.theme.getValue()==themeValue,hasAlpha,allowsRainbow,color,rainbow));
			}

			@Override
			public Color getColor (String name) {
				return ((ColorSetting)ClickGUIModule.theme.subSettings.stream().filter(setting->setting.configName.equals(themeName+"-"+name)).findFirst().orElse(null)).getValue();
			}
		}
	}
}
