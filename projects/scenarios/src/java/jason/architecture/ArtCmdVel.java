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

import geometry_msgs.Twist;

import org.ros.node.topic.Publisher;
import cartago.*;
//import jason.architecture.*;

/**
 * ROS artifact that handles cmd_vel topic. Based on CArtAgO artifact
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-21
 */
public class ArtCmdVel extends JaCaRosArtifact {
	
	private float speed;
	private Publisher<geometry_msgs.Twist> publisherCmdVel;
	private geometry_msgs.Twist twist = null;	
	private static String rosNodeName = "ArtCmdVel";
	private String propertyName = "cmd_vel";
	private String topicName = "/cmd_vel";	
	private String topicType = geometry_msgs.Twist._TYPE;
	
	/**
	 * Creates ROS node
	 */
	public ArtCmdVel(){
		super(rosNodeName);
		logger1.info("ArtCmdVel >> construtor()");	
	}
	
	/**
	 * Initialize communication on ROS topic
	 */
	void init(){
		logger1.info("ArtCmdVel >> init()");
        init(null);
        logger1.info("ArtCmdVel >> end of init()");
        
		//logger1.info("ArtCmdVel >> init()");
		//OdomValues x = new OdomValues();
		//defineObsRosProperty("robot_1","cmd_vel",x.name,x.linearX);
		//publisherCmdVel = (Publisher<Twist>) connectToPublisher("/robot_1/cmd_vel", topicType);
		}
	
	/**
	 * Initialize communication on ROS topic
	 */
	void init(String agentName){
		logger1.info("ArtCmdVel >> init(String agentName)");
		
		if (agentName != null) {
			// Update topic name with agent name
			topicName = "/"+agentName+topicName;
		}

		//defineObsRosProperty("robot_1","cmd_vel",x.name,x.linearX);
		publisherCmdVel = (Publisher<Twist>) createPublisher(topicName, topicType);
		}
	
	/**
	 * Publish into ROS topics: cmd_vel Linear X
	 */
	@OPERATION void moveForward(){
		System.out.println("[JaCaRosArtifact - ArtCmdVel] moveForward()");
		//Structure linear  = (Structure)(act.getActionTerm().getTerm(0));
		//Structure angular = (Structure)(act.getActionTerm().getTerm(1));
		
		geometry_msgs.Twist twist = publisherCmdVel.newMessage();
		twist.getLinear().setX( 0.5f );
		twist.getLinear().setY( 0 );
		twist.getLinear().setZ( 0 );
		twist.getAngular().setX( 0 );
		twist.getAngular().setY( 0 );
		twist.getAngular().setZ(  0 );
		
		/*twist.getLinear().setX( ((NumberTerm)linear.getTerm(0)).solve() );
		 twist.getLinear().setY( ((NumberTerm)linear.getTerm(1)).solve() );
		 twist.getLinear().setZ( ((NumberTerm)linear.getTerm(2)).solve() );
		 twist.getAngular().setX( ((NumberTerm)angular.getTerm(0)).solve() );
		 twist.getAngular().setY( ((NumberTerm)angular.getTerm(1)).solve() );
		 twist.getAngular().setZ( ((NumberTerm)angular.getTerm(2)).solve() );*/
		
		for (int i=0; i<10/*0/*1000*/; i++)
			{
			//System.out.println("[JaCaRosArtifact - ArtCmdVel] I= " + i);
			//System.out.println(twist);
			sleepNoLog(10);
			publisherCmdVel.publish(twist);
			}
		}
	
	/**
	 * Publish into ROS topics: cmd_vel Linear -X
	 */
	@OPERATION void moveReverse(){
		System.out.println("[JaCaRosArtifact - ArtCmdVel] moveReverse()");
		
		geometry_msgs.Twist twist = publisherCmdVel.newMessage();
		twist.getLinear().setX( -3 ); twist.getLinear().setY( 0 ); twist.getLinear().setZ( 0 );
		twist.getAngular().setX( 0 ); twist.getAngular().setY( 0 ); twist.getAngular().setZ( 0 );
		
		for (int i=0; i<1000; i++)
			{
			System.out.println("[JaCaRosArtifact - ArtCmdVel] I= " + i);
			System.out.println(twist);
			publisherCmdVel.publish(twist);
			}
		}
	
	/**
	 * Publish into ROS topics: cmd_vel Angular Z
	 */
	@OPERATION void rotateLeft(){
		System.out.println("[JaCaRosArtifact - ArtCmdVel] rotate() Left");
		
		geometry_msgs.Twist twist = publisherCmdVel.newMessage();
		twist.getLinear().setX( 0 );
		twist.getLinear().setY( 0 );
		twist.getLinear().setZ( 0 );
		twist.getAngular().setX( 0 );
		twist.getAngular().setY( 0 );
		twist.getAngular().setZ( 3 );
		
		for (int i=0; i<1000; i++)
			{
			System.out.println("[JaCaRosArtifact - ArtCmdVel] I= " + i);
			//System.out.println(twist);
			publisherCmdVel.publish(twist);
			}
		}
	
	/**
	 * Publish into ROS topics: cmd_vel Angular -Z
	 */
	@OPERATION void rotateRight(){
		System.out.println("[JaCaRosArtifact - ArtCmdVel] rotate() Right");
		
		geometry_msgs.Twist twist = publisherCmdVel.newMessage();
		twist.getLinear().setX( 0 );
		twist.getLinear().setY( 0 );
		twist.getLinear().setZ( 0 );
		twist.getAngular().setX( 0 );
		twist.getAngular().setY( 0 );
		twist.getAngular().setZ( -3 );
		
		for (int i=0; i<1000; i++)
			{
			System.out.println("[JaCaRosArtifact - ArtCmdVel] I= " + i);
			//System.out.println(twist);
			publisherCmdVel.publish(twist);
			}
		}
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotate(int degree){
		logger1.info("ArtCmdVelMux >> rotate() DEGREES");
		twist = publisherCmdVel.newMessage();
		float localSpeed = this.speed;
		localSpeed = localSpeed - 0.1f;
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
		
		for (short i=0; i<degree/30; i++)
			{
			publisherCmdVel.publish(twist);
			//sleep(500);
			sleep(1000);
			}
		sleep(1000);
		}
	
	/**
	 * Get current sensor values
	 */
	@OPERATION void getCurrent3(){
		System.out.println("[JaCaRosArtifact - ArtCmdVel] getCurrent()");
		
		ObsProperty prop = getObsProperty("cmd_vel");
		prop.updateValues(prop.getValues());
		signal("tick_cmd_vel");
		}
	
	@OPERATION void move(int linear_duration){
		twist = publisherCmdVel.newMessage();
		twist.getLinear().setX(1);
		twist.getLinear().setY(0);
		twist.getLinear().setZ(0);
		twist.getAngular().setX(0);
		twist.getAngular().setY(0);
		twist.getAngular().setZ(0);

		for (int a=0; a<=linear_duration; a++){ //publish during a given time
			publisherCmdVel.publish(twist);
			sleepNoLog(100);
		}
	}
	}

