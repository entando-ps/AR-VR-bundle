import { getDefaultOptions, request } from 'api/helpers';

const resource = 'api/accesscodes';

export const apiAccesscodeGet = async (serviceUrl, id) => {
  const url = `${serviceUrl}/${resource}/${id}`;
  const options = {
    ...getDefaultOptions(),
    method: 'GET',
  };
  return request(url, options);
};

export const apiAccesscodePost = async (serviceUrl, accesscode) => {
  const url = `${serviceUrl}/${resource}`;
  const options = {
    ...getDefaultOptions(),
    method: 'POST',
    body: accesscode ? JSON.stringify(accesscode) : null,
  };
  return request(url, options);
};

export const apiAccesscodePut = async (serviceUrl, id, accesscode) => {
  const url = `${serviceUrl}/${resource}/${id}`;
  const options = {
    ...getDefaultOptions(),
    method: 'PUT',
    body: accesscode ? JSON.stringify(accesscode) : null,
  };
  return request(url, options);
};

export const apiAccesscodeDelete = async (serviceUrl, id) => {
  const url = `${serviceUrl}/${resource}/${id}`;
  const options = {
    ...getDefaultOptions(),
    method: 'DELETE',
  };
  return request(url, options);
};
