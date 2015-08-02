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

import tf2_msgs.TFMessage;

//import perception_msgs.perception;
//import jason.architecture.*;

/**
 * ROS artifact that handles /cmd_vel_mux and /odom topic.
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-30
 */
public class ArtCmdVelMuxAndOdometry extends JaCaRosArtifact {
	
	/* cmd_vel properties */
	private Publisher<geometry_msgs.Twist> publisherCmdVelMux;
	private geometry_msgs.Twist twist = null; //revisit (check if we need to set this to null. Isn't this already null?)
	private static String rosNodeName = "ArtCmdVelMuxAndOdometry";
	private String propertyNameCmdVelMux = "cmd_vel_mux";
	private String topicNameCmdVelMux = "/cmd_vel_mux/input/navi";	
	private String topicTypeCmdVelMux = geometry_msgs.Twist._TYPE;
	private float speed = 0.2f;
	//private double distance;
	private double stepCounter = 0;
	private boolean abort = false;
	
	/* odom properties */
    Subscriber <nav_msgs.Odometry> subscriberOdometry;
    Vector3 currentOdometry;
    //private static String rosNodeName = "ArtCmdVelAndOdometry";
    private String propertyNameOdom = "odom_cmd_vel_mux";
    private String topicNameOdom = "/odom";
    private String topicTypeOdom = nav_msgs.Odometry._TYPE;

