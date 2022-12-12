import React from 'react';
import '@testing-library/jest-dom/extend-expect';
import { fireEvent, render, wait } from '@testing-library/react';
import i18n from 'i18n/__mocks__/i18nMock';
import { accesscodeMockEdit as accesscodeMock } from 'components/__mocks__/accesscodeMocks';
import AccesscodeForm from 'components/AccesscodeForm';
import { createMuiTheme } from '@material-ui/core';
import { ThemeProvider } from '@material-ui/styles';

const theme = createMuiTheme();

describe('Accesscode Form', () => {
  it('shows form', () => {
    const { getByLabelText, getByTestId } = render(
      <ThemeProvider theme={theme}>
        <AccesscodeForm accesscode={accesscodeMock} />
      </ThemeProvider>
    );

    expect(getByTestId('accesscode-id').value).toBe(accesscodeMock.id.toString());
    expect(getByLabelText('entities.accesscode.age').value).toBe(accesscodeMock.age);
    expect(getByLabelText('entities.accesscode.gender').value).toBe(accesscodeMock.gender);
    expect(getByLabelText('entities.accesscode.race').value).toBe(accesscodeMock.race);
    expect(getByLabelText('entities.accesscode.sentiment').value).toBe(accesscodeMock.sentiment);
  });

  it('submits form', async () => {
    const handleSubmit = jest.fn();
    const { getByTestId } = render(
      <ThemeProvider theme={theme}>
        <AccesscodeForm accesscode={accesscodeMock} onSubmit={handleSubmit} />
      </ThemeProvider>
    );

    const form = getByTestId('accesscode-form');
    fireEvent.submit(form);

    await wait(() => {
      expect(handleSubmit).toHaveBeenCalledTimes(1);
    });
  });
});
