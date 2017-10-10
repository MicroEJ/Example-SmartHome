/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * Use of this source code is subject to license terms.
 */
package com.microej.demo.smarthome.style;

import com.microej.demo.smarthome.util.Colors;
import com.microej.demo.smarthome.widget.CircularPlainBackground;
import com.microej.demo.smarthome.widget.TimeWidget;

import ej.microui.display.Font;
import ej.microui.display.GraphicsContext;
import ej.style.State;
import ej.style.Stylesheet;
import ej.style.background.NoBackground;
import ej.style.background.PlainBackground;
import ej.style.background.SimpleRoundedPlainBackground;
import ej.style.border.SimpleRoundedBorder;
import ej.style.font.FontProfile;
import ej.style.font.FontProfile.FontSize;
import ej.style.outline.ComplexOutline;
import ej.style.outline.Outline;
import ej.style.outline.SimpleOutline;
import ej.style.selector.ClassSelector;
import ej.style.selector.StateSelector;
import ej.style.selector.TypeSelector;
import ej.style.selector.combinator.AndCombinator;
import ej.style.text.ComplexTextManager;
import ej.style.util.EditableStyle;
import ej.style.util.StyleHelper;

/**
 * The populator of the stylesheet.
 */
public class StylePopulator {

	private static final int SIMPLE_OUTLINE = 8;
	private static final int HALF_OUTLINE = SIMPLE_OUTLINE / 2;
	private static final int DOUBLE_OUTLINE = SIMPLE_OUTLINE * 2;

	
	private StylePopulator(){
		// Forbid instantiation.
	}
	
