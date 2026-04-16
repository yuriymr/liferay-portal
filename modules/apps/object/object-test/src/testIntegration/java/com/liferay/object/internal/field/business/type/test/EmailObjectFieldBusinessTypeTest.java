/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.business.type.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.constants.ObjectFieldSettingConstants;
import com.liferay.object.exception.ObjectFieldSettingValueException;
import com.liferay.object.field.builder.EmailObjectFieldBuilder;
import com.liferay.object.field.business.type.ObjectFieldBusinessType;
import com.liferay.object.field.business.type.ObjectFieldBusinessTypeRegistry;
import com.liferay.object.field.setting.builder.ObjectFieldSettingBuilder;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectField;
import com.liferay.object.model.ObjectFieldSetting;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.object.service.ObjectFieldSettingLocalServiceUtil;
import com.liferay.object.test.util.ObjectDefinitionTestUtil;
import com.liferay.portal.kernel.test.AssertUtils;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.test.rule.FeatureFlag;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.vulcan.util.LocalizedMapUtil;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Eduardo Garcia
 */
@FeatureFlag("LPD-70673")
@RunWith(Arquillian.class)
public class EmailObjectFieldBusinessTypeTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_objectFieldBusinessType =
			_objectFieldBusinessTypeRegistry.getObjectFieldBusinessType(
				ObjectFieldConstants.BUSINESS_TYPE_EMAIL);

		_objectDefinition =
			ObjectDefinitionTestUtil.addCustomObjectDefinition();

		_objectField = ObjectFieldUtil.addCustomObjectField(
			new EmailObjectFieldBuilder(
			).userId(
				TestPropsValues.getUserId()
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"emailField"
			).objectFieldSettings(
				Arrays.asList(
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_AUTOCOMPLETE
					).value(
						Boolean.TRUE.toString()
					).build(),
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_DOMAINS
					).value(
						"liferay.com,gmail.com"
					).build(),
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_UNIQUE_VALUES
					).value(
						Boolean.TRUE.toString()
					).build())
			).objectDefinitionId(
				_objectDefinition.getObjectDefinitionId()
			).build());

		_objectDefinition =
			ObjectDefinitionLocalServiceUtil.publishCustomObjectDefinition(
				TestPropsValues.getUserId(),
				_objectDefinition.getObjectDefinitionId());
	}

	@After
	public void tearDown() throws Exception {
		ObjectDefinitionLocalServiceUtil.deleteObjectDefinition(
			_objectDefinition.getObjectDefinitionId());
	}

	@Test
	public void testObjectFieldBusinessType() {
		Assert.assertEquals(
			ObjectFieldConstants.BUSINESS_TYPE_EMAIL,
			_objectField.getBusinessType());
		Assert.assertEquals(
			ObjectFieldConstants.DB_TYPE_STRING, _objectField.getDBType());
	}

	@Test
	public void testPredefineObjectFieldSettings() throws Exception {
		ObjectField objectField = ObjectFieldUtil.addCustomObjectField(
			new EmailObjectFieldBuilder(
			).userId(
				TestPropsValues.getUserId()
			).labelMap(
				LocalizedMapUtil.getLocalizedMap(RandomTestUtil.randomString())
			).name(
				"emailFieldWithDefaultValue"
			).objectFieldSettings(
				Arrays.asList(
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_DEFAULT_VALUE
					).value(
						"User@Example.com"
					).build(),
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_DEFAULT_VALUE_TYPE
					).value(
						ObjectFieldSettingConstants.VALUE_INPUT_AS_VALUE
					).build())
			).objectDefinitionId(
				_objectDefinition.getObjectDefinitionId()
			).build());

		ObjectFieldSetting objectFieldSetting =
			ObjectFieldSettingLocalServiceUtil.fetchObjectFieldSetting(
				objectField.getObjectFieldId(),
				ObjectFieldSettingConstants.NAME_DEFAULT_VALUE);

		Assert.assertEquals("user@example.com", objectFieldSetting.getValue());
	}

	@Test
	public void testValidateObjectFieldSettings() {
		AssertUtils.assertFailure(
			ObjectFieldSettingValueException.InvalidValue.class,
			"The value maybe of setting \"autocomplete\" is invalid for " +
				"object field \"emailField\"",
			() -> _objectFieldBusinessType.validateObjectFieldSettings(
				_objectField,
				Arrays.asList(
					new ObjectFieldSettingBuilder(
					).name(
						ObjectFieldSettingConstants.NAME_AUTOCOMPLETE
					).value(
						"maybe"
					).build())));
	}

	@Test
	public void testValidateObjectFieldSettingsDefaultValue() {
		AssertUtils.assertFailure(
			ObjectFieldSettingValueException.InvalidValue.class,
			"The value user of setting \"defaultValue\" is invalid for " +
				"object field \"emailField\"",
			() ->
				_objectFieldBusinessType.
					validateObjectFieldSettingsDefaultValue(
						_objectField,
						HashMapBuilder.put(
							ObjectFieldSettingConstants.NAME_DEFAULT_VALUE,
							"user"
						).put(
							ObjectFieldSettingConstants.NAME_DEFAULT_VALUE_TYPE,
							ObjectFieldSettingConstants.VALUE_INPUT_AS_VALUE
						).build()));
	}

	private ObjectDefinition _objectDefinition;
	private ObjectField _objectField;
	private ObjectFieldBusinessType _objectFieldBusinessType;

	@Inject
	private ObjectFieldBusinessTypeRegistry _objectFieldBusinessTypeRegistry;

}
