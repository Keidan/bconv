package org.keidan.bconv;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class ActivityPopup extends Activity{

  private int           dx                = 0;
  private int           dy                = 0;
  
  
  protected void onCreate(final Bundle savedInstanceState, int layout_id) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_LEFT_ICON);
    setContentView(layout_id);;
    setTitle(getResources().getText(R.string.title));
    setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
    overrideTransition(true);
    getWindow().setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
  }
  
  private void overrideTransition(boolean open) {
    if(open) overridePendingTransition(R.anim.animation_enter_in, R.anim.animation_enter_out);
    else overridePendingTransition(R.anim.animation_leave_in, R.anim.animation_leave_out);
  }
  

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overrideTransition(false);
    finish();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    overrideTransition(false);
  }
  
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if(event.getAction() == MotionEvent.ACTION_DOWN) {
      dx = (int) event.getX();
      dy = (int) event.getY();
    } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
      int xp = (int) event.getRawX();
      int yp = (int) event.getRawY();
      int sides = (xp - dx);
      int topBot = (yp - dy);
      move((int)sides, (int)topBot);
    }
    return true;
  }
  
  private void move(int x, int y) {
    WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
    layoutParams.x = x;
    layoutParams.y = y;
    getWindow().setAttributes(layoutParams);
  }
}
