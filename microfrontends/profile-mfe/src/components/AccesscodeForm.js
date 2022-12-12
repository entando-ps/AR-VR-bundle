import React, { PureComponent } from 'react';
import PropTypes from 'prop-types';
import { formValues, formTouched, formErrors } from 'components/__types__/accesscode';
import { withFormik } from 'formik';
import { withTranslation } from 'react-i18next';
import { withStyles } from '@material-ui/core/styles';
import { compose } from 'recompose';
import * as Yup from 'yup';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import InputLabel from '@material-ui/core/InputLabel';
import Select from '@material-ui/core/Select';
import ConfirmationDialogTrigger from 'components/common/ConfirmationDialogTrigger';

const styles = theme => ({
  root: {
    margin: theme.spacing(3),
  },
  textField: {
    width: '100%',
  },
});
class AccesscodeForm extends PureComponent {
  constructor(props) {
    super(props);
    this.handleConfirmationDialogAction = this.handleConfirmationDialogAction.bind(this);
  }

  handleConfirmationDialogAction(action) {
    const { onDelete, values } = this.props;
    switch (action) {
      case ConfirmationDialogTrigger.CONFIRM: {
        onDelete(values);
        break;
      }
      default:
        break;
    }
  }

  render() {
    const {
      classes,
      values,
      touched,
      errors,
      handleChange,
      handleBlur,
      handleSubmit: formikHandleSubmit,
      onDelete,
      onCancelEditing,
      isSubmitting,
      t,
    } = this.props;

    const getHelperText = field => (errors[field] && touched[field] ? errors[field] : '');

    const handleSubmit = e => {
      e.stopPropagation(); // avoids double submission caused by react-shadow-dom-retarget-events
      formikHandleSubmit(e);
    };

    return (
      <form onSubmit={handleSubmit} className={classes.root} data-testid="accesscode-form">
        <Grid container spacing={2}>
          <input type="hidden" id="accesscode-id" data-testid="accesscode-id" value={values.id} />
          <Grid item xs={12} sm={6}>
            <InputLabel htmlFor="accesscode-age">{t('entities.accesscode.age')}</InputLabel>
            <Select
              native
              id="accesscode-age"
              error={errors.age && touched.age}
              className={classes.textField}
              value={values.age}
              name="age"
              onChange={handleChange}
            >
              {/* eslint-disable-next-line jsx-a11y/control-has-associated-label */}
              <option value="" />
              <option value="CHILD">CHILD</option>
              <option value="TEEN">TEEN</option>
              <option value="YOUNG_ADULT">YOUNG_ADULT</option>
              <option value="ADULT">ADULT</option>
              <option value="ELDER">ELDER</option>
            </Select>
          </Grid>
          <Grid item xs={12} sm={6}>
            <InputLabel htmlFor="accesscode-gender">{t('entities.accesscode.gender')}</InputLabel>
            <Select
              native
              id="accesscode-gender"
              error={errors.gender && touched.gender}
              className={classes.textField}
              value={values.gender}
              name="gender"
              onChange={handleChange}
            >
              {/* eslint-disable-next-line jsx-a11y/control-has-associated-label */}
              <option value="" />
              <option value="FEMALE">FEMALE</option>
              <option value="MALE">MALE</option>
              <option value="UNKNOWN">UNKNOWN</option>
            </Select>
          </Grid>
          <Grid item xs={12} sm={6}>
            <InputLabel htmlFor="accesscode-race">{t('entities.accesscode.race')}</InputLabel>
            <Select
              native
              id="accesscode-race"
              error={errors.race && touched.race}
              className={classes.textField}
              value={values.race}
              name="race"
              onChange={handleChange}
            >
              {/* eslint-disable-next-line jsx-a11y/control-has-associated-label */}
              <option value="" />
              <option value="EAST_ASIAN">EAST_ASIAN</option>
              <option value="SOUTHEAST_ASIAN">SOUTHEAST_ASIAN</option>
              <option value="INDIAN">INDIAN</option>
              <option value="BLACK">BLACK</option>
              <option value="WHITE">WHITE</option>
              <option value="MIDDLE_EASTERN">MIDDLE_EASTERN</option>
              <option value="LATINO_ISPANIC">LATINO_ISPANIC</option>
            </Select>
          </Grid>
          <Grid item xs={12} sm={6}>
            <InputLabel htmlFor="accesscode-sentiment">
              {t('entities.accesscode.sentiment')}
            </InputLabel>
            <Select
              native
              id="accesscode-sentiment"
              error={errors.sentiment && touched.sentiment}
              className={classes.textField}
              value={values.sentiment}
              name="sentiment"
              onChange={handleChange}
            >
              {/* eslint-disable-next-line jsx-a11y/control-has-associated-label */}
              <option value="" />
              <option value="ANGRY">ANGRY</option>
              <option value="DISGUST">DISGUST</option>
              <option value="FEAR">FEAR</option>
              <option value="HAPPY">HAPPY</option>
              <option value="SAD">SAD</option>
              <option value="SURPRISE">SURPRISE</option>
              <option value="NEUTRAL">NEUTRAL</option>
            </Select>
          </Grid>
          {onDelete && (
            <ConfirmationDialogTrigger
              onCloseDialog={this.handleConfirmationDialogAction}
              dialog={{
                title: t('entities.accesscode.deleteDialog.title'),
                description: t('entities.accesscode.deleteDialog.description'),
                confirmLabel: t('common.yes'),
                discardLabel: t('common.no'),
              }}
              Renderer={({ onClick }) => (
                <Button onClick={onClick} disabled={isSubmitting}>
                  {t('common.delete')}
                </Button>
              )}
            />
          )}

          <Button onClick={onCancelEditing} disabled={isSubmitting} data-testid="cancel-btn">
            {t('common.cancel')}
          </Button>

          <Button type="submit" color="primary" disabled={isSubmitting} data-testid="submit-btn">
            {t('common.save')}
          </Button>
        </Grid>
      </form>
    );
  }
}

AccesscodeForm.propTypes = {
  classes: PropTypes.shape({
    root: PropTypes.string,
    textField: PropTypes.string,
    submitButton: PropTypes.string,
    button: PropTypes.string,
    downloadAnchor: PropTypes.string,
  }),
  values: formValues,
  touched: formTouched,
  errors: formErrors,
  handleChange: PropTypes.func.isRequired,
  handleBlur: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  onDelete: PropTypes.func,
  onCancelEditing: PropTypes.func,
  isSubmitting: PropTypes.bool.isRequired,
  t: PropTypes.func.isRequired,
  i18n: PropTypes.shape({ language: PropTypes.string }).isRequired,
};

AccesscodeForm.defaultProps = {
  onCancelEditing: () => {},
  classes: {},
  values: {},
  touched: {},
  errors: {},
  onDelete: null,
};

const emptyAccesscode = {
  id: '',
  age: '',
  gender: '',
  race: '',
  sentiment: '',
};

const validationSchema = Yup.object().shape({
  id: Yup.number(),
  age: Yup.string(),
  gender: Yup.string(),
  race: Yup.string(),
  sentiment: Yup.string(),
});

const formikBag = {
  mapPropsToValues: ({ accesscode }) => accesscode || emptyAccesscode,

  enableReinitialize: true,

  validationSchema,

  handleSubmit: (values, { setSubmitting, props: { onSubmit } }) => {
    onSubmit(values);
    setSubmitting(false);
  },

  displayName: 'AccesscodeForm',
};

export default compose(
  withStyles(styles, { withTheme: true }),
  withTranslation(),
  withFormik(formikBag)
)(AccesscodeForm);
