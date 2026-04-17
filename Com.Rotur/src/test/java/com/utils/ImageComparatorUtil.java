package com.utils;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageComparatorUtil {
	
	public static double compare(String img1, String img2, String diffPath) {
		
		try {
			BufferedImage i1 = ImageIO.read(new File(img1));
			BufferedImage i2 = ImageIO.read(new File(img2));
			int width = Math.min(i1.getWidth(), i2.getWidth());
			int height = Math.min(i1.getHeight(), i2.getHeight());
			
			BufferedImage diff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			int diffPixels = 0;
			for(int y =0; y<height; y++) {
				for(int x=0; x<width; x++) {
					//Masking
					if(y>700) continue;
					int p1 = i1.getRGB(x,y);
					int p2 = i2.getRGB(x, y);
					if(p1 != p2) {
						diff.setRGB(x, y, 0xFF0000);
						diffPixels ++;
					}else {
						diff.setRGB(x, y, p1);
					}
				}
			}
			
			ImageIO.write(diff, "png", new File(diffPath));
			return (diffPixels*100.0)/(width*height);
			
		}catch(Exception e) {
			e.printStackTrace();
			return 100;
		}
		
	}

}
