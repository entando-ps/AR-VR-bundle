import PropTypes from 'prop-types';

const conferenceType = PropTypes.shape({
  id: PropTypes.number,
  nome: PropTypes.string,
});

export default conferenceType;
