/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayAutocomplete from '@clayui/autocomplete';
import {ReactFieldBase as FieldBase} from 'dynamic-data-mapping-form-field-type/api';
import React, {useEffect, useMemo, useState} from 'react';

interface EmailProps {
	autocomplete?: boolean | string;
	displayErrors?: boolean;
	domains?: string;
	errorMessage?: string;
	id?: string;
	label?: string;
	locale?: Liferay.Language.Locale;
	name: string;
	onBlur?: React.FocusEventHandler<HTMLInputElement>;
	onChange?: (event: {target: {value: string}}) => void;
	onFocus?: React.FocusEventHandler<HTMLInputElement>;
	predefinedValue?: string;
	readOnly?: boolean;
	required?: boolean;
	tip?: string;
	valid?: boolean;
	value?: string;
}

export default function Email({
	autocomplete,
	displayErrors,
	domains,
	errorMessage,
	id,
	label,
	locale,
	name,
	onBlur,
	onChange,
	onFocus,
	predefinedValue = '',
	readOnly,
	required,
	tip,
	valid,
	value: initialValue,
	...otherProps
}: EmailProps) {
	const [value, setValue] = useState(initialValue ?? predefinedValue);
	const [visible, setVisible] = useState(false);

	useEffect(() => {
		setValue(initialValue ?? predefinedValue);
	}, [initialValue, predefinedValue]);

	const autocompleteEnabled =
		autocomplete === true || autocomplete === 'true';

	const domainItems = useMemo(() => {
		if (!domains) {
			return [];
		}

		return domains.split(
			/[,\n]+/
		).map(
			(domain) => domain.trim().replace(/^@/, '').toLowerCase()
		).filter(
			Boolean
		);
	}, [domains]);

	const atIndex = value.indexOf('@');
	const localPart = atIndex >= 0 ? value.slice(0, atIndex) : '';
	const domainPart =
		atIndex >= 0 ? value.slice(atIndex + 1).toLowerCase() : '';
	const suggestions =
		autocompleteEnabled && atIndex >= 0 && localPart
			? domainItems.filter((domain) => domain.startsWith(domainPart))
			: [];

	const handleChange = (nextValue: string) => {
		setValue(nextValue);
		setVisible(
			autocompleteEnabled &&
				nextValue.includes('@') &&
				domainItems.length > 0
		);

		onChange?.({target: {value: nextValue}});
	};

	return (
		<FieldBase
			{...otherProps}
			displayErrors={displayErrors}
			errorMessage={errorMessage}
			id={id}
			label={label}
			name={name}
			readOnly={readOnly}
			required={required}
			tip={tip}
			valid={valid}
		>
			<ClayAutocomplete>
				<ClayAutocomplete.Input
					aria-required={required}
					disabled={readOnly}
					dir={locale ? Liferay.Language.direction[locale] : ''}
					id={id ?? name}
					lang={locale?.replaceAll('_', '-')}
					name={name}
					onBlur={(event) => {
						setTimeout(() => setVisible(false), 200);
						onBlur?.(event);
					}}
					onChange={(event) => handleChange(event.target.value)}
					onFocus={(event) => {
						setVisible(suggestions.length > 0);
						onFocus?.(event);
					}}
					type="email"
					value={value}
				/>

				<ClayAutocomplete.DropDown
					active={!readOnly && visible && suggestions.length > 0}
					onSetActive={setVisible}
				>
					<ul className="list-unstyled">
						{suggestions.map((domain) => {
							const suggestion = `${localPart}@${domain}`;

							return (
								<ClayAutocomplete.Item
									key={domain}
									onClick={() => handleChange(suggestion)}
									value={domain}
								/>
							);
						})}

					</ul>
				</ClayAutocomplete.DropDown>
			</ClayAutocomplete>
		</FieldBase>
	);
}
