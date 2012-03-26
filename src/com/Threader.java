package com;

import java.awt.image.BufferedImage;

public class Threader implements Runnable{
	
	Main parent;
	
	public Threader(Main parent){
		this.parent = parent;
	}

	@Override
	public void run() {
		try{
			while(true){
				System.out.println("Iteration");

				//Get our screen-shot
				BufferedImage bi = parent.getImage();
				
				parent.checkMot(bi);
				
				parent.checkReason(bi);
				
				parent.checkPurpose(bi);
				
				System.out.println("Done");
			}
		}catch(Exception e){
			System.out.println("Error: ");
			e.printStackTrace();
		}
	}
	
}
