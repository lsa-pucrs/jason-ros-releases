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

//import org.ros.master.client.TopicType;
import org.ros.node.topic.Publisher;
import cartago.*;

/**
 * ROS artifact that handles <code>cmd_vel_mux topic</code>. Based on CArtAgO artifact
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-05-21
 */
public class ArtCmdVelMux extends JaCaRosArtifact {
	
	private float speed;	
	private Publisher<geometry_msgs.Twist> publisherCmdVel;
	private geometry_msgs.Twist twist = null;
	private static String rosNodeName = "ArtCmdVelMux";
	private String propertyName = "cmd_vel_mux";
	private String topicName = "/cmd_vel_mux/input/navi";	
	private String topicType = geometry_msgs.Twist._TYPE;
	
	/**
	 * Creates ROS node
	 */
	public ArtCmdVelMux() {
		super(rosNodeName);
		logger1.info("ArtCmdVelMux >> construtor()");	
	}	
	
	/**
	 * Initialize communication on ROS topic
	 */
	void init(String agentName, float value){
		logger1.info("ArtCmdVelMux >> init() two parameters");
		logger1.info("ArtCmdVelMux >> init() agent name: "+ agentName);
		
		// Save value from parameter (artifact initialization @ agente code)
		this.speed = value;
		
		// Update topic name with agent name
		topicName = "/"+agentName+topicName;
		
		logger1.info("ArtCmdVelMux >>  init() before connect the publusher");
		publisherCmdVel = (Publisher<Twist>) createPublisher(topicName, topicType);
		logger1.info("ArtCmdVelMux >> init() after connect the publusher");
		this.twist = publisherCmdVel.newMessage();
		defineObsProperty(propertyName, this.twist);
		
		logger1.info("ArtCmdVelMux >> Initial speed: " + value);
		}

	/**
	 * Initialize communication on ROS topic
	 */
	void init(float value){
		logger1.info("ArtCmdVelMux >> init() one parameter");
		logger1.info("ArtCmdVelMux >> init() speed value: " + value);
		
		// Save value from parameter (artifact initialization @ agente code)
		this.speed = value;		
		
		logger1.info("ArtCmdVelMux >> init() before connect the publusher");
		publisherCmdVel = (Publisher<Twist>) createPublisher(topicName, topicType);
		
		logger1.info("ArtCmdVelMux >> init() antes oi");
		this.twist = publisherCmdVel.newMessage();		
		logger1.info("ArtCmdVelMux >> init() depois oi");
		
		defineObsProperty(propertyName, this.twist);
		logger1.info("ArtCmdVelMux >> init() depois");
		logger1.info("ArtCmdVelMux >> Initial speed: " + value);
		}
	
	/**
	 * Initialize communication on ROS topic
	 */
	void init(){
		init(0.7f);	
	}
	
	/**
	 * 
	 * @param linear_duration
	 */
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
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Linear X
	 */		
	@OPERATION void moveForward(){
		//sleep(1500);
		logger1.info("ArtCmdVelMux >> moveForward()");		
		twist = publisherCmdVel.newMessage();
		
		twist.getLinear().setX(this.speed - 0.8f); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] Twist: " + twist);
		logger1.info("ArtCmdVelMux >> Current speed: " + this.speed);
		
		publisherCmdVel.publish(twist);
		sleep(200);
		publisherCmdVel.publish(twist);
		sleep(200);
		publisherCmdVel.publish(twist);
		sleep(200);
		publisherCmdVel.publish(twist);
		sleep(200);		
		publisherCmdVel.publish(twist);
		sleep(1500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel_mux Linear -X
	 */
	@OPERATION void moveReverse(){
		sleep(1500);
		logger1.info("ArtCmdVelMux >> moveReverse()");
		twist = publisherCmdVel.newMessage();
		
		twist.getLinear().setX(this.speed * -1); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(0);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] Twist: " + twist);
		logger1.info("[JaCaRosArtifact - ArtCmdVel] Current speed: " + (this.speed * -1));
		publisherCmdVel.publish(twist);
		sleep(1500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotateLeft(){
		logger1.info("ArtCmdVelMux >> rotate() Left");
		twist = publisherCmdVel.newMessage();
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(this.speed);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] Twist: " + twist);
		logger1.info("[JaCaRosArtifact - ArtCmdVel] Current speed: " + this.speed);
		publisherCmdVel.publish(twist);
		sleep(500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel Angular -Z
	 */
	@OPERATION void rotateRight(){
		logger1.info("ArtCmdVelMux >> rotate() Right");
		twist = publisherCmdVel.newMessage();
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(this.speed * -1);
		
		//logger1.info("[JaCaRosArtifact - ArtCmdVel] Twist: " + twist);
		logger1.info("ArtCmdVelMux >> Current speed: " + (this.speed * -1));
		publisherCmdVel.publish(twist);
		sleep(500);
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
		
		logger1.info("[JaCaRosArtifact - ArtCmdVel] rotate() Current speed: " + localSpeed);
		
		for (short i=0; i<degree/15; i++)
			{
			publisherCmdVel.publish(twist);
			sleepNoLog(500);
			}
		sleep(1000);
		}
		
	/**
	 * Get current sensor values
	 */
	@OPERATION void getCurrent3(){
		logger1.info("ArtCmdVelMux >> getCurrent()");
		ObsProperty prop = getObsProperty(propertyName);
		prop.updateValues(prop.getValues());
		signal("tick_cmd_vel_mux");
		}
		
	}
