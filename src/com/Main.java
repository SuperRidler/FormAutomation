package com;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

public class Main {

	Robot robocop;
	int screenWidth, screenHeight;
	Rectangle screenRec;
	
	//MOT tick box
	int[][] motSel;
	//Tick (49, 6)
	final String MOT_FILE_NAME = "MOT.png";
	
	//Purpose selection box
	int[][] purposeSel;
	//Select (+135, +1/2/3)
	//Select (+131, +53)
	static int PURPOSE_OPTION = 0;
	final String PURPOSE_FILE_NAME = "Purpose.png";
	
	//Reason text box
	int[][] reasonSel;
	final String REASON_FILE_NAME = "Reason.png";
	static String REASON_TEXT = "Install";
	
	public static void main(String[] args) {
		if(args.length > 0){
			try{
				int x = Integer.parseInt(args[0]);
				if(x==1){
					REASON_TEXT = "Fault";
					PURPOSE_OPTION = 1;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		new Main();
	}
	
	public Main(){
		
		screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		screenRec = new Rectangle(screenWidth, screenHeight);
		
		motSel = getSel(MOT_FILE_NAME);
		purposeSel = getSel(PURPOSE_FILE_NAME);
		reasonSel = getSel(REASON_FILE_NAME);
		
		try {
			robocop = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		
		Runnable run = new Runnable(){

			@Override
			public void run() {
				try{
					while(true){
						System.out.println("Iteration");

						//Get our screen-shot
						BufferedImage bi = getImage();
						
						checkMot(bi);
						
						checkReason(bi);
						
						checkPurpose(bi);
						
						System.out.println("Done");
						Thread.sleep(50L);
					}
				}catch(Exception e){
					System.out.println("Error: ");
					e.printStackTrace();
				}
			}
			
		};
		
		Thread thread = new Thread(run);
		thread.start();
		
	}
	
	public BufferedImage getImage(){
		return robocop.createScreenCapture(screenRec);
	}
	
	public Point getLocationOfMot(BufferedImage bi){
		for(int i=0; i<bi.getWidth(); i++){
			for(int j=0; j<bi.getHeight(); j++){
				if(isMatchMot(bi, i, j)){
					return new Point(i, j);
				}
			}
		}
		return new Point(0, 0);
	}
	
	public boolean isMatchMot(BufferedImage bi, int x, int y){
		for(int i=0; i<motSel.length; i++){
			for(int j=0; j<motSel[0].length; j++){
				if(motSel[i][j] != bi.getRGB(i+x, j+y)){
					return false;
				}
			}
		}
		return true;
	}
	
	public Point getLocation(BufferedImage bi, int[][] sel){
		for(int i=0; i<bi.getWidth(); i++){
			for(int j=0; j<bi.getHeight(); j++){
				if(isMatch(bi, sel, i, j)){
					return new Point(i, j);
				}
			}
		}
		return new Point(0, 0);
	}
	
	public boolean isMatch(BufferedImage bi, int[][] sel, int x, int y){
		for(int i=0; i<sel.length; i++){
			for(int j=0; j<sel[0].length; j++){
				if(sel[i][j] != bi.getRGB(i+x, j+y)){
					return false;
				}
			}
		}
		return true;
	}
	
	public int[][] getSel(String fileName){
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		int[][] sel = new int[img.getWidth()][img.getHeight()];
		for(int i=0; i<img.getWidth(); i++){
			for(int j=0; j<img.getHeight(); j++){
				sel[i][j] = img.getRGB(i, j);
			}
		}
		return sel;
	}
	
	public int[][] getSel2(BufferedImage img){
		motSel = new int[img.getWidth()][img.getHeight()];
		for(int i=0; i<img.getWidth(); i++){
			for(int j=0; j<img.getHeight(); j++){
				motSel[i][j] = img.getRGB(i, j);
			}
		}
		return motSel;
	}
	
	public void tickAt(Point p, Point p2){
		robocop.mouseMove(p.x, p.y);
		robocop.delay(20);
		robocop.mousePress(InputEvent.BUTTON1_MASK);
		robocop.mouseRelease(InputEvent.BUTTON1_MASK);
		robocop.mouseMove(p2.x, p2.y);
	}
	
	public void mouseClick(){
		robocop.mousePress(InputEvent.BUTTON1_MASK);
		robocop.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	public void typeTextAt(Point p1, Point p2, String s){
		//Moves to p1, types s and then moves to point p2
		robocop.mouseMove(p1.x, p1.y);
		mouseClick();
		try {
			Thread.sleep(50L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		typeText(s);
		robocop.mouseMove(p2.x, p2.y);
	}
	
	public void typeText(String s){
		char c = ' ';
		for(int i=0; i<s.length(); i++){
			c = s.charAt(i);
			typeCharacter(c);
		}
	}
	
	public void checkMot(BufferedImage bi){
		//Check tick box
		Point p = getLocation(bi, motSel);
		if(p.x != 0){
			System.out.println("MOT Match at ("+p.x+", "+p.y+")");
			Point p2 = MouseInfo.getPointerInfo().getLocation();
			tickAt(new Point(p.x+49, p.y+6), p2);
		}else{
			System.out.println("No Match On MOT");
		}
	}
	
	public void checkReason(BufferedImage bi){
		//Check reason box
		Point p = getLocation(bi, reasonSel);
		if(p.x != 0){
			System.out.println("Reason Match at ("+p.x+", "+p.y+")");
			Point p2 = MouseInfo.getPointerInfo().getLocation();
			typeTextAt(new Point(p.x+81, p.y+3), p2, REASON_TEXT);
		}else{
			System.out.println("No Match on Reason");
		}
	}
	
	public void checkPurpose(BufferedImage bi){
		//Check purpose selection box
		Point p = getLocation(bi, purposeSel);
		if(p.x != 0){
			System.out.println("Purpose Match at ("+p.x+", "+p.y+")");
			Point p2 = MouseInfo.getPointerInfo().getLocation();
			robocop.mouseMove(p.x+135, p.y+1);
			mouseClick();
			try {
				Thread.sleep(250L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			BufferedImage bi2 = getImage();
			if(PURPOSE_OPTION==1){
				if(bi2.getRGB(p.x+98, p.y+11) == -1){
					robocop.mouseMove(p.x+131, p.y+74);
				}else{
					robocop.mouseMove(p.x+131, p.y-50);
				}
			}else{
				if(bi2.getRGB(p.x+98, p.y+11) == -1){
					robocop.mouseMove(p.x+131, p.y+93);
				}else{
					robocop.mouseMove(p.x+131, p.y-33);
				}
			}
			mouseClick();
			robocop.mouseMove(p2.x, p2.y);
			//mouseClick();
		}else{
			System.out.println("No Match on Purpose");
		}
	}
	
	public void typeCharacter(char letter){
		try
        {
            boolean upperCase = Character.isUpperCase(letter);
            String variableName = "VK_" + Character.toUpperCase(letter);

            Class<KeyEvent> clazz = KeyEvent.class;
            Field field = clazz.getField( variableName );
            int keyCode = field.getInt(null);

            robocop.delay(100);

            if (upperCase) robocop.keyPress( KeyEvent.VK_SHIFT );

            robocop.keyPress( keyCode );
            robocop.keyRelease( keyCode );

            if (upperCase) robocop.keyRelease( KeyEvent.VK_SHIFT );
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
	}
 
}
