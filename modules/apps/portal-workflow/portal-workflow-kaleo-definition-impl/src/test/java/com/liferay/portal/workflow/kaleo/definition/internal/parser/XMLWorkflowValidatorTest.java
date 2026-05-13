/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.definition.internal.parser;

import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.security.script.management.configuration.helper.ScriptManagementConfigurationHelper;
import com.liferay.portal.test.rule.LiferayUnitTestRule;
import com.liferay.portal.workflow.kaleo.definition.Definition;
import com.liferay.portal.workflow.kaleo.definition.ScriptAction;
import com.liferay.portal.workflow.kaleo.definition.ScriptLanguage;
import com.liferay.portal.workflow.kaleo.definition.State;
import com.liferay.portal.workflow.kaleo.definition.exception.KaleoDefinitionValidationException.NotAllowedScriptLanguage;
import com.liferay.portal.workflow.kaleo.definition.parser.NodeValidator;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Yuri Monteiro
 */
public class XMLWorkflowValidatorTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Before
	public void setUp() {
		_xmlWorkflowValidator = new XMLWorkflowValidator();

		ScriptManagementConfigurationHelper
			scriptManagementConfigurationHelper = Mockito.mock(
				ScriptManagementConfigurationHelper.class);

		Mockito.when(
			scriptManagementConfigurationHelper.
				isAllowScriptContentToBeExecutedOrIncluded()
		).thenReturn(
			false
		);

		ReflectionTestUtil.setFieldValue(
			_xmlWorkflowValidator, "_scriptManagementConfigurationHelper",
			scriptManagementConfigurationHelper);

		ServiceTrackerMap<?, ?> serviceTrackerMap = Mockito.mock(
			ServiceTrackerMap.class);

		Mockito.when(
			serviceTrackerMap.getService(Mockito.any())
		).thenReturn(
			Mockito.mock(NodeValidator.class)
		);

		ReflectionTestUtil.setFieldValue(
			_xmlWorkflowValidator, "_serviceTrackerMap", serviceTrackerMap);
	}

	@Test
	public void testValidateAllowsJavaForReputationApprover() throws Exception {
		Definition definition = _buildDefinition(
			"Message Board Threads and Comments Reputation Approver",
			"<script-language>java</script-language>", ScriptLanguage.JAVA);

		_xmlWorkflowValidator.validate(definition);
	}

	@Test
	public void testValidateRejectsCDATAWrappedGroovy() throws Exception {
		Definition definition = _buildDefinition(
			"poc-cdata-groovy",
			"<script-language><![CDATA[groovy]]></script-language>",
			ScriptLanguage.GROOVY);

		try {
			_xmlWorkflowValidator.validate(definition);

			Assert.fail("Expected NotAllowedScriptLanguage");
		}
		catch (NotAllowedScriptLanguage notAllowedScriptLanguage) {
			Assert.assertEquals(
				"Groovy is not allowed", notAllowedScriptLanguage.getMessage());
		}
	}

	@Test
	public void testValidateRejectsPlainGroovy() throws Exception {
		Definition definition = _buildDefinition(
			"poc-plain-groovy", "<script-language>groovy</script-language>",
			ScriptLanguage.GROOVY);

		try {
			_xmlWorkflowValidator.validate(definition);

			Assert.fail("Expected NotAllowedScriptLanguage");
		}
		catch (NotAllowedScriptLanguage notAllowedScriptLanguage) {
			Assert.assertEquals(
				"Groovy is not allowed", notAllowedScriptLanguage.getMessage());
		}
	}

	@Test
	public void testValidateRejectsSplitCDATAGroovy() throws Exception {
		Definition definition = _buildDefinition(
			"poc-split-cdata-groovy",
			"<script-language>gr<![CDATA[oovy]]></script-language>",
			ScriptLanguage.GROOVY);

		try {
			_xmlWorkflowValidator.validate(definition);

			Assert.fail("Expected NotAllowedScriptLanguage");
		}
		catch (NotAllowedScriptLanguage notAllowedScriptLanguage) {
			Assert.assertEquals(
				"Groovy is not allowed", notAllowedScriptLanguage.getMessage());
		}
	}

	private Definition _buildDefinition(
			String name, String scriptLanguageXML,
			ScriptLanguage scriptLanguage)
		throws Exception {

		String content = StringBundler.concat(
			"<workflow-definition><name>", name,
			"</name><version>1</version><state><name>start</name>",
			"<initial>true</initial><actions><action><name>x</name>",
			"<script>println 'hi'</script>", scriptLanguageXML,
			"<execution-type>onEntry</execution-type></action></actions>",
			"</state><state><name>end</name></state></workflow-definition>");

		Definition definition = new Definition(name, "", content, 1);

		State startState = new State("start", "", true);

		startState.setActions(
			Collections.singleton(
				new ScriptAction(
					"x", "", "onEntry", "println 'hi'",
					scriptLanguage.getValue(), null, 0)));

		definition.addNode(startState);

		definition.addNode(new State("end", "", false));

		return definition;
	}

	private XMLWorkflowValidator _xmlWorkflowValidator;

}