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

import geometry_msgs.*;
//import geometry_msgs.Pose;
//import geometry_msgs.PoseWithCovariance;

import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

/**
 * ROS artifact that handles /odom topic.
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-21
 */
public class ArtOdometry extends JaCaRosArtifact {
	private Subscriber <nav_msgs.Odometry> subscriberOdometry;
    private Point currentOdometry;
    private static String rosNodeName = "ArtOdometry";
    private String propertyName = "odom";
    private String topicName = "/odom";
    private String topicType = nav_msgs.Odometry._TYPE;
    private ReadCmd cmd;
    private final byte refreshRateValue = 1; //must be > 0
    private byte refreshRateAux = refreshRateValue;

    /**
     * Creates ROS node
     */
    public ArtOdometry() {
        super(rosNodeName);
        logger1.info("ArtOdometry >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtOdometry >> init(String agentName)");
        logger1.info("ArtOdometry >> init(String agentName): agent name=" + agentName);
        logger1.info("ArtOdometry >> init(String agentName): Defining artifact property " + propertyName);
        //defineObsProperty(propertyName,currentPerception);        
        defineObsProperty(propertyName, 0,0,0);

        if (agentName != null) {
            // Update topic name with agent name
            topicName = "/" + agentName + topicName;
        }

        cmd = new ReadCmd();

        subscriberOdometry = (Subscriber <nav_msgs.Odometry>) createSubscriber(topicName, topicType);
        subscriberOdometry.addMessageListener(new MessageListener <nav_msgs.Odometry> () {
        	@Override
        	public void onNewMessage(nav_msgs.Odometry message) {
        		//logger1.info("I heard (from Artifact)" + message);                
        		Point localPose = message.getPose().getPose().getPosition();
        		
        		if (currentOdometry == null)
        		{
        			currentOdometry = localPose;
        		}
        		else if (!currentOdometry.equals(localPose) && refreshRate())
        		{
            		// Do not send the message if the current value is the same of the previous one
        			currentOdometry = localPose;
        			//logger1.info("variavel: " + m_odometry);
        			//logger1.info("X: " +m_odometry.getX());
        			//logger1.info("Y: " +m_odometry.getY());
        			//logger1.info("Z: " +m_odometry.getZ());
        			execInternalOp("receiving");
        		}
            }

			private boolean refreshRate() {
				boolean result = false;
				if (refreshRateAux % refreshRateValue == 0)
				{
					//logger1.info("----------- Artifact should UPDATE current value. [" + refreshRateAux + "/" + refreshRateValue + "]");
					refreshRateAux = refreshRateValue;
					result = true;
				}
				//else
				//{
				//	logger1.info("----------- Artifact should NOT update current value. [" + refreshRateAux + "/" + refreshRateValue + "]");
				//}
				
				refreshRateAux++;
				//return true;
				return result;
			}
        });

        logger1.info("ArtOdometry >> init(String agentName): end of init(String agentName)");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtOdometry >> init()");
        init(null);
        logger1.info("ArtOdometry >> end of init()");
    }

    @INTERNAL_OPERATION
    void receiving() {
        //logger1.info("ArtOdometry >> receiving()");
        await(cmd);
        signal(propertyName); //comment this to use 'on demand' mode like
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
                prop.updateValues(currentOdometry.getX(),currentOdometry.getY(),currentOdometry.getZ());  //comment this to use 'on demand' mode like

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Get current sensor values
     */
    @
    OPERATION void getCurrentOdom() {
    	logger1.info("ArtOdometry >> getCurrentOdom()");
    	ObsProperty prop = getObsProperty(propertyName);
    	if(currentOdometry!= null)
    		prop.updateValues(currentOdometry.getX(),currentOdometry.getY(),currentOdometry.getZ());
    	else
    		prop.updateValues(0,0,0);
    	logger1.info("ArtOdometry >>  Value:" + prop);
        signal(propertyName);
        logger1.info("ArtOdometry >>  FIM DO getCurrentOdom()");
    }
}