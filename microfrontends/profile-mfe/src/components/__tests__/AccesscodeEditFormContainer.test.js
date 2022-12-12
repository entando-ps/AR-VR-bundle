import React from 'react';
import { fireEvent, render, wait } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { apiAccesscodeGet, apiAccesscodePut } from 'api/accesscodes';
import AccesscodeEditFormContainer from 'components/AccesscodeEditFormContainer';
import 'i18n/__mocks__/i18nMock';
import { accesscodeMockEdit as accesscodeMock } from 'components/__mocks__/accesscodeMocks';

const configMock = {
  systemParams: {
    api: {
      'accesscode-api': {
        url: '',
      },
    },
  },
};

jest.mock('api/accesscodes');

jest.mock('auth/withKeycloak', () => {
  const withKeycloak = Component => {
    return props => (
      <Component
        {...props} // eslint-disable-line react/jsx-props-no-spreading
        keycloak={{
          initialized: true,
          authenticated: true,
        }}
      />
    );
  };

  return withKeycloak;
});

describe('AccesscodeEditFormContainer', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const errorMessageKey = 'error.dataLoading';
  const successMessageKey = 'common.dataSaved';

  const onErrorMock = jest.fn();
  const onUpdateMock = jest.fn();

  it('loads data', async () => {
    apiAccesscodeGet.mockImplementation(() => Promise.resolve(accesscodeMock));
    const { queryByText } = render(
      <AccesscodeEditFormContainer
        id="1"
        onError={onErrorMock}
        onUpdate={onUpdateMock}
        config={configMock}
      />
    );

    await wait(() => {
      expect(apiAccesscodeGet).toHaveBeenCalledTimes(1);
      expect(apiAccesscodeGet).toHaveBeenCalledWith('', '1');
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
      expect(onErrorMock).toHaveBeenCalledTimes(0);
    });
  }, 7000);

  it('saves data', async () => {
    apiAccesscodeGet.mockImplementation(() => Promise.resolve(accesscodeMock));
    apiAccesscodePut.mockImplementation(() => Promise.resolve(accesscodeMock));

    const { findByTestId, queryByText } = render(
      <AccesscodeEditFormContainer
        id="1"
        onError={onErrorMock}
        onUpdate={onUpdateMock}
        config={configMock}
      />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiAccesscodePut).toHaveBeenCalledTimes(1);
      expect(apiAccesscodePut).toHaveBeenCalledWith('', accesscodeMock.id, accesscodeMock);
      expect(queryByText(successMessageKey)).toBeInTheDocument();
      expect(onErrorMock).toHaveBeenCalledTimes(0);
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
    });
  }, 7000);

  it('shows an error if data is not successfully loaded', async () => {
    apiAccesscodeGet.mockImplementation(() => Promise.reject());
    const { queryByText } = render(
      <AccesscodeEditFormContainer
        id="1"
        onError={onErrorMock}
        onUpdate={onUpdateMock}
        config={configMock}
      />
    );

    await wait(() => {
      expect(apiAccesscodeGet).toHaveBeenCalledTimes(1);
      expect(apiAccesscodeGet).toHaveBeenCalledWith('', '1');
      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(queryByText(errorMessageKey)).toBeInTheDocument();
      expect(queryByText(successMessageKey)).not.toBeInTheDocument();
    });
  }, 7000);

  it('shows an error if data is not successfully saved', async () => {
    apiAccesscodeGet.mockImplementation(() => Promise.resolve(accesscodeMock));
    apiAccesscodePut.mockImplementation(() => Promise.reject());
    const { findByTestId, getByText } = render(
      <AccesscodeEditFormContainer id="1" onError={onErrorMock} config={configMock} />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiAccesscodeGet).toHaveBeenCalledTimes(1);
      expect(apiAccesscodeGet).toHaveBeenCalledWith('', '1');

      expect(apiAccesscodePut).toHaveBeenCalledTimes(1);
      expect(apiAccesscodePut).toHaveBeenCalledWith('', accesscodeMock.id, accesscodeMock);

      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(getByText(errorMessageKey)).toBeInTheDocument();
    });
  }, 7000);
});
