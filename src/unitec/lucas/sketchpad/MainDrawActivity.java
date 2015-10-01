//Main activity class
//Author: Yang Zhang (Lucas)

package unitec.lucas.sketchpad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainDrawActivity extends Activity {
	
	private DrawView drawView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sketchpad);
		
		drawView = (DrawView) findViewById(R.id.paint_view);
	}

	//	control color button selection, and set color in the view.
	//	Also controls the buttons animation.

	public void selectColor(View v) {
		findViewById(R.id.silver_button).setSelected(false);
		findViewById(R.id.cyan_button).setSelected(false);
		findViewById(R.id.lime_button).setSelected(false);
		findViewById(R.id.yellow_button).setSelected(false);
		findViewById(R.id.violet_button).setSelected(false);
		findViewById(R.id.red_button).setSelected(false);
		findViewById(R.id.blue_button).setSelected(false);
		findViewById(R.id.green_button).setSelected(false);
		findViewById(R.id.orange_button).setSelected(false);		
		findViewById(R.id.purple_button).setSelected(false);
		switch (v.getId()) {
		case R.id.silver_button:
			findViewById(R.id.silver_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#C0C0C0"));
			break;
		case R.id.cyan_button:
			findViewById(R.id.cyan_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#00FFFF"));
			break;
		case R.id.lime_button:
			findViewById(R.id.lime_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#00FF00"));
			break;				
		case R.id.yellow_button:
			findViewById(R.id.yellow_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#FFFF00"));
			break;
		case R.id.violet_button:
			findViewById(R.id.violet_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#8D38C9"));
			break;
		case R.id.red_button:
			findViewById(R.id.red_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#FF0000"));
			break;
		case R.id.blue_button:
			findViewById(R.id.blue_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#0000FF"));
			break;
		case R.id.green_button:
			findViewById(R.id.green_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#008000"));
			break;
		case R.id.orange_button:
			findViewById(R.id.orange_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#FFA500"));
			break;
		case R.id.purple_button:
			findViewById(R.id.purple_button).setSelected(true);
			drawView.setPaintColor(Color.parseColor("#800080"));
			break;

		}
	}

	//	control shape button selection, and set shape in the view.
	//	Also controls the buttons animation.

	public void selectShape(View v) {
		findViewById(R.id.lineShape).setSelected(false);
		findViewById(R.id.triangleShape).setSelected(false);
		findViewById(R.id.circleShape).setSelected(false);
		findViewById(R.id.rectangelShape).setSelected(false);
		switch (v.getId()) {
		case R.id.lineShape:
			findViewById(R.id.lineShape).setSelected(true);
			drawView.setPaintShape(ShapeConstant.LINE);
			break;
		case R.id.triangleShape:
			findViewById(R.id.triangleShape).setSelected(true);
			drawView.setPaintShape(ShapeConstant.TRIANGLE);
			break;
		case R.id.circleShape:
			findViewById(R.id.circleShape).setSelected(true);
			drawView.setPaintShape(ShapeConstant.CIRCLE);
			break;				
		case R.id.rectangelShape:
			findViewById(R.id.rectangelShape).setSelected(true);
			drawView.setPaintShape(ShapeConstant.RECTANGLE);
			break;
		}
	}
	
	//	control function button selection.

	public void selectFunction(View v) {
		switch (v.getId()) {
		case R.id.saveBtn:
			drawView.save();			
			break;
		case R.id.resetBtn:
			//System.out.println("clear");
			drawView.clearScreen();			
			break;
		case R.id.emailBtn:
			Intent emailIntent = drawView.email();
			startActivity(Intent.createChooser(emailIntent, "Send mail"));		//Launch a new activity with email Intent
			break;				
		case R.id.exitBtn:
			drawView.exit();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_draw, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
