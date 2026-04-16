/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.object.internal.field.util;

import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * @author Eduardo Garcia
 */
public class EmailObjectFieldValueUtil {

	public static boolean isValid(String emailAddress) {
		if ((emailAddress == null) || (emailAddress.length() > 254)) {
			return false;
		}

		return Validator.isEmailAddress(emailAddress);
	}

	public static String normalize(String emailAddress) {
		if (emailAddress == null) {
			return null;
		}

		return StringUtil.toLowerCase(emailAddress);
	}

	private EmailObjectFieldValueUtil() {
	}

}
