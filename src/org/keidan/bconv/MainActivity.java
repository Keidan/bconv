package org.keidan.bconv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class MainActivity extends ActivityPopup implements OnClickListener,
    OnKeyListener {

  private static double BIT_SIZE          = 8.0;

  private EditText      editTextOctets    = null;
  private EditText      editTextBits      = null;
  private Button        buttonConvert     = null;
  private ToggleButton  buttonTypeChoice  = null;
  private Spinner       spinnerUnitBits   = null;
  private Spinner       spinnerUnitOctets = null;
  private KeyListener   klBits            = null;
  private KeyListener   klOctets          = null;
  

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState, R.layout.activity_main);
    
    editTextOctets = (EditText) findViewById(R.id.editTextOctets);
    editTextBits = (EditText) findViewById(R.id.editTextBits);
    buttonConvert = (Button) findViewById(R.id.buttonConvert);
    buttonTypeChoice = (ToggleButton) findViewById(R.id.buttonTypeChoice);

    buttonConvert.setOnKeyListener(this);
    buttonTypeChoice.setOnKeyListener(this);

    spinnerUnitBits = getSpinner(R.id.spinnerUnitBits, Unit.Bit, Unit.Kb,
        Unit.Mb, Unit.Gb, Unit.Tb);
    spinnerUnitOctets = getSpinner(R.id.spinnerUnitOctets, Unit.Octet, Unit.Ko,
        Unit.Mo, Unit.Go, Unit.To);
    spinnerUnitBits.setSelection(Unit.Mb.getIndex());
    spinnerUnitOctets.setSelection(Unit.Ko.getIndex());

    klBits = editTextBits.getKeyListener();
    klOctets = editTextOctets.getKeyListener();
    editTextOctets.setKeyListener(null);
  }

  private Spinner getSpinner(final int id, final Unit... units) {
    final List<Unit> array = new ArrayList<Unit>(Arrays.asList(units));
    final ArrayAdapter<Unit> adapter = new ArrayAdapter<Unit>(this,
        android.R.layout.simple_spinner_item, array);
    adapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    final Spinner sItems = (Spinner) findViewById(id);
    sItems.setAdapter(adapter);
    return sItems;
  }

  @Override
  public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && v.getId() == R.id.buttonConvert) {
      onClick(buttonConvert);
      return true;
    }
    return false;
  }

  @Override
  public void onClick(final View v) {
    if (v.getId() == R.id.buttonClose)
      onBackPressed();
    else if (v.getId() == R.id.buttonClear) {
      editTextOctets.setText(R.string.empty);
      editTextBits.setText(R.string.empty);
    } else if (v.getId() == R.id.buttonTypeChoice) {
      final boolean on = buttonTypeChoice.isChecked();
      if (klBits == null)
        klBits = editTextBits.getKeyListener();
      if (klOctets == null)
        klOctets = editTextOctets.getKeyListener();
      if (!on) {
        editTextOctets.setKeyListener(null);
        editTextBits.setKeyListener(klBits);
        editTextBits.requestFocus();
      } else {
        editTextOctets.setKeyListener(klOctets);
        editTextBits.setKeyListener(null);
        editTextOctets.requestFocus();
      }
    } else if (v.getId() == R.id.buttonConvert) {
      final boolean on = buttonTypeChoice.isChecked();
      if (!on) {
        try {
          final String result = editTextBits.getText().toString();
          double n = Double.parseDouble(result);
          Unit unit = (Unit) spinnerUnitBits.getSelectedItem();
          n *= unit.getValue();

          double d = (n / BIT_SIZE);
          unit = (Unit) spinnerUnitOctets.getSelectedItem();
          d /= unit.getValue();
          editTextOctets.setText("" + d);
        } catch (final Exception e) {
          showError("Unable to convert bytes/s to octets/s without a valid value!");
        }
      } else {
        try {
          final String result = editTextOctets.getText().toString();
          double n = Double.parseDouble(result);
          Unit unit = (Unit) spinnerUnitOctets.getSelectedItem();
          n *= unit.getValue();

          double d = (n * BIT_SIZE);
          unit = (Unit) spinnerUnitBits.getSelectedItem();
          d /= unit.getValue();
          editTextBits.setText("" + d);
        } catch (final Exception e) {
          showError("Unable to convert octets/s to bytes/s without a valid value!");
        }
      }
    }
  }
  
  

  private void showError(final String message) {
    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle("Error");
    alertDialog.setMessage(message);
    alertDialog.setIcon(android.R.drawable.ic_delete);
    alertDialog.setNeutralButton("OK", null);
    final AlertDialog dlg = alertDialog.create();
    dlg.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
      @Override
      public boolean onKey(final DialogInterface dialog, final int keyCode,
          final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
          // disable the back button
        }
        return true;
      }
    });
    dlg.show();
  }

}
