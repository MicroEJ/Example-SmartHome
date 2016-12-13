/*
 * Java
 *
 * Copyright 2016 IS2T. All rights reserved.
 * IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.microej.demo.smarthome.style;

import com.microej.demo.smarthome.util.Colors;
import com.microej.demo.smarthome.util.Constants;
import com.microej.demo.smarthome.widget.CircularPlainBackground;

import ej.bon.Util;
import ej.microui.display.GraphicsContext;
import ej.style.State;
import ej.style.Stylesheet;
import ej.style.background.NoBackground;
import ej.style.background.PlainBackground;
import ej.style.background.SimpleRoundedPlainBackground;
import ej.style.font.FontProfile;
import ej.style.font.FontProfile.FontSize;
import ej.style.outline.ComplexOutline;
import ej.style.outline.Outline;
import ej.style.outline.SimpleOutline;
import ej.style.selector.ClassSelector;
import ej.style.selector.StateSelector;
import ej.style.selector.combinator.AndCombinator;
import ej.style.text.ComplexTextManager;
import ej.style.util.EditableStyle;
import ej.style.util.StyleHelper;

/**
 *
 */
public class StylePopulator {

	private static final int SIMPLE_OUTLINE = 8;
	private static final int HALF_OUTLINE = SIMPLE_OUTLINE >> 1;
	private static final int DOUBLE_OUTLINE = SIMPLE_OUTLINE << 1;

	public static void initializeStylesheet() {

		Util.setCurrentTimeMillis(Constants.START_TIME);
		Stylesheet stylesheet = StyleHelper.getStylesheet();

		FontProfile fpXSmall = new FontProfile();
		fpXSmall.setFamily(FontFamilies.SOURCE_SANS_PRO);
		fpXSmall.setSize(FontSize.X_SMALL);

		FontProfile fpSmall = new FontProfile();
		fpSmall.setFamily(FontFamilies.SOURCE_SANS_PRO);
		fpSmall.setSize(FontSize.SMALL);

		FontProfile fpMedium = new FontProfile();
		fpMedium.setFamily(FontFamilies.SOURCE_SANS_PRO);
		fpMedium.setSize(FontSize.MEDIUM);

		FontProfile fpLarge = new FontProfile();
		fpLarge.setFamily(FontFamilies.SOURCE_SANS_PRO);
		fpLarge.setSize(FontSize.LARGE);

		FontProfile fpXLarge = new FontProfile();
		fpXLarge.setFamily(FontFamilies.SOURCE_SANS_PRO_LIGHT);
		fpXLarge.setSize(FontSize.X_LARGE);

		FontProfile fpXXLarge = new FontProfile();
		fpXXLarge.setFamily(FontFamilies.SOURCE_SANS_PRO_LIGHT);
		fpXXLarge.setSize(FontSize.XX_LARGE);

		Outline defaultOutline = new SimpleOutline(SIMPLE_OUTLINE);
		Outline doubleOutline = new SimpleOutline(DOUBLE_OUTLINE);
		PlainBackground plainBackground = new PlainBackground();

		{ // Default style
			EditableStyle defaultStyle = new EditableStyle();
			defaultStyle.setForegroundColor(Colors.WHITE);
			defaultStyle.setBackgroundColor(Colors.BLACK_50);
			defaultStyle.setBackground(NoBackground.NO_BACKGROUND);
			defaultStyle.setFontProfile(fpSmall);
			defaultStyle.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
			stylesheet.setDefaultStyle(defaultStyle);
		}


		{ // Header
			EditableStyle headerStyle = new EditableStyle();
			headerStyle.setBackground(plainBackground);
			headerStyle.setPadding(defaultOutline);
			stylesheet.addRule(new ClassSelector(ClassSelectors.HEADER), headerStyle);
		}

		{ // Footer
			ClassSelector footerClassSelector = new ClassSelector(ClassSelectors.FOOTER);

			EditableStyle footerStyle = new EditableStyle();
			footerStyle.setBackground(plainBackground);
			footerStyle.setPadding(
					new ComplexOutline(SIMPLE_OUTLINE, SIMPLE_OUTLINE, SIMPLE_OUTLINE, DOUBLE_OUTLINE));
			footerStyle.setForegroundColor(Colors.CONCRETE_25);
			footerStyle.setBackgroundColor(Colors.WHITE);
			stylesheet.addRule(footerClassSelector, footerStyle);
		}

		{ // Active footer button
			EditableStyle activeFooterStyle = new EditableStyle();
			activeFooterStyle.setForegroundColor(Colors.CORAL);
			stylesheet.addRule(
					new AndCombinator(new ClassSelector(ClassSelectors.FOOTER_MENU_BUTTON),
							new StateSelector(State.Focus)),
					activeFooterStyle);
		}

		{ // Body
			EditableStyle bodyStyle = new EditableStyle();
			bodyStyle.setBackground(plainBackground);
			bodyStyle.setBackgroundColor(Colors.CONCRETE_90);
			bodyStyle.setForegroundColor(Colors.CONCRETE_25);
			bodyStyle.setPadding(new ComplexOutline(0, 0, DOUBLE_OUTLINE, 0));
			stylesheet.addRule(new ClassSelector(ClassSelectors.BODY), bodyStyle);
		}

		{ // Light
			EditableStyle lightStyle = new EditableStyle();
			lightStyle.setBackgroundColor(Colors.CONCRETE_25);
			lightStyle.setForegroundColor(Colors.CORAL);
			lightStyle.setMargin(defaultOutline);
			stylesheet.addRule(new ClassSelector(ClassSelectors.LIGHT), lightStyle);
		}

		{ // Light percentage value

			ClassSelector lightValueSelector = new ClassSelector(ClassSelectors.LIGHT_VALUE);
			{
				EditableStyle lightPercentageValueStyle = new EditableStyle();
				lightPercentageValueStyle.setForegroundColor(Colors.CORAL);
				lightPercentageValueStyle.setFontProfile(fpLarge);
				lightPercentageValueStyle.setAlignment(GraphicsContext.TOP | GraphicsContext.RIGHT);
				stylesheet.addRule(lightValueSelector, lightPercentageValueStyle);
			}

			{ // Light percentage value
				EditableStyle lightPercentageStyle = new EditableStyle();
				lightPercentageStyle.setForegroundColor(Colors.CONCRETE_25);
				lightPercentageStyle.setAlignment(GraphicsContext.TOP | GraphicsContext.LEFT);
				stylesheet.addRule(new AndCombinator(new ClassSelector(ClassSelectors.TYPE), lightValueSelector),
						lightPercentageStyle);
				stylesheet.addRule(new ClassSelector(ClassSelectors.LIGHT_VALUE_OFF), lightPercentageStyle);
			}

		}

		{ // Thermostat page
			{
				EditableStyle thermostatStyle = new EditableStyle();
				thermostatStyle.setForegroundColor(Colors.BLACK_50);
				thermostatStyle.setBackgroundColor(Colors.CONCRETE_25);
				thermostatStyle.setAlignment(GraphicsContext.HCENTER | GraphicsContext.VCENTER);
				thermostatStyle.setPadding(defaultOutline);
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT), thermostatStyle);
			}

