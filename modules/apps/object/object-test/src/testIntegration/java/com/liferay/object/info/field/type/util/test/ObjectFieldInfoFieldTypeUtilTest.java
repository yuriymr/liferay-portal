/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.info.field.type.util.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.info.field.type.PhoneNumberInfoFieldType;
import com.liferay.object.constants.ObjectFieldConstants;
import com.liferay.object.field.util.ObjectFieldUtil;
import com.liferay.object.info.field.type.util.ObjectFieldInfoFieldTypeUtil;
import com.liferay.object.model.ObjectField;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Yuri Monteiro
 */
@RunWith(Arquillian.class)
public class ObjectFieldInfoFieldTypeUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetInfoFieldTypePhoneNumber() {
		ObjectField objectField = ObjectFieldUtil.createObjectField(
			ObjectFieldConstants.BUSINESS_TYPE_PHONE_NUMBER,
			ObjectFieldConstants.DB_TYPE_STRING, "phoneNumberField");

		Assert.assertSame(
			PhoneNumberInfoFieldType.INSTANCE,
			ObjectFieldInfoFieldTypeUtil.getInfoFieldType(objectField));
	}

}
