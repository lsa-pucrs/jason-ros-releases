/**
 * ROS artifact code for project jacaros
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package jason.architecture;


import cartago.*;
import jason.architecture.ArtOdometry.ReadCmd;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * ROS artifact that handles /odom topic.
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-21
 */
public class ArtCleaning extends Artifact {	
    private String propertyNameAtRoom = "atRoom";
    
    private String propertyNameVacuum = "vacuum";
    private String propertyNameMop = "mop";
    private String propertyNameScrub = "scrub";
    
    private final String HALLWAY ="hallway";
    private final String LIVING ="living Room";
    private final String KITCHEN ="kitchen";
    private final String BATHROOM ="bathroom";    
    private int atRoom = 0;
    private String room = HALLWAY;
    
    /**
     * Initialize artifact
     */
    void init() {
        //System.out.println("ArtCleaning >> init()");                
        defineObsProperty(propertyNameAtRoom, atRoom); //initialize at hallway
        //signal(propertyNameAtRoom);
        defineObsProperty(propertyNameVacuum, "", ""); 
        defineObsProperty(propertyNameMop, "", ""); 
        defineObsProperty(propertyNameScrub, "", ""); 
        //signal(propertyNameCleaning);
        //System.out.println("ArtCleaning >> END OF init()");    
    }    
    
    /**
     * Get current room
     */
    @OPERATION void whereAmI() {
    	System.out.print("ArtCleaning >> whereAmI()");
    	ObsProperty prop = getObsProperty(propertyNameAtRoom);    	
    	signal(propertyNameAtRoom);
        System.out.print("ArtCleaning >>  FIM DO whereAmI()");
    }
	
    /**
     * clean some place
     */
	@OPERATION void clean(int localRoom){
		//System.out.println("ArtCleaning >> clean =============== !!! ========================");
		
    	if(localRoom == 0){
    		//System.out.println("HALLWAY");
    		room = HALLWAY;
    	}else if (localRoom == 1){
    		//System.out.println("LIVING");
    		room = LIVING;
    		ObsProperty prop = getObsProperty(propertyNameVacuum);
    		prop.updateValues("Carpet", room);
    		signal(propertyNameVacuum);
    	}else if (localRoom == 2){
    		//System.out.println("KITCHEN");
    		room = KITCHEN;
    		ObsProperty prop = getObsProperty(propertyNameMop);
    		prop.updateValues("Flor", room);
    		signal(propertyNameMop);
    	}else if (localRoom == 3){
    		//System.out.println("BATHROOM");
    		room = BATHROOM;
    		ObsProperty prop = getObsProperty(propertyNameScrub);
    		prop.updateValues("Tub", room);
    		signal(propertyNameScrub);
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		prop = getObsProperty(propertyNameMop);
    		prop.updateValues("Flor", room);
    		signal(propertyNameMop);
    	}else {
    		room = "error";
    	}
    	System.out.println("LIMPANDO");
    	try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * update room
	 */
	@OPERATION void updateWhereAmI(){
		//System.out.print("ArtCleaning >> updateWhereAmI");
		atRoom++;
		if (atRoom > 3)
			atRoom = 0;
		
		ObsProperty prop = getObsProperty(propertyNameAtRoom);
		prop.updateValues(atRoom);
    	signal(propertyNameAtRoom);
	}
}