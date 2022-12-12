import React from 'react';
import { fireEvent, render, wait } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import { apiAccesscodePost } from 'api/accesscodes';
import AccesscodeAddFormContainer from 'components/AccesscodeAddFormContainer';
import 'i18n/__mocks__/i18nMock';
import { accesscodeMockAdd as accesscodeMock } from 'components/__mocks__/accesscodeMocks';

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
jest.mock('@material-ui/pickers', () => {
  // eslint-disable-next-line react/prop-types
  const MockPicker = ({ id, value, name, label, onChange }) => {
    const handleChange = event => onChange(event.currentTarget.value);
    return (
      <span>
        <label htmlFor={id}>{label}</label>
        <input id={id} name={name} value={value || ''} onChange={handleChange} />
      </span>
    );
  };
  return {
    ...jest.requireActual('@material-ui/pickers'),
    DateTimePicker: MockPicker,
    DatePicker: MockPicker,
  };
});

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

describe('AccesscodeAddFormContainer', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const errorMessageKey = 'error.dataLoading';
  const successMessageKey = 'common.dataSaved';

  const onErrorMock = jest.fn();
  const onCreateMock = jest.fn();

  it('saves data', async () => {
    apiAccesscodePost.mockImplementation(data => Promise.resolve(data));

    const { findByTestId, findByLabelText, queryByText, rerender } = render(
      <AccesscodeAddFormContainer
        onError={onErrorMock}
        onUpdate={onCreateMock}
        config={configMock}
      />
    );

    const ageField = await findByLabelText('entities.accesscode.age');
    fireEvent.change(ageField, { target: { value: accesscodeMock.age } });
    const genderField = await findByLabelText('entities.accesscode.gender');
    fireEvent.change(genderField, { target: { value: accesscodeMock.gender } });
    const raceField = await findByLabelText('entities.accesscode.race');
    fireEvent.change(raceField, { target: { value: accesscodeMock.race } });
    const sentimentField = await findByLabelText('entities.accesscode.sentiment');
    fireEvent.change(sentimentField, { target: { value: accesscodeMock.sentiment } });
    rerender(
      <AccesscodeAddFormContainer
        onError={onErrorMock}
        onUpdate={onCreateMock}
        config={configMock}
      />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiAccesscodePost).toHaveBeenCalledTimes(1);
      expect(apiAccesscodePost).toHaveBeenCalledWith('', accesscodeMock);

      expect(queryByText(successMessageKey)).toBeInTheDocument();

      expect(onErrorMock).toHaveBeenCalledTimes(0);
      expect(queryByText(errorMessageKey)).not.toBeInTheDocument();
    });
  }, 7000);

  it('shows an error if data is not successfully saved', async () => {
    apiAccesscodePost.mockImplementation(() => Promise.reject());

    const { findByTestId, findByLabelText, queryByText, rerender } = render(
      <AccesscodeAddFormContainer
        onError={onErrorMock}
        onUpdate={onCreateMock}
        config={configMock}
      />
    );

    const ageField = await findByLabelText('entities.accesscode.age');
    fireEvent.change(ageField, { target: { value: accesscodeMock.age } });
    const genderField = await findByLabelText('entities.accesscode.gender');
    fireEvent.change(genderField, { target: { value: accesscodeMock.gender } });
    const raceField = await findByLabelText('entities.accesscode.race');
    fireEvent.change(raceField, { target: { value: accesscodeMock.race } });
    const sentimentField = await findByLabelText('entities.accesscode.sentiment');
    fireEvent.change(sentimentField, { target: { value: accesscodeMock.sentiment } });
    rerender(
      <AccesscodeAddFormContainer
        onError={onErrorMock}
        onUpdate={onCreateMock}
        config={configMock}
      />
    );

    const saveButton = await findByTestId('submit-btn');

    fireEvent.click(saveButton);

    await wait(() => {
      expect(apiAccesscodePost).toHaveBeenCalledTimes(1);
      expect(apiAccesscodePost).toHaveBeenCalledWith('', accesscodeMock);

      expect(queryByText(successMessageKey)).not.toBeInTheDocument();

      expect(onErrorMock).toHaveBeenCalledTimes(1);
      expect(queryByText(errorMessageKey)).toBeInTheDocument();
    });
  }, 7000);
});