    /**
     * Creates ROS node
     */
    public ArtCmdVelMuxAndOdometry() {
        super(rosNodeName);
        logger1.info("ArtCmdVelAndOdometry >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtCmdVelAndOdometry >> init()");
        //logger1.info("ArtCmdVelAndOdometry >> init(): agent name=" + agentName);

        if (agentName != null) {
            // Update topic name with agent name
            topicNameOdom = "/" + agentName + topicNameOdom;
            topicNameCmdVelMux = "/" + agentName + topicNameCmdVelMux;
            propertyNameOdom = agentName + "_" + propertyNameOdom;
        }
        
        /* cmd_vel communication */
        logger1.info("ArtCmdVelAndOdometry >> init(): Defining artifact property " + propertyNameCmdVelMux);
        publisherCmdVelMux = (Publisher<Twist>) createPublisher(topicNameCmdVelMux, topicTypeCmdVelMux);
        sleep(2000);

        /* odom communication */
        logger1.info("ArtCmdVelAndOdometry >> init(): Defining artifact property " + propertyNameOdom);
        defineObsProperty(propertyNameOdom, 0,0,0);
        
        
//        Subscriber<TFMessage> subscriberOdometry2 = (Subscriber <tf2_msgs.TFMessage>) createSubscriber("/tf", tf2_msgs.TFMessage._TYPE);
//        subscriberOdometry2.addMessageListener(new MessageListener<TFMessage>() {
//        	@Override
//        	public void onNewMessage(TFMessage message){
//        		List<TransformStamped> localTransf = message.getTransforms();
//        	}
//		});        
        
        subscriberOdometry = (Subscriber <nav_msgs.Odometry>) createSubscriber(topicNameOdom, topicTypeOdom);
        subscriberOdometry.addMessageListener(new MessageListener <nav_msgs.Odometry> () {
        	@Override
        	public void onNewMessage(nav_msgs.Odometry message) {
        		//logger1.info("I heard (from Artifact)" + message);
        		//Point localPose = message.getPose().getPose().getPosition();
        		Vector3 localPose = message.getTwist().getTwist().getLinear();
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
		twist = publisherCmdVelMux.newMessage();
		twist.getLinear().setX(this.speed /*- 0.8f*/); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		this.abort = false; //initialize abort flag
		double initialPose;
		if (currentOdometry!=null)
			initialPose = currentOdometry.getX();
		else
		{			
			initialPose = 0;
			while(currentOdometry==null)
			{
				logger1.info("ArtCmdVelAndOdometry >>>>>>> ESPERANDO ODOMETRIA");
			}
		}
		
		logger1.info("ArtCmdVelAndOdometry >>>>>>> TOPICO:"+topicNameCmdVelMux);
		
		logger1.info("ArtCmdVelAndOdometry >> valor inicial distance =" + distance);
		logger1.info("ArtCmdVelAndOdometry >> valor inicial stepCounter =" + stepCounter);
		logger1.info("ArtCmdVelAndOdometry >> valor inicial() currentOdometry =" + currentOdometry.getX());

		//if (currentOdometry!=null)
		//	stepCounter = currentOdometry.getX(); //initialize stepcounter
		//else
		stepCounter = 0; //initialize stepcounter
		while (!this.abort && stepCounter <= distance)
		{
			logger1.info("ArtCmdVelAndOdometry >> stepCounter <= distance????????????????????");
			logger1.info("ArtCmdVelAndOdometry >> distance =" + distance);
			logger1.info("ArtCmdVelAndOdometry >> stepCounter =" + stepCounter);
			logger1.info("ArtCmdVelAndOdometry >> currentOdometry =" + currentOdometry.getX());
			//logger1.info("ArtCmdVelAndOdometry >> moveForward() abort value =" + this.abort);
			if (currentOdometry!=null)
				stepCounter = currentOdometry.getX() - initialPose;
			//logger1.info("ArtCmdVelAndOdometry >> moveForward() I moved" + stepCounter);
			sleepNoLog(10);
			publisherCmdVelMux.publish(twist);
			//getCurrentOdom(); this will keep agent informed about the odometry. Comment it to avoid data overhead into the agent
		}	

		
		if (this.abort)
		{
			logger1.info("ArtCmdVelAndOdometry >> ABORTED");
		}
	}
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotate(int degree){
		logger1.info("ArtCmdVelMux >> rotate() DEGREES");
		twist = publisherCmdVelMux.newMessage();
		float localSpeed = 0.5f; //this.speed;
		//localSpeed = localSpeed - 0.1f;
		//localSpeed = localSpeed + 0.38f;
		
		logger1.info("ArtCmdVelMux >> rotate() DEGREE=" + degree);
		if (degree < 0)
			{
			localSpeed = localSpeed * -1;
			degree = degree * -1;
			}
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(localSpeed);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] Twist: " + twist);
		logger1.info("[JaCaRosArtifact - ArtCmdVel] rotate() Current speed: " + localSpeed);
		
		for (short i=0; i<degree/6; i++)
			{ //magic number depends of robot calibration - 7 sem peso, 6.5 com peso
			publisherCmdVelMux.publish(twist);
			logger1.info(i);
			sleep(500);
			//sleep(1000);
			}
		sleep(1000);
		}
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotateD(int degree){
		logger1.info("ArtCmdVelMux >> rotate() DEGREES");
		twist = publisherCmdVelMux.newMessage();
		float localSpeed = 0.5f; //this.speed;
		//localSpeed = localSpeed - 0.1f;
		//localSpeed = localSpeed + 0.38f;
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(localSpeed);
		
		logger1.info("[JaCaRosArtifact - ArtCmdVel] rotate() Current speed: " + localSpeed);
		
		for (short i=0; i<degree; i++)
			{ //magic number depends of robot calibration - 
			publisherCmdVelMux.publish(twist);
			logger1.info(i);
			sleep(500);
			//sleep(1000);
			}
		sleep(1000);
		}
	
	@OPERATION void moveF(int linear_duration){
		logger1.info("ArtCmdVelMuxAndOdom >> moveF");		
		twist = publisherCmdVelMux.newMessage();
		
		twist.getLinear().setX(this.speed); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		//logger1.info("ArtCmdVelMux >> Current speed: " + this.speed);
		
		for (int a=0; a<=linear_duration/*25 or 50*/; a++){
			logger1.info("ArtCmdVelMux >> publishing...");
			publisherCmdVelMux.publish(twist);
			sleepNoLog(100);			
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
}