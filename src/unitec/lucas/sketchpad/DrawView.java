//Main activity class
//Author: Yang Zhang (Lucas)
//With reference to John Casey's original DrawView class

package unitec.lucas.sketchpad;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener
{
	// DrawView class developed by John Casey 06/03/2014 
	
	final static Paint paint = new Paint();
	
	Bitmap offScreenBitmap;
	Canvas offScreenCanvas;
	static int shape;
	
	boolean isMoved = false;
	
	float origanalX;
	float origanalY;
	float finalX;
	float finalY;
	
	File file;
	int fileNumber = 1;
	
	String email = "zhangysd@hotmail.com";					//use your own email address
	
	

	// define additional constructors so that DrawView will work with out layout file
	
	public DrawView(Context context) {
		super(context);		
		setup();
	}

	public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);		
		setup();
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);		
		setup();
	}

	public void setup()
	{
		setOnTouchListener(this); // define event listener and start intercepting events 
	}
	
	//Set the paint shape according to the shape use selected
	
	public void setPaintShape(int selectedShape)
	{
		shape = selectedShape;
		
	}
	
	//Set the paint color according to the color use selected
	
	public void setPaintColor(int color)
	{
		paint.setColor(color);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// draw the off screen bitmap
		canvas.drawBitmap(offScreenBitmap, 0, 0, paint);
	}
	
	//handle touch actions, decide what to draw based on the user's action
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// get the x,y coordinates of the MotionEvent.ACTION_MOVE event
		
		switch (event.getAction()) {
		     case MotionEvent.ACTION_MOVE:
		    	 isMoved = true;
		         break;
		     case MotionEvent.ACTION_DOWN:
		    	 isMoved = false;
		    	 origanalX = event.getX();
		 		 origanalY = event.getY();
		    	 break;
		     case MotionEvent.ACTION_UP:
		    	 if(isMoved){ 					//decide if the coordinate moves
		    		 finalX = event.getX();
			 		 finalY = event.getY();
			 		 drawShape(origanalX, origanalY, finalX, finalY); 
		    	 }else{
		    		 drawShape(origanalX, origanalY);
		    	 }
		         break;
	     }
				
		return true; // actually consume the event
	}
	
	//draw line
	
	private void drawLine(float origanalX, float origanalY, float finalX, float finalY){
		paint.setStrokeWidth((float) 5.0);  
		offScreenCanvas.drawLine(origanalX, origanalY, finalX, finalY, paint);	
		invalidate();
	}
	
	//draw circle
	
	private void drawCircle(float x, float y, float radius){  
		offScreenCanvas.drawCircle(x, y, radius, paint);	
		invalidate();
	}
	
	//draw rectangle
	
	private void drawRect(float originalX, float originalY, float finalX, float finalY){  
		offScreenCanvas.drawRect(originalX, originalY, finalX, finalY, paint);
		invalidate();
	}
	
	//draw default triangle with original point
	
	private void drawTriangle(float x, float y) {
		float perpendicularLength = (float) Math.sqrt(Math.pow(ShapeConstant.DEFAULT_TRIANGLE_LENGTH, 2)*3/4);
		Path path = new Path();  
        path.moveTo(x, y); 
        path.lineTo(x-ShapeConstant.DEFAULT_TRIANGLE_LENGTH/2, y+perpendicularLength);
        path.lineTo(x+ShapeConstant.DEFAULT_TRIANGLE_LENGTH/2, y+perpendicularLength);        
        path.close(); 
        offScreenCanvas.drawPath(path, paint);
        invalidate(); 
	}
	
	//draw triangle with the size of user selected, I use two drawTriangle because triangle is 
	//much more complicated to draw.
	
	private void drawTriangle(float x, float y, float finalX, float finalY) {	
		float distant = getDistance(x, y, finalX, finalY);
		float perpendicularLength = Math.abs(y-finalY);
		float sideLength = (float) Math.sqrt(Math.pow(distant, 2) - Math.pow(perpendicularLength, 2) );
		Path path = new Path();  
        path.moveTo(x, y); 
        path.lineTo(finalX, finalY);
        if(finalX >= x){
        	path.lineTo(finalX-sideLength*2, finalY);
        }else{
        	path.lineTo(finalX+sideLength*2, finalY);
        }         
        path.close(); 
        offScreenCanvas.drawPath(path, paint);
        invalidate();      
	}
	
	//calculate the distant between two points
	
	private float getDistance(float originalX, float originalY, float finalX, float finalY) {
		float distant = (float) Math.sqrt(Math.pow(originalX - finalX, 2) + Math.pow(originalY - finalY, 2) );
		return distant;
	}
	
	//handle all drawing actions when dragging occurs, set the shape's size to the size user selected
		
	private void drawShape(float originalX, float originalY, float finalX, float finalY) {
		switch (shape) {
		case ShapeConstant.LINE:
			drawLine(originalX, originalY, finalX, finalY);
			break;
		case ShapeConstant.TRIANGLE:
			drawTriangle(originalX, originalY, finalX, finalY);
			break;
		case ShapeConstant.CIRCLE:
			float distant = getDistance(originalX, originalY, finalX, finalY);
			float radius = distant/2;
			drawCircle((originalX+finalX)/2, (originalY+finalY)/2, radius);
			break;				
		case ShapeConstant.RECTANGLE:
			drawRect(originalX, originalY, finalX, finalY);
			break;
		}
		
	}
	
	//handle all drawing actions by default

	private void drawShape(float x, float y) {
		switch (shape) {
		case ShapeConstant.LINE:
			drawLine(x, y, x+ShapeConstant.DEFAULT_LINE_LENGTH, y);
			break;
		case ShapeConstant.TRIANGLE:
			drawTriangle(x, y);
			break;
		case ShapeConstant.CIRCLE:
			drawCircle(x, y, ShapeConstant.DEFAULT_RADIUS);
			break;				
		case ShapeConstant.RECTANGLE:
			drawRect(x, y, x+ShapeConstant.DEFAULT_RECT_LENGTH, y+ShapeConstant.DEFAULT_RECT_LENGTH);
			break;
		}
		
	}
	
	//save the bitmap to storage/sdcard directory and add it to the gallery.
	
	public void save() {  
		
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		System.out.println(path);

		file = new File(path, fileNumber + ".png"); 
		fileNumber++;
		if (file.exists()) { 
			file.delete(); 
		} 
		try { 
			//store the bitmap as .png file in /storage/sdcard folder
			FileOutputStream stream = new FileOutputStream(file); 
			offScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); 
			stream.flush(); 
			stream.close(); 
			//add the picture to the Gallery.
			Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			} catch (FileNotFoundException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (IOException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} 
     }
	
	//send the saved picture via email
	public Intent email() {
		save();
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND); 
		emailIntent.setType("application/image");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, email); 			//email address
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Test Subject"); 	//Subject
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "From My App"); 	//Text Content
		System.out.println(file.getPath());
		//add attachment
		emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getPath()));
		return emailIntent;		
	} 

	
	//clear the screen, re-initialize the bitmap and canvas

	public void clearScreen(){
		offScreenBitmap = Bitmap.createBitmap(getWidth(), getHeight(),  Bitmap.Config.ARGB_8888);  
		offScreenCanvas = new Canvas(offScreenBitmap);    
		invalidate();
	}
	
	//exit the application
	
	public void exit() {   
        System.exit(0);		
	}	
	 

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// create / re-create the off screen bitmap to capture the state of our drawing
		// this operation will reset the user's drawing
		
		offScreenBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		offScreenCanvas = new Canvas(offScreenBitmap);
	}
	
}
