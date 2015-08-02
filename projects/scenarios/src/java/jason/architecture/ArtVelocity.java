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
import geometry_msgs.Twist;
import org.ros.node.topic.Publisher;

/**
 * ROS artifact that handles <code>/mobile_base/commands/velocity topic</code>.
 * 
 * @author Talles De Souza Perozzo <talles.perozzo@acad.pucrs.br>
 * @version 1.0
 * @since 2014-12-8
 */
public class ArtVelocity extends JaCaRosArtifact {
	
	private float speed;	
	private Publisher<geometry_msgs.Twist> publisherVelocity;
	private geometry_msgs.Twist twist = null;
	private static String rosNodeName = "Velocity";
	private String propertyName = "Velocity";
	private String topicName = "/mobile_base/commands/velocity";	
	private String topicType = geometry_msgs.Twist._TYPE;
	
	/**
	 * Creates ROS node
	 */
	public ArtVelocity()
	{
		super(rosNodeName);
        logger1.info("ArtVelocity >> construtor()");
	}	
	
	/**
	 * Initialize communication on ROS topic
	 */
	void init(String agentName, float value){
		logger1.info("ArtVelocity >> init(agentName,value)");
		
		// Save value from parameter (artifact initialization @ agente code)
		this.speed = value;
		
		// Update topic name with agent name
		topicName = "/"+agentName+topicName;
		
		publisherVelocity = (Publisher<geometry_msgs.Twist>) createPublisher(topicName, topicType);
		this.twist = publisherVelocity.newMessage();
		defineObsProperty(propertyName, this.twist);
		}

	/**
	 * Initialize communication on ROS topic
	 */
	void init(float value){
		logger1.info("ArtVelocity >> init(value) ");
		
		// Save value from parameter (artifact initialization @ agente code)
		this.speed = value;		
		
		publisherVelocity = (Publisher<geometry_msgs.Twist>) createPublisher(topicName, topicType);
		
		this.twist = publisherVelocity.newMessage();		
		
		defineObsProperty(propertyName, this.twist);
		
		}
	
	/**
	 * Publish into ROS topics: cmd_vel_mux Linear X
	 */		
	@OPERATION void moveForward(){
		sleep(1500);
		logger1.info("ArtVelocity >> moveForward()");		
		
		twist = publisherVelocity.newMessage();
		
		twist.getLinear().setX(this.speed - 0.8f); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0);	twist.getAngular().setY(0);	twist.getAngular().setZ(0);
		
		//System.out.println("[JaCaRosArtifact - ArtVelocity] Twist: " + twist);
				
		publisherVelocity.publish(twist);
		sleep(200);
		publisherVelocity.publish(twist);
		sleep(200);
		publisherVelocity.publish(twist);
		sleep(200);
		publisherVelocity.publish(twist);
		sleep(200);
		publisherVelocity.publish(twist);
		sleep(1500);
		
		}
		
	/**
	 * Publish into ROS topics: cmd_vel_mux Linear -X
	 */
	@OPERATION void moveReverse(){
		sleep(1500);
		System.out.println("ArtVelocity >> moveReverse()");
		twist = publisherVelocity.newMessage();
		
		twist.getLinear().setX((this.speed - 0.8f)*-1); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(0);
		
		//System.out.println("[JaCaRosArtifact - ArtVelocity] Twist: " + twist);
		publisherVelocity.publish(twist);
		sleep(1500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotateLeft(){
		logger1.info("ArtVelocity >> rotate() Left");
		twist = publisherVelocity.newMessage();
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(this.speed);
		
		publisherVelocity.publish(twist);
		sleep(500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel Angular -Z
	 */
	@OPERATION void rotateRight(){
		logger1.info("ArtVelocity >> rotate() Right");
		twist = publisherVelocity.newMessage();
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(this.speed * -1);
		
		//System.out.println("[JaCaRosArtifact - ArtVelocity] Twist: " + twist);
		System.out.println("[JaCaRosArtifact - ArtVelocity] Current speed: " + (this.speed * -1));
		publisherVelocity.publish(twist);
		sleep(500);
		}
		
	/**
	 * Publish into ROS topics: cmd_vel_mux Angular Z
	 */
	@OPERATION void rotate(int degree){
		logger1.info("ArtVelocity >> rotate() " + degree + " degrees");
		twist = publisherVelocity.newMessage();
		float localSpeed = this.speed;
		localSpeed = localSpeed - 0.1f;
		//localSpeed = localSpeed + 0.38f;
		
		
		if (degree < 0)
			{
			localSpeed = localSpeed * -1;
			degree = degree * -1;
			}
		
		twist.getLinear().setX(0); twist.getLinear().setY(0); twist.getLinear().setZ(0);
		twist.getAngular().setX(0); twist.getAngular().setY(0); twist.getAngular().setZ(localSpeed);
		
		
		
		for (short i=0; i<degree/30; i++)
			{
			publisherVelocity.publish(twist);
			//sleep(500);
			sleep(1000);
			}
		sleep(1000);
		}
		
	/**
	 * Get current sensor values
	 */
	@OPERATION void getCurrent3(){
		logger1.info("ArtVelocity >> getCurrent()");
		ObsProperty prop = getObsProperty(propertyName);
		prop.updateValues(prop.getValues());
		signal("tick_cmd_vel_mux");
		}		
	}
