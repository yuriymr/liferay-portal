/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {fireEvent, render, screen} from '@testing-library/react';
import React from 'react';

import Email from '../js/Email/Email';

describe('Email object field', () => {
	it('shows domain suggestions when the value contains @', () => {
		render(
			<Email
				autocomplete="true"
				domains="liferay.com,gmail.com"
				label="Email"
				name="email"
				onChange={() => {}}
			/>
		);

		fireEvent.change(screen.getByRole('textbox'), {
			target: {value: 'user@'},
		});

		expect(screen.getByText('liferay.com')).toBeInTheDocument();
		expect(screen.getByText('gmail.com')).toBeInTheDocument();
	});

	it('appends the selected domain to the local part', () => {
		const onChange = jest.fn();

		render(
			<Email
				autocomplete="true"
				domains="liferay.com"
				label="Email"
				name="email"
				onChange={onChange}
			/>
		);

		fireEvent.change(screen.getByRole('textbox'), {
			target: {value: 'user@'},
		});

		fireEvent.click(screen.getByText('liferay.com'));

		expect(onChange).toHaveBeenLastCalledWith({
			target: {value: 'user@liferay.com'},
		});
	});

	it('does not show suggestions when autocomplete is disabled', () => {
		render(
			<Email
				autocomplete="false"
				domains="liferay.com"
				label="Email"
				name="email"
				onChange={() => {}}
			/>
		);

		fireEvent.change(screen.getByRole('textbox'), {
			target: {value: 'user@'},
		});

		expect(screen.queryByText('liferay.com')).not.toBeInTheDocument();
	});
});
