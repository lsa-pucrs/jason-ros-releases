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

import java.util.Arrays;

import geometry_msgs.Point;
import cartago.*;

import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

import sensor_msgs.LaserScan;

/**
 * ROS artifact that handles /base_scan topic. Based on CArtAgO artifact
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-06-07
 */
public class ArtBaseScan extends JaCaRosArtifact {
	
	private Subscriber<sensor_msgs.LaserScan> subscriberScan;
	private float[] currentScan;
    private static String rosNodeName = "ArtBaseScan";
    private String propertyName = "base_scan";
    private String topicName = "/base_scan";
    private String topicType = sensor_msgs.LaserScan._TYPE;
	
	//private int laserScanSize = 641;
    private int laserScanSize = 1081;
	private float[] ranges_scan = new float[laserScanSize];;
    ReadCmd cmd;

    /**
     * Creates ROS node
     */
	public ArtBaseScan(){
		super(rosNodeName);
		System.out.println("ArtBaseScan >> construtor()");	
	}
	
//	float[] ranges = m_scan.getRanges();
//	ranges_scan = m_scan.getRanges();
//	ranges_scan[0] = checkNan(ranges[0]);
//	ranges_scan[159] = checkNan(ranges[159]);
//	ranges_scan[319] = checkNan(ranges[319]);
//	ranges_scan[479] = checkNan(ranges[479]);
//	ranges_scan[639] = checkNan(ranges[639]);
	
//	ranges_scan[0] = ranges[0];
//	ranges_scan[159] = ranges[159];
//	ranges_scan[319] = ranges[319];
//	ranges_scan[479] = ranges[479];
//	ranges_scan[639] = ranges[639];
	
//	a = -21; b = -22; c = -23;
//	defineObsProperty(type, a, b, c);
//	defineObsProperty(type, ranges_scan[0], ranges_scan[159], ranges_scan[319], ranges_scan[479], ranges_scan[639]);
	
    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName){
    	logger1.info("ArtBaseScan >> init(agentName)");
        logger1.info("ArtBaseScan >> init(agentName): agent name=" + agentName);
        logger1.info("ArtBaseScan >> init(agentName): Defining artifact property " + propertyName);
        
        defineObsProperty(propertyName, 50000f, 50000f, 50000f, 50000f, 50000f);
        
        if (agentName != null) {
            // Update topic name with agent name
            topicName = "/" + agentName + topicName;
        }

        cmd = new ReadCmd();
        
        subscriberScan = (Subscriber<LaserScan>) createSubscriber(topicName, topicType);
        subscriberScan.addMessageListener(new MessageListener <sensor_msgs.LaserScan> () {
        	@Override
            public void onNewMessage(LaserScan message) {
                //logger1.info("I heard from ArtBaseScan: " + message);
        		
        		float[] localBaseScan = message.getRanges();
        		//logger1.info("ArtBaseScan >> init(agentName): RANGE SIZE " + localBaseScan.length);
        		
        		if (currentScan == null)
        		{
        			currentScan = localBaseScan;
        		}
        		else if ( !Arrays.equals(currentScan, localBaseScan) )
        		{
            		// Do not send the message if the current value is the same of the previous one
        			currentScan = localBaseScan;
        			//logger1.info("variavel: " + m_odometry);
        			//logger1.info("X: " +m_odometry.getX());
        			//logger1.info("Y: " +m_odometry.getY());
        			//logger1.info("Z: " +m_odometry.getZ());
        			execInternalOp("receiving");
        		}
        		
        		
        		
                //currentScan = message.getRanges();
                //execInternalOp("receiving");
            }
        });

        logger1.info("ArtBaseScan >> init(agentName): end of method");
		}
	
    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtBaseScan >> init()");
        init(null);
        logger1.info("ArtBaseScan >> end of init()");
    }
    
    @INTERNAL_OPERATION
    void receiving() {
        //logger1.info("ArtBaseScan >> receiving()");
        await(cmd);
        signal(propertyName);
    }
    
    /**
     * The ReadCmd implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method.
     *
     * @author Rodrigo Wesz
     *
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

                ObsProperty prop = getObsProperty(propertyName);
                prop.updateValues(checkNan(currentScan[0]), checkNan(currentScan[(int) (laserScanSize*0.25)]), checkNan(currentScan[(int) (laserScanSize*0.5)]), checkNan(currentScan[(int) (laserScanSize*0.75)]), checkNan(currentScan[laserScanSize-1]));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
  
	/**
	 * Get current sensor values
	 */
	@OPERATION void getCurrentBaseScan(){
		logger1.info("ArtBaseScan >> getCurrentBaseScan()");
    	ObsProperty prop = getObsProperty(propertyName);
    	if(currentScan!= null)
    		prop.updateValues(checkNan(currentScan[0]), checkNan(currentScan[(int) (laserScanSize*0.25)]), checkNan(currentScan[(int) (laserScanSize*0.5)]), checkNan(currentScan[(int) (laserScanSize*0.75)]), checkNan(currentScan[laserScanSize-1]));
    	else
    		prop.updateValues(50000f, 50000f, 50000f, 50000f, 50000f);
    	//logger1.info("ArtBaseScan >> Value:" + prop);
        logger1.info("ArtBaseScan >> end of getCurrentBaseScan()");
        //0, 270, 514, 810, 1080
        //0, 159, 319, 479, 639
//		System.out.println("ArtBaseScan >> getCurrentBaseScan()");
//		ObsProperty prop = getObsProperty(propertyName);
//		prop.updateValues(prop.getValues());		
		signal("tick_scan");
		}
	}
