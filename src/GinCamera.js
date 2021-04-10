import PropTypes from 'prop-types';
import { requireNativeComponent, ViewPropTypes } from 'react-native';
var viewProps = {
  name: 'GinCamera',
  propTypes: {
    filter: PropTypes.string,
    ...ViewPropTypes,
  }
}
module.exports = requireNativeComponent('GinCamera', viewProps);