	/**
	 * Initialize the stylesheet.
	 */
	public static void initializeStylesheet() {

		final Stylesheet stylesheet = StyleHelper.getStylesheet();

		final FontProfile fpXSmall = new FontProfile(FontFamilies.SOURCE_SANS_PRO, FontSize.X_SMALL, Font.STYLE_PLAIN);
		final FontProfile fpSmall = new FontProfile(FontFamilies.SOURCE_SANS_PRO, FontSize.SMALL, Font.STYLE_PLAIN);
		final FontProfile fpMedium = new FontProfile(FontFamilies.SOURCE_SANS_PRO, FontSize.MEDIUM, Font.STYLE_PLAIN);
		// final FontProfile fpLarge = new FontProfile(FontFamilies.SOURCE_SANS_PRO, FontSize.LARGE, Font.STYLE_PLAIN);
		final FontProfile fpXLarge = new FontProfile(FontFamilies.SOURCE_SANS_PRO_LIGHT, FontSize.X_LARGE,
				Font.STYLE_PLAIN);
		// final FontProfile fpXXLarge = new FontProfile(FontFamilies.SOURCE_SANS_PRO_LIGHT, FontSize.XX_LARGE,
		// Font.STYLE_PLAIN);

		final Outline defaultOutline = new SimpleOutline(SIMPLE_OUTLINE);
		final Outline doubleOutline = new SimpleOutline(DOUBLE_OUTLINE);
		final PlainBackground plainBackground = new PlainBackground();

		{ // Default style
			final EditableStyle defaultStyle = new EditableStyle();
			defaultStyle.setForegroundColor(Colors.WHITE);
			defaultStyle.setBackgroundColor(Colors.BLACK_50);
			defaultStyle.setBackground(NoBackground.NO_BACKGROUND);
			defaultStyle.setFontProfile(fpSmall);
			defaultStyle.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			stylesheet.setDefaultStyle(defaultStyle);
		}

		{ // Header
			final EditableStyle headerStyle = new EditableStyle();
			headerStyle.setForegroundColor(Colors.WHITE);
			headerStyle.setBackground(plainBackground);
			headerStyle.setPadding(defaultOutline);
			stylesheet.addRule(new ClassSelector(ClassSelectors.HEADER), headerStyle);
		}

		{ // Time widget
			final EditableStyle timeStyle = new EditableStyle();
			timeStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
			stylesheet.addRule(new TypeSelector(TimeWidget.class), timeStyle);
		}

		{ // Footer
			final ClassSelector footerClassSelector = new ClassSelector(ClassSelectors.FOOTER);

			final EditableStyle footerStyle = new EditableStyle();
			footerStyle.setBackground(plainBackground);
			footerStyle.setPadding(new ComplexOutline(SIMPLE_OUTLINE, SIMPLE_OUTLINE, SIMPLE_OUTLINE, DOUBLE_OUTLINE));
			footerStyle.setForegroundColor(Colors.CONCRETE_25);
			footerStyle.setBackgroundColor(Colors.WHITE);
			stylesheet.addRule(footerClassSelector, footerStyle);
		}

		{ // Active footer button
			final EditableStyle activeFooterStyle = new EditableStyle();
			activeFooterStyle.setForegroundColor(Colors.CORAL);
			stylesheet.addRule(new AndCombinator(new ClassSelector(ClassSelectors.FOOTER_MENU_BUTTON),
					new StateSelector(State.Checked)), activeFooterStyle);
		}

		{ // Body
			final EditableStyle bodyStyle = new EditableStyle();
			bodyStyle.setBackground(plainBackground);
			bodyStyle.setBackgroundColor(Colors.CONCRETE_90);
			bodyStyle.setForegroundColor(Colors.CONCRETE_25);
			stylesheet.addRule(new ClassSelector(ClassSelectors.BODY), bodyStyle);
		}

		{ // Light

			{ // Light widget
				final EditableStyle lightWidgetStyle = new EditableStyle();
				lightWidgetStyle.setPadding(new ComplexOutline(SIMPLE_OUTLINE, 0, DOUBLE_OUTLINE, 0));
				stylesheet.addRule(new ClassSelector(ClassSelectors.LIGHT_WIDGET), lightWidgetStyle);
			}

			{ // Light progress
				final EditableStyle lightProgressStyle = new EditableStyle();
				lightProgressStyle.setBackgroundColor(Colors.CONCRETE_25);
				lightProgressStyle.setForegroundColor(Colors.CORAL);
				lightProgressStyle.setMargin(new ComplexOutline(HALF_OUTLINE, 0, HALF_OUTLINE, 0));
				stylesheet.addRule(new ClassSelector(ClassSelectors.LIGHT_PROGRESS), lightProgressStyle);
			}

			{ // Light toggle
				final EditableStyle lightToggleStyle = new EditableStyle();
				lightToggleStyle.setMargin(new ComplexOutline(0, 0, DOUBLE_OUTLINE, 0));
				stylesheet.addRule(new ClassSelector(ClassSelectors.LIGHT_TOGGLE), lightToggleStyle);
			}

		}

		{ // Color picker

			{ // Picker
				final EditableStyle pickerStyle = new EditableStyle();
				pickerStyle.setForegroundColor(Colors.BLACK_50);
				pickerStyle.setBackgroundColor(Colors.CONCRETE_90);
				stylesheet.addRule(new ClassSelector(ClassSelectors.PICKER), pickerStyle);
			}

			{ // Title label
				final EditableStyle pickerTitleStyle = new EditableStyle();
				pickerTitleStyle.setForegroundColor(Colors.CONCRETE_25);
				pickerTitleStyle.setAlignment(GraphicsContext.LEFT | GraphicsContext.VCENTER);
				pickerTitleStyle
						.setPadding(new ComplexOutline(SIMPLE_OUTLINE, DOUBLE_OUTLINE, SIMPLE_OUTLINE, DOUBLE_OUTLINE));
				stylesheet.addRule(new ClassSelector(ClassSelectors.PICKER_TITLE_LABEL), pickerTitleStyle);
			}

			{ // Close button
				final EditableStyle pickerCloseStyle = new EditableStyle();
				pickerCloseStyle.setForegroundColor(Colors.CORAL);
				pickerCloseStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				pickerCloseStyle
						.setPadding(new ComplexOutline(SIMPLE_OUTLINE, DOUBLE_OUTLINE, SIMPLE_OUTLINE, DOUBLE_OUTLINE));
				stylesheet.addRule(new ClassSelector(ClassSelectors.PICKER_CLOSE_BUTTON), pickerCloseStyle);
			}
			
			{ // Picker Image
				final EditableStyle pickerImageStyle = new EditableStyle();
				pickerImageStyle.setBackground(plainBackground);
				pickerImageStyle.setBackgroundColor(Colors.CONCRETE_90);
				stylesheet.addRule(new ClassSelector(ClassSelectors.PICKER_PLAIN_BACKGROUND), pickerImageStyle);
			}
			
		}

		{ // Door widget
			final EditableStyle doorWidgetStyle = new EditableStyle();
			doorWidgetStyle.setPadding(new ComplexOutline(DOUBLE_OUTLINE, 0, 0, 0));
			stylesheet.addRule(new ClassSelector(ClassSelectors.DOOR_WIDGET), doorWidgetStyle);
		}

		{ // Thermostat page
			{
				final EditableStyle thermostatStyle = new EditableStyle();
				thermostatStyle.setForegroundColor(Colors.BLACK_50);
				thermostatStyle.setBackgroundColor(Colors.CONCRETE_25);
				thermostatStyle.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
				thermostatStyle.setPadding(defaultOutline);
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT), thermostatStyle);
			}

