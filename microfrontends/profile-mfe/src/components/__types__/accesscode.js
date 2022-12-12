import PropTypes from 'prop-types';

export default PropTypes.shape({
  id: PropTypes.number,
  age: PropTypes.string,
  gender: PropTypes.string,
  race: PropTypes.string,
  sentiment: PropTypes.string,
});

export const formValues = PropTypes.shape({
  id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  age: PropTypes.string,
  gender: PropTypes.string,
  race: PropTypes.string,
  sentiment: PropTypes.string,
});

export const formTouched = PropTypes.shape({
  id: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
  age: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
  gender: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
  race: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
  sentiment: PropTypes.oneOfType([PropTypes.bool, PropTypes.shape()]),
});

export const formErrors = PropTypes.shape({
  id: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
  age: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
  gender: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
  race: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
  sentiment: PropTypes.oneOfType([PropTypes.string, PropTypes.shape()]),
});