			{
				EditableStyle thermostatTargetStyle = new EditableStyle();
				thermostatTargetStyle.setBackgroundColor(Colors.CORAL); // When "hoter"
				thermostatTargetStyle.setForegroundColor(Colors.LIGHT_BLUE); // When "colder"
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_TARGET_COLOR), thermostatTargetStyle);
			}

			{
				EditableStyle thermostatValidateStyle = new EditableStyle();
				thermostatValidateStyle.setBackgroundColor(Colors.WHITE);
				thermostatValidateStyle.setForegroundColor(Colors.CORAL);
				thermostatValidateStyle.setPadding(doubleOutline);
				thermostatValidateStyle.setBackground(new CircularPlainBackground());
				thermostatValidateStyle.setFontProfile(fpSmall);
				stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_VALIDATE), thermostatValidateStyle);
			}

			{ // Left & Right Labels

				{ // Shared style for the top (Current & Desired)
					EditableStyle thermostatTopLabel = new EditableStyle();
					thermostatTopLabel.setAlignment(GraphicsContext.HCENTER | GraphicsContext.BOTTOM);
					thermostatTopLabel.setFontProfile(fpSmall);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_TOP_LABEL), thermostatTopLabel);
				}

				{ // Color fo current
					EditableStyle blueLabel = new EditableStyle();
					blueLabel.setForegroundColor(Colors.BLACK_50);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_CURRENT), blueLabel);

				}

				{ // Color fo Desired
					EditableStyle coralLabel = new EditableStyle();
					coralLabel.setForegroundColor(Colors.CORAL);
					stylesheet.addRule(new ClassSelector(ClassSelectors.COLOR_CORAL),
							coralLabel);
					stylesheet.addRule(new AndCombinator(new ClassSelector(ClassSelectors.DASHBOARD_LIGHT_COUNT),
							new StateSelector(State.Enabled)),
							coralLabel);
				}

				{ // Color fo blue
					EditableStyle coralLabel = new EditableStyle();
					coralLabel.setForegroundColor(Colors.LIGHT_BLUE);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DESIRED_COLD), coralLabel);
				}

				{ // Shared style for the bottom (Current & Desired)
					EditableStyle thermostatBottomLabel = new EditableStyle();
					thermostatBottomLabel.setAlignment(GraphicsContext.HCENTER | GraphicsContext.TOP);
					thermostatBottomLabel.setFontProfile(fpXLarge);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_BOTTOM_LABEL),
							thermostatBottomLabel);
				}

				{ // Diff Value
					EditableStyle thermostatDiffValue = new EditableStyle();
					thermostatDiffValue.setAlignment(GraphicsContext.RIGHT | GraphicsContext.TOP);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE),
							thermostatDiffValue);
				}

				{ // Diff Value degree
					EditableStyle thermostatDiffValue = new EditableStyle();
					thermostatDiffValue.setAlignment(GraphicsContext.LEFT | GraphicsContext.TOP);
					stylesheet.addRule(new ClassSelector(ClassSelectors.THERMOSTAT_DIFF_VALUE_DEGREE),
							thermostatDiffValue);
				}

			}
		}

		{ // Dashboard

			ClassSelector dashBoardMenuSelector = new ClassSelector(ClassSelectors.DASHBOARD_MENU);
			{ // menu
				EditableStyle dashBoardMenuStyle = new EditableStyle();
				dashBoardMenuStyle.setPadding(defaultOutline);
				dashBoardMenuStyle.setFontProfile(fpXSmall);
				stylesheet.addRule(dashBoardMenuSelector, dashBoardMenuStyle);
			}

			{ // menu
				EditableStyle dashBoardActiveMenuStyle = new EditableStyle();
				dashBoardActiveMenuStyle.setPadding(new ComplexOutline(HALF_OUTLINE, 0, HALF_OUTLINE, 0));
				dashBoardActiveMenuStyle.setMargin(new ComplexOutline(0, DOUBLE_OUTLINE, 0, DOUBLE_OUTLINE));
				dashBoardActiveMenuStyle.setBackground(new SimpleRoundedPlainBackground(11));
				dashBoardActiveMenuStyle.setBackgroundColor(Colors.CORAL);
				dashBoardActiveMenuStyle.setForegroundColor(Colors.CONCRETE_90);
				stylesheet.addRule(new AndCombinator(new ClassSelector(ClassSelectors.DASHBOARD_MENU_BUTTON),
						new StateSelector(State.Focus)),
						dashBoardActiveMenuStyle);
			}

			{ // item icon
				EditableStyle dashboardIconValue = new EditableStyle();
				dashboardIconValue.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_ITEM_ICON), dashboardIconValue);
			}

			{ // item text
				EditableStyle dashboardTextStyle = new EditableStyle();
				dashboardTextStyle.setAlignment(GraphicsContext.LEFT | GraphicsContext.VCENTER);
				// dashboardTextStyle.setPadding(new ComplexOutline(4, 0, 0, 0));
				dashboardTextStyle.setTextManager(new ComplexTextManager());
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_ITEM_TEXT), dashboardTextStyle);
			}

			{ // Lights count text and temperature
				EditableStyle lightCountStyle = new EditableStyle();
				lightCountStyle.setFontProfile(fpXLarge);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_HUGE_TEXT), lightCountStyle);
			}

			{ // Instant Power Consumption
				EditableStyle powerConsumptionStyle = new EditableStyle();
				powerConsumptionStyle.setFontProfile(fpXLarge);
				powerConsumptionStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				powerConsumptionStyle.setForegroundColor(Colors.CORAL);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION), powerConsumptionStyle);
			}

			{ // Instant Power Bar
				EditableStyle powerConsumptionStyle = new EditableStyle();
				powerConsumptionStyle.setFontProfile(fpXLarge);
				powerConsumptionStyle.setAlignment(GraphicsContext.RIGHT | GraphicsContext.VCENTER);
				powerConsumptionStyle.setForegroundColor(Colors.CORAL);
				powerConsumptionStyle.setBackgroundColor(Colors.CONCRETE_25);
				powerConsumptionStyle.setPadding(defaultOutline);
				stylesheet.addRule(new ClassSelector(ClassSelectors.DASHBOARD_POWER_CONSUMPTION_BAR),
						powerConsumptionStyle);
			}

		}

		{
			EditableStyle testStyle = new EditableStyle();
			testStyle.setForegroundColor(0xFF0000);
			testStyle.setBackgroundColor(0x00FF00);
			testStyle.setBackground(plainBackground);
			stylesheet.addRule(new ClassSelector(ClassSelectors.TEST), testStyle);
		}
	}

}
