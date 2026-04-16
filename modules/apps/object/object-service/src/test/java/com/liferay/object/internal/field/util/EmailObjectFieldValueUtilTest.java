/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.util;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Eduardo Garcia
 */
public class EmailObjectFieldValueUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testIsValid() {
		Assert.assertFalse(EmailObjectFieldValueUtil.isValid(null));
		Assert.assertFalse(EmailObjectFieldValueUtil.isValid("user"));
		Assert.assertFalse(EmailObjectFieldValueUtil.isValid("user@"));
		Assert.assertFalse(
			EmailObjectFieldValueUtil.isValid("user@example"));
		Assert.assertTrue(
			EmailObjectFieldValueUtil.isValid("user@example.com"));
	}

	@Test
	public void testNormalize() {
		Assert.assertNull(EmailObjectFieldValueUtil.normalize(null));
		Assert.assertEquals(
			"user@example.com",
			EmailObjectFieldValueUtil.normalize("User@Example.com"));
	}

}
