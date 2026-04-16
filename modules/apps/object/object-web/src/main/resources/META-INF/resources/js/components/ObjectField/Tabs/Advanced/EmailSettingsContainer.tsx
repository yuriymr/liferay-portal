/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {Input, Toggle} from '@liferay/object-js-components-web';
import React from 'react';

import {
	normalizeFieldSettings,
	updateFieldSettings,
} from '../../../../utils/fieldSettings';

interface EmailSettingsContainerProps {
	onSubmit?: (values?: Partial<ObjectField>) => void;
	setValues: (value: Partial<ObjectField>) => void;
	values: Partial<ObjectField>;
}

export function EmailSettingsContainer({
	onSubmit,
	setValues,
	values,
}: EmailSettingsContainerProps) {
	const settings = normalizeFieldSettings(values.objectFieldSettings);

	const updateSettings = (
		objectFieldSetting: ObjectFieldSetting,
		submit = true
	) => {
		const objectFieldSettings = updateFieldSettings(
			values.objectFieldSettings,
			objectFieldSetting
		);

		setValues({objectFieldSettings});

		if (submit) {
			onSubmit?.({
				...values,
				objectFieldSettings,
			});
		}
	};

	return (
		<>
			<Toggle
				label={Liferay.Language.get('autocomplete')}
				name="autocomplete"
				onToggle={(autocomplete) => {
					updateSettings({
						name: 'autocomplete',
						value: String(autocomplete),
					});
				}}
				toggled={
					settings.autocomplete === true ||
					settings.autocomplete === 'true'
				}
			/>

			<Input
				component="textarea"
				label={Liferay.Language.get('domains')}
				name="domains"
				onBlur={() => onSubmit?.()}
				onChange={({target: {value}}) => {
					updateSettings({
						name: 'domains',
						value,
					}, false);
				}}
				placeholder="liferay.com, gmail.com"
				value={(settings.domains as string) ?? ''}
			/>
		</>
	);
}
