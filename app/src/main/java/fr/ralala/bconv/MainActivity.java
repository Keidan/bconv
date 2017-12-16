package fr.ralala.bconv;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *******************************************************************************
 * <p><b>Project bconv</b><br/>
 * Main activity
 * </p>
 * @author Keidan
 *
 *******************************************************************************
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {
  private EditText mEditTextOctets = null;
  private EditText mEditTextBits = null;
  private ToggleButton mButtonTypeChoice = null;
  private Spinner mSpinnerUnitBits   = null;
  private Spinner mSpinnerUnitOctets = null;

  /**
   * Called when the activity is created.
   * @param savedInstanceState The savec instance state.
   */
  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_LEFT_ICON);
    setContentView(R.layout.activity_main);
    setTitle(getResources().getText(R.string.title));
    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_launcher);
    overrideTransition(true);

    mEditTextOctets = findViewById(R.id.editTextOctets);
    mEditTextBits = findViewById(R.id.editTextBits);
    mButtonTypeChoice = findViewById(R.id.buttonTypeChoice);

    mSpinnerUnitBits = createSpinner(R.id.spinnerUnitBits, Unit.BIT, Unit.KB,  Unit.MB, Unit.GB, Unit.TB);
    mSpinnerUnitOctets = createSpinner(R.id.spinnerUnitOctets, Unit.OCTET, Unit.KO, Unit.MO, Unit.GO, Unit.TO);
    mSpinnerUnitBits.setSelection(Unit.MB.index());
    mSpinnerUnitOctets.setSelection(Unit.KO.index());
  }

  /**
   * Called when the user press on the back button.
   */
  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overrideTransition(false);
    finish();
  }

  /**
   * Called when the activity is destroyed.
   */
  @Override
  public void onDestroy() {
    super.onDestroy();
    overrideTransition(false);
  }

  /**
   * Used to handle the ENTER key pressed
   * @param v See javadoc
   * @param actionId See javadoc
   * @param event See javadoc
   * @return See javadoc
   */
  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
    boolean handled = false;
    if (actionId == EditorInfo.IME_ACTION_SEND) {
      convert();
      handled = true;
    }
    return handled;
  }

  /**
   * Converts the entries.
   */
  private void convert() {
    final boolean on = mButtonTypeChoice.isChecked();
    double bisSize = 8.0;
    if (!on) {
      try {
        final String result = mEditTextBits.getText().toString();
        double n = Double.parseDouble(result);
        Unit unit = (Unit) mSpinnerUnitBits.getSelectedItem();
        n *= unit.value();

        double d = (n / bisSize);
        unit = (Unit) mSpinnerUnitOctets.getSelectedItem();
        d /= unit.value();
        mEditTextOctets.setText(String.valueOf(d));
      } catch (final Exception e) {
        shakeError(mEditTextBits, "Unable to convert bytes/s to octets/s without a valid value!");
      }
    } else {
      try {
        final String result = mEditTextOctets.getText().toString();
        double n = Double.parseDouble(result);
        Unit unit = (Unit) mSpinnerUnitOctets.getSelectedItem();
        n *= unit.value();

        double d = (n * bisSize);
        unit = (Unit) mSpinnerUnitBits.getSelectedItem();
        d /= unit.value();
        mEditTextBits.setText(String.valueOf(d));
      } catch (final Exception e) {
        shakeError(mEditTextOctets, "Unable to convert octets/s to bytes/s without a valid value!");
      }
    }
  }

  /**
   * Override the transition.
   * @param open Open or false.
   */
  private void overrideTransition(boolean open) {
    if(open) overridePendingTransition(R.anim.enter_in, R.anim.enter_out);
    else overridePendingTransition(R.anim.leave_in, R.anim.leave_out);
  }

  /**
   * Creates a spinner item.
   * @param id The spinner id.
   * @param units The list to add.
   * @return The created spinner.
   */
  private Spinner createSpinner(final int id, final Unit... units) {
    final List<Unit> array = new ArrayList<>(Arrays.asList(units));
    final ArrayAdapter<Unit> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, array);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    final Spinner sItems = findViewById(id);
    sItems.setAdapter(adapter);
    return sItems;
  }

  /**
   * Called when the user click on a button.
   * @param v The button clicked.
   */
  @Override
  public void onClick(final View v) {
    if (v.getId() == R.id.buttonTypeChoice) {
      final boolean on = mButtonTypeChoice.isChecked();
      if (!on) {
        mEditTextBits.requestFocus();
      } else {
        mEditTextOctets.requestFocus();
      }
    } else if (v.getId() == R.id.buttonConvert) {
      convert();
    }
  }

  /**
   * Called to create the option menu.
   * @param menu The owner menu.
   * @return boolean
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  /**
   * Called when the user click on an option menu.
   * @param item The selected option item.
   * @return boolean
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.action_clear) {
      mEditTextOctets.setText("");
      mEditTextBits.setText("");
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * Shake a view on error.
   * @param owner The owner view.
   * @param errText The error text.
   */
  public static void shakeError(TextView owner, String errText) {
    TranslateAnimation shake = new TranslateAnimation(0, 10, 0, 0);
    shake.setDuration(500);
    shake.setInterpolator(new CycleInterpolator(5));
    if(owner != null) {
      if(errText != null)
        owner.setError(errText);
      owner.clearAnimation();
      owner.startAnimation(shake);
    }
  }
}
