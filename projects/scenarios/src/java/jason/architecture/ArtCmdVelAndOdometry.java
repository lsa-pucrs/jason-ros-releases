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

import java.util.List;
import java.util.ArrayList;

import org.ros.message.MessageListener;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;

//import perception_msgs.perception;
//import jason.architecture.*;

/**
 * ROS artifact that handles /cmd_vel and /odom topic.
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-24
 */
public class ArtCmdVelAndOdometry extends JaCaRosArtifact {
	
	/* cmd_vel properties */
	private Publisher<geometry_msgs.Twist> publisherCmdVel;
	private geometry_msgs.Twist twist = null; //revisit (check if we need to set this to null. Isn't this already null?)
	private static String rosNodeName = "ArtCmdVelAndOdometry";
	private String propertyNameCmdVel = "cmd_vel";
	private String topicNameCmdVel = "/cmd_vel";	
	private String topicTypeCmdVel = geometry_msgs.Twist._TYPE;
	private float speed = 1;
	//private double distance;
	private double stepCounter = 0;
	private boolean abort = false;
	
	/* odom properties */
    Subscriber <nav_msgs.Odometry> subscriberOdometry;
    Point currentOdometry;
    //private static String rosNodeName = "ArtCmdVelAndOdometry";
    private String propertyNameOdom = "odom_cmd_vel";
    private String topicNameOdom = "/odom";
    private String topicTypeOdom = nav_msgs.Odometry._TYPE;

    /**
     * Creates ROS node
     */
    public ArtCmdVelAndOdometry() {
        super(rosNodeName);
        logger1.info("ArtCmdVelAndOdometry >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtCmdVelAndOdometry >> init()");
        logger1.info("ArtCmdVelAndOdometry >> init(): agent name=" + agentName);

        if (agentName != null) {
            // Update topic name with agent name
            topicNameOdom = "/" + agentName + topicNameOdom;
            topicNameCmdVel = "/" + agentName + topicNameCmdVel;
            propertyNameOdom = agentName + "_" + propertyNameOdom;
        }
        
        /* cmd_vel communication */
        logger1.info("ArtCmdVelAndOdometry >> init(): Defining artifact property " + propertyNameCmdVel);
        publisherCmdVel = (Publisher<Twist>) createPublisher(topicNameCmdVel, topicTypeCmdVel);
        sleep(2000);

        /* odom communication */
        logger1.info("ArtCmdVelAndOdometry >> init(): Defining artifact property " + propertyNameOdom);
        defineObsProperty(propertyNameOdom, 0,0,0);
        
        subscriberOdometry = (Subscriber <nav_msgs.Odometry>) createSubscriber(topicNameOdom, topicTypeOdom);
        subscriberOdometry.addMessageListener(new MessageListener <nav_msgs.Odometry> () {
        	@Override
        	public void onNewMessage(nav_msgs.Odometry message) {
        		//logger1.info("I heard (from Artifact)" + message);
        		Point localPose = message.getPose().getPose().getPosition();        		
        		if (currentOdometry == null || !currentOdometry.equals(message.getPose().getPose().getPosition()))        		
        			currentOdometry = localPose;
            }
        });

        logger1.info("ArtCmdVelAndOdometry >> init(): end of method");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtCmdVelAndOdometry >> init()");
        init(null);
        logger1.info("ArtCmdVelAndOdometry >> end of init()");
    }
        
    /* cmd_vel Operations */
    
    /**
	 * Publish into ROS topics: cmd_vel Linear X
	 */
	@OPERATION void moveForward(double distance){
		logger1.info("ArtCmdVelAndOdometry >> moveForward()");		
		twist = publisherCmdVel.newMessage();
		twist.getLinear().setX(this.speed /*- 0.8f*/); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		this.abort = false; //initialize abort flag
		double initialPose;
		if (currentOdometry!=null)
			initialPose = currentOdometry.getX();
		else
			initialPose = 0;
		
		logger1.info("ArtCmdVelAndOdometry >>>>>>> TOPICO:"+topicNameCmdVel);

		stepCounter = 0; //initialize stepcounter
		while (!this.abort && stepCounter <= distance)
		{
			//logger1.info("ArtCmdVelAndOdometry >> moveForward() abort value =" + this.abort);
			if (currentOdometry!=null)
				stepCounter = currentOdometry.getX() - initialPose;
			//logger1.info("ArtCmdVelAndOdometry >> moveForward() I moved" + stepCounter);
			sleepNoLog(10);
			publisherCmdVel.publish(twist);
			//getCurrentOdom(); this will keep agent informed about the odometry. Comment it to avoid data overhead into the agent
		}	

		
		if (this.abort)
		{
			logger1.info("ArtCmdVelAndOdometry >> ABORTED");
		}
	}
    
	/* odom Operations */
	
	/**
	 * Operation used to abort a movement
	 */
	@OPERATION void abortMoving() {
		logger1.info("ArtCmdVelAndOdometry >> abortMoving()");
		this.abort = true;
		logger1.info("ArtCmdVelAndOdometry >> abortMoving() Abort value: " + this.abort);
	}
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotate(int degree){
		//logger1.info("ArtCmdVelMux >> rotate() DEGREES");
		twist = publisherCmdVel.newMessage();
		float localSpeed = 1.3f; //this.speed; tava 0.5
		//localSpeed = localSpeed - 0.1f;
		//localSpeed = localSpeed + 0.38f;
		
		//logger1.info("ArtCmdVelMux >> rotate() DEGREE=" + degree);
		if (degree < 0)
			{
			localSpeed = localSpeed * -1;
			degree = degree * -1;
			}
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(localSpeed);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] rotate() Current speed: " + localSpeed);
		
		for (short i=0; i<degree/15; i++)
			{ //magic number depends of robot calibration
			publisherCmdVel.publish(twist);
			sleepNoLog(500);
			//sleep(1000);
			}
		sleepNoLog(1000);
		}

    /**
     * Get current sensor values
     */
    @OPERATION void getCurrentOdom() {
    	logger1.info("ArtOdometry >> getCurrentOdom()");
    	ObsProperty prop = getObsProperty(propertyNameOdom);
    	if(currentOdometry!= null)
    		prop.updateValues(currentOdometry.getX(),currentOdometry.getY(),currentOdometry.getZ());
    	else
    		prop.updateValues(0,0,0);
    	logger1.info("ArtOdometry >>  Value:" + prop);
        signal(propertyNameOdom);
        logger1.info("ArtOdometry >>  FIM DO getCurrentOdom()");
    }
    
    /**
	 * Publish into ROS topics: cmd_vel_mux Linear X
	 */		
	@OPERATION void moveF(){
		//logger1.info("ArtCmdVelMuxAndOdom >> moveF");		
		twist = publisherCmdVel.newMessage();
		
		twist.getLinear().setX(this.speed); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		//logger1.info("ArtCmdVelMux >> Current speed: " + this.speed);
		
		for (int a=0; a<=25/*50*/; a++){
			publisherCmdVel.publish(twist);
			sleepNoLog(100);			
		}
	}
}