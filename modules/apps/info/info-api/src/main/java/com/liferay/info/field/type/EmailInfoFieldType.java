/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.info.field.type;

/**
 * @author Eduardo Garcia
 */
public class EmailInfoFieldType implements InfoFieldType {

	public static final EmailInfoFieldType INSTANCE = new EmailInfoFieldType();

	@Override
	public String getName() {
		return "email";
	}

	private EmailInfoFieldType() {
	}

}