			{
				final EditableStyle thermostatTargetStyle = new EditableStyle();
				thermostatTargetStyle.setBackgroundColor(Colors.CORAL); // When "hoter"
				thermostatTargetStyle.setForegroundColor(Colors.LIGHT_BLUE); // When "colder"
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_TARGET_COLOR), thermostatTargetStyle);
			}

			{
				final EditableStyle thermostatValidateStyle = new EditableStyle();
				thermostatValidateStyle.setBackgroundColor(Colors.WHITE);
				thermostatValidateStyle.setForegroundColor(Colors.CORAL);
				thermostatValidateStyle.setPadding(doubleOutline);
				thermostatValidateStyle.setBackground(new CircularPlainBackground());
				thermostatValidateStyle.setFontProfile(fpSmall);
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_VALIDATE), thermostatValidateStyle);
			}

			{ // Left & Right Labels

				{ // Shared style for the top (Current & Desired)
					final EditableStyle thermostatTopLabel = new EditableStyle();
					thermostatTopLabel.setAlignment(GraphicsContext.HCENTER | GraphicsContext.BOTTOM);
					thermostatTopLabel.setFontProfile(fpSmall);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL), thermostatTopLabel);
				}

				{ // Color fo current
					final EditableStyle blueLabel = new EditableStyle();
					blueLabel.setForegroundColor(Colors.BLACK_50);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_CURRENT), blueLabel);

				}

				{ // Color fo Desired
					final EditableStyle coralLabel = new EditableStyle();
					coralLabel.setForegroundColor(Colors.CORAL);
					stylesheet.addRule(new ClassSelector(ClassSelectors.COLOR_CORAL), coralLabel);
					stylesheet.addRule(new AndCombinator(new ClassSelector(ClassSelectors.DASHBOARD_LIGHT_COUNT),
							new StateSelector(State.Enabled)), coralLabel);
				}

				{ // Color for blue
					final EditableStyle coralLabel = new EditableStyle();
					coralLabel.setForegroundColor(Colors.LIGHT_BLUE);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD), coralLabel);
				}

				{ // Shared style for the bottom (Current & Desired)
					final EditableStyle thermostatBottomLabel = new EditableStyle();
					thermostatBottomLabel.setAlignment(GraphicsContext.HCENTER | GraphicsContext.TOP);
					thermostatBottomLabel.setFontProfile(fpXLarge);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL),
							thermostatBottomLabel);
				}

				{ // Diff Value
					final EditableStyle thermostatDiffValue = new EditableStyle();
					thermostatDiffValue.setAlignment(GraphicsContext.RIGHT | GraphicsContext.TOP);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE), thermostatDiffValue);
				}

				{ // Diff Value degree
					final EditableStyle thermostatDiffValue = new EditableStyle();
					thermostatDiffValue.setAlignment(GraphicsContext.LEFT | GraphicsContext.TOP);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE_DEGREE),
							thermostatDiffValue);
				}

			}
		}

		{ // Dashboard

			final ClassSelector dashBoardMenuSelector = new ClassSelector(ClassSelectors.DASHBOARD_MENU);
			final ClassSelector menuButtonSelector = new ClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON);
			{ // menu
				final EditableStyle dashBoardMenuStyle = new EditableStyle();
				dashBoardMenuStyle.setPadding(defaultOutline);
				dashBoardMenuStyle.setBackground(plainBackground);
				dashBoardMenuStyle.setBackgroundColor(Colors.CORAL);
				dashBoardMenuStyle.setForegroundColor(Colors.WHITE);
				dashBoardMenuStyle.setFontProfile(fpXSmall);
				stylesheet.addRule(dashBoardMenuSelector, dashBoardMenuStyle);
			}

			{ // menu
				final EditableStyle dashBoardActiveMenuStyle = new EditableStyle();
				dashBoardActiveMenuStyle.setPadding(new ComplexOutline(HALF_OUTLINE, 0, HALF_OUTLINE, 0));
				dashBoardActiveMenuStyle.setMargin(new ComplexOutline(0, DOUBLE_OUTLINE, 0, DOUBLE_OUTLINE));
				dashBoardActiveMenuStyle.setBorder(new SimpleRoundedBorder(12, 1));
				dashBoardActiveMenuStyle.setBorderColor(Colors.WHITE);
				stylesheet.addRule(menuButtonSelector, dashBoardActiveMenuStyle);
			}

			{ // menu
				final EditableStyle dashBoardActiveMenuStyle = new EditableStyle();
				dashBoardActiveMenuStyle.setBackground(new SimpleRoundedPlainBackground(14));
				dashBoardActiveMenuStyle.setBackgroundColor(Colors.WHITE);
				dashBoardActiveMenuStyle.setForegroundColor(Colors.CORAL);
				stylesheet.addRule(new AndCombinator(menuButtonSelector, new StateSelector(State.Checked)),
						dashBoardActiveMenuStyle);
			}

			{ // item icon
				final EditableStyle dashboardIconValue = new EditableStyle();
				dashboardIconValue.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_ITEM_ICON), dashboardIconValue);
			}

			{ // item text
				final EditableStyle dashboardTextStyle = new EditableStyle();
				dashboardTextStyle.setAlignment(GraphicsContext.LEFT | GraphicsContext.VCENTER);
				dashboardTextStyle.setForegroundColor(Colors.CONCRETE_25);
				dashboardTextStyle.setTextManager(new ComplexTextManager());
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT), dashboardTextStyle);
			}

			{ // Lights count text and temperature
				final EditableStyle lightCountStyle = new EditableStyle();
				lightCountStyle.setFontProfile(fpXLarge);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT), lightCountStyle);
			}

			{ // Instant Power Consumption
				final EditableStyle powerConsumptionStyle = new EditableStyle();
				powerConsumptionStyle.setFontProfile(fpXLarge);
				powerConsumptionStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				powerConsumptionStyle.setForegroundColor(Colors.CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION),
						powerConsumptionStyle);
			}

			{ // Instant Power Bar
				final EditableStyle powerConsumptionStyle = new EditableStyle();
				powerConsumptionStyle.setFontProfile(fpXLarge);
				powerConsumptionStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				powerConsumptionStyle.setForegroundColor(Colors.CORAL);
				powerConsumptionStyle.setBackgroundColor(Colors.CONCRETE_25);
				powerConsumptionStyle.setPadding(defaultOutline);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR),
						powerConsumptionStyle);
			}

		}

		{ // Chart page

			{ // Chart scroll
				final EditableStyle chartScrollStyle = new EditableStyle();
				chartScrollStyle.setBackground(new PlainBackground());
				chartScrollStyle.setBackgroundColor(Colors.CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART_SCROLL), chartScrollStyle);
			}

			{ // Chart
				final EditableStyle chartStyle = new EditableStyle();
				chartStyle.setFontProfile(fpSmall);
				chartStyle.setForegroundColor(Colors.WHITE);
				chartStyle.setPadding(doubleOutline);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART), chartStyle);
			}

			{ // Chart point
				final EditableStyle chartPointStyle = new EditableStyle();
				chartPointStyle.setFontProfile(fpXSmall);
				chartPointStyle.setForegroundColor(Colors.WHITE);
				chartPointStyle.setBackgroundColor(Colors.CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART_POINT), chartPointStyle);
			}

			{ // Chart scale
				final EditableStyle chartScaleStyle = new EditableStyle();
				chartScaleStyle.setFontProfile(fpSmall);
				chartScaleStyle.setForegroundColor(Colors.LIGHT_CORAL);
				chartScaleStyle.setBackgroundColor(Colors.DARK_CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART_SCALE), chartScaleStyle);
			}

			{ // Chart selected info
				final EditableStyle chartInfoStyle = new EditableStyle();
				chartInfoStyle.setFontProfile(fpSmall);
				chartInfoStyle.setForegroundColor(Colors.CONCRETE_25);
				chartInfoStyle.setBackgroundColor(Colors.WHITE);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART_SELECTED_INFO), chartInfoStyle);
			}

			{ // Chart selected value
				final EditableStyle chartValueStyle = new EditableStyle();
				chartValueStyle.setFontProfile(fpMedium);
				chartValueStyle.setForegroundColor(Colors.CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.CHART_SELECTED_VALUE), chartValueStyle);
			}

		}
	}
}
