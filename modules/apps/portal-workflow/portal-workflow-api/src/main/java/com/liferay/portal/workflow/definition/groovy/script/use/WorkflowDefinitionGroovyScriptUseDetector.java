/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.definition.groovy.script.use;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/**
 * @author Feliphe Marinho
 */
public class WorkflowDefinitionGroovyScriptUseDetector {

	public static boolean detect(String content, JSONFactory jsonFactory)
		throws JSONException {

		Queue<Map<String, Object>> queue = new LinkedList<>();

		JSONObject jsonObject = jsonFactory.createJSONObject(content);

		queue.add(jsonObject.toMap());

		while (!queue.isEmpty()) {
			Map<String, Object> map = queue.poll();

			if (Objects.equals(map.get("#tag-name"), "script-language")) {
				String scriptLanguage = _extractScriptLanguage(map);

				if (Objects.equals(scriptLanguage, "groovy") ||
					Objects.equals(scriptLanguage, "java")) {

					return true;
				}
			}

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if (Objects.equals(entry.getKey(), "#cdata-value")) {
					continue;
				}

				if (entry.getValue() instanceof List) {
					queue.addAll((List<Map<String, Object>>)entry.getValue());
				}
			}
		}

		return false;
	}

	private static String _extractScriptLanguage(Map<String, Object> map) {
		Object value = map.get("#value");

		if (value instanceof String) {
			String string = (String)value;

			return string.trim();
		}

		Object cdataValue = map.get("#cdata-value");

		if (cdataValue instanceof List) {
			StringBundler sb = new StringBundler();

			for (Object line : (List<?>)cdataValue) {
				sb.append(line);
			}

			String string = sb.toString();

			return string.trim();
		}

		return null;
	}

}