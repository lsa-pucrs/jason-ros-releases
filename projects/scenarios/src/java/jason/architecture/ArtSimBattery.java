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
public class ArtSimBattery extends Artifact {	
    private String propertyNameBattery = "battery";    
    private int currentBattery = 100; //initialize battery 100% charged
    
    private String propertyNameDiagnostics = "diagnostics";
    private String currentDiagnostics = "Full Battery"; //initialize status = full charged
    
    private ReadCmd cmd;
    private Timer timer;

    /**
     * Initialize artifact
     */
    void init() {
        //System.out.println("ArtSimBattery >> init()");                
        defineObsProperty(propertyNameBattery, currentBattery);
        signal(propertyNameBattery);
        
        defineObsProperty(propertyNameDiagnostics, currentDiagnostics);
        signal(propertyNameDiagnostics);
        
        cmd = new ReadCmd();
        
        timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentBattery = currentBattery - 2; //5
				System.out.println("ArtSimBattery >> Discharging. Current Level: " + currentBattery);
    			execInternalOp("receiving");				
				
				if (currentBattery <= 10){
					timer.stop();
				}
			}           
        });
        
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();
        
        //System.out.println("ArtSimBattery >> END OF init()");    
    }
    
    @INTERNAL_OPERATION
    void receiving() {
        await(cmd);        
    }

    /**
     * The ReadCmd implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method.     *
     * @author Rodrigo Wesz
     */
    class ReadCmd implements IBlockingCmd {

        /**
         * Constructor
         */
        public ReadCmd() {}

        /**
         * The command code
         */
        public void exec() {
            try {            	
            	isDiagnosticUpdateNeeded();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void isDiagnosticUpdateNeeded() {
		String localDiagnostic = getObsProperty(propertyNameDiagnostics).stringValue();   
		//System.out.println("ArtSimBattery >> isDiagnosticUpdateNeeded() | Before =" +localDiagnostic);
		
		switch (currentBattery) {
		case 100:
			currentDiagnostics = "Full Battery";
			break;
		case 50:
			currentDiagnostics = "Medium Battery";
			break;
		case 20:
			currentDiagnostics = "Low Battery";
			break;
//      default:
//        	currentDiagnostics = "Error";
//         	break;
			}
		                
		ObsProperty prop = getObsProperty(propertyNameBattery);
		prop.updateValues(currentBattery);  
		
		if (!localDiagnostic.equals(currentDiagnostics)){
			ObsProperty propDiag = getObsProperty(propertyNameDiagnostics);
		    propDiag.updateValues(currentDiagnostics);
		    //signal(propertyNameDiagnostics);
			System.out.println("ArtSimBattery >> "+ localDiagnostic +" eh diferente de "+currentDiagnostics);
		}
	}
    
    /**
     * Get current battery value
     */
    @OPERATION void getCurrentBattery() {
    	System.out.println("ArtSimBattery >> getCurrentBattery()");
    	ObsProperty prop = getObsProperty(propertyNameBattery);    	
    	prop.updateValues(currentBattery);    	
    	System.out.println("ArtSimBattery >> getCurrentBattery() | Value:" + prop);
        signal(propertyNameBattery);        
    }
    
    /**
     * Get current Diagnostic value
     */
    @OPERATION void getCurrentDiagnostic(int battery) {
    	System.out.println("ArtSimBattery >> getCurrentDiagnostic()");    	
    	ObsProperty prop = getObsProperty(propertyNameDiagnostics);    	
    	prop.updateValues(currentDiagnostics);    	
    	System.out.println("ArtSimBattery >> getCurrentDiagnostic() | Value:" + prop);
        signal(propertyNameDiagnostics);        
    }
    
	
	@OPERATION void operationRecharge() throws InterruptedException{
		System.out.println("ArtSimBattery >> operationRecharge()");
        
		while (currentBattery < 100)
		{
			currentBattery = currentBattery + 10;
			ObsProperty prop = getObsProperty(propertyNameBattery);   
			System.out.println("Recharging");
			prop.updateValues(currentBattery);
			signal(propertyNameBattery);
			Thread.sleep(500);
			isDiagnosticUpdateNeeded();
		}
		timer.start();
	}
	
	@OPERATION void operationligar(){
		timer.start();
	}
}