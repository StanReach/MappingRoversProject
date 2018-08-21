package nl.vu.cs.s2.simbadtest;


import simbad.gui.*;
import simbad.sim.*;
import javax.vecmath.Vector3d;

/**
  Derivate your own code from this example.
 */


public class Main {

    public static void main(String[] args) {
        // request antialising so that diagonal lines are not "stairy"
        System.setProperty("j3d.implicitAntialiasing", "true");
        
        // creation of the environment containing all obstacles and robots
        EnvironmentDescription environment = new Environment();
        
        controlCentre controlCentre = new controlCentre(10, environment);
        
        /*MappingRover kj = new MappingRover(new Vector3d(0,0,0),"1", controlCentre);
        
        environment.add(kj );*/
        
        // here we create an instance of the whole Simbad simulator and we assign the newly created environment 
        Simbad frame = new Simbad(environment, false);
        frame.update(frame.getGraphics());
    }

} 