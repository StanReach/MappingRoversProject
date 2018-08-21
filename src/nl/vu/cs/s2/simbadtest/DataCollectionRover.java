// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package nl.vu.cs.s2.simbadtest;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import javafx.scene.Camera;
import simbad.sim.CameraSensor;
import simbad.sim.RobotFactory;

/************************************************************/
/**
 * 
 */
public class DataCollectionRover extends Rover {
	
	CameraSensor camera;
    BufferedImage cameraImage;
    
    BufferedImage[] imageCollection;
    int imageCounter = 0;
    
    controlCentre controlHub;
	
	public DataCollectionRover(Vector3d pos, String ID, controlCentre mainControlCentre) {
		super(pos,ID);
		
		 RobotFactory.addBumperBeltSensor(this, 12);
		 
		 camera = RobotFactory.addCameraSensor(this);
         // reserve space for image capture
         cameraImage = camera.createCompatibleImage();
         
         imageCollection = new BufferedImage[200];
         
         controlHub = mainControlCentre;
	}
	
	Random rand = new Random();
	
	public Path mainPath;
	/**
	 * 
	 */
	String currentMode = "Idle";

	/**
	 * 
	 */
	private int pointOnPath = 0;
	
	
	
	public void takePictures() {
		
		for (int i = 0; i < 8; i++) {
			
			imageCollection[imageCounter] = cameraImage;
			
			rotate(1);
			
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void goToLocation(Point3d location) {
		
		Point3d currentLocation = getCoordinates();
		
		double xCoordOffSet = (currentLocation.x - location.x);
		double yCoordOffSet = (currentLocation.z - location.z);
		
		
		if (Math.abs(xCoordOffSet) < 0.1) {
			
			xCoordOffSet = 0;
		}
		
		if (Math.abs(yCoordOffSet) < 0.1) {
			
			yCoordOffSet = 0;
		}
		
		if (xCoordOffSet < 0) {
			
			if (yCoordOffSet < 0) {
				
				while (orientation != 7) {
					
					rotate(1);
				}
			} else if (yCoordOffSet > 0) {
				
				while (orientation != 1) {
					
					rotate(1);
				}
			} else if (yCoordOffSet == 0) {
				
				while (orientation != 0) {
					
					rotate(1);
				}
			}
		} else if (xCoordOffSet > 0) {
			
			if (yCoordOffSet < 0) {
				
				while (orientation != 5) {
					
					rotate(1);
				}
			} else if (yCoordOffSet > 0) {
				
				while (orientation != 3) {
					
					rotate(1);
				}
			} else if (yCoordOffSet == 0) {
				
				while (orientation != 4) {
					
					rotate(1);
				}
			}
		} else if (xCoordOffSet == 0) {
			
			if (yCoordOffSet < 0) {
				
				while (orientation != 6) {
					
					rotate(1);
				}
			} else if (yCoordOffSet > 0) {
				
				while (orientation != 2) {
					
					rotate(1);
				}
			} else if (yCoordOffSet == 0) {
				
					
					System.out.println(mainPath.stepArray[pointOnPath].x + " " + mainPath.stepArray[pointOnPath].z);
					pointOnPath++;
					
					takePictures();
					
					if (mainPath.stepArray[pointOnPath] == null) {
		    			
		    			currentMode = "finished";
		    		}
				}
			}
		}

	/**
	 * 
	 */
	public void sendPicturesControlCentre() {
		for(int i = 0; i < imageCollection.length;i++) {
			controlHub.imageSet[i] = (imageCollection[i]);
		}
	}
	
    public void performBehavior() {
    	
    	// perform the following actions every 5 virtual seconds
    	if(this.getCounter() % 5 == 0) {
    		
    		if (currentMode.equals("GoToLocation")) {
    			
    			this.setTranslationalVelocity(0.05);
    			goToLocation(mainPath.stepArray[pointOnPath]);
    		} 
    		
    		if (currentMode.equals("finished")) {
    			
    			sendPicturesControlCentre();
    			
    			currentMode = "idle";
    		}
    		
    		if (currentMode.equals("Idle")) {
    			
    			this.setTranslationalVelocity(0);
    		}
    	}
    }
}

