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
import kobuki_msgs.BumperEvent;
import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

/**
 * ROS artifact that handles <code>/mobile_base/events/bumper topic</code>.
 * 
 * @author Talles De Souza Perozzo <talles.perozzo@acad.pucrs.br>
 * @version 1.0
 * @since 2014-12-8
 */
public class ArtBumper extends JaCaRosArtifact {
    Subscriber < kobuki_msgs.BumperEvent > subscriberBumper;
    kobuki_msgs.BumperEvent m_Bumper;
    private static String rosNodeName = "ArtBumper";
    private String propertyName = "Bumper";
    private String topicName = "/mobile_base/events/bumper";
    private String topicType = kobuki_msgs.BumperEvent._TYPE;
    byte currentBumper = 0;
	byte currentState = 0;
    ReadCmd cmd;

    /**
     * Creates ROS node
     */
    public ArtBumper() {
        super(rosNodeName);
        logger1.info("ArtBumper >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtBumper >> init(agentName)");
        defineObsProperty(propertyName, 0);
		
        if (agentName != null) {
            // Update topic name with agent name
            topicName = "/" + agentName + topicName;
        }

        cmd = new ReadCmd();

        subscriberBumper = (Subscriber < kobuki_msgs.BumperEvent > ) createSubscriber(topicName, topicType);
        subscriberBumper.addMessageListener(new MessageListener < BumperEvent > () {

            @
            Override
            public void onNewMessage(BumperEvent message) {
                currentBumper = message.getBumper();
				currentState = message.getState();
				//logger1.info("BUMPER " + currentBumper);
				//logger1.info("STATE " + currentState);
				//signal("tick_Bumper");
                execInternalOp("receiving");
            }
        });
        logger1.info("ArtBumper >> init(String agentName): end of init(String agentName)");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtBumper >> init()");
        init(null);
        logger1.info("ArtBumper >> end of init()");
    }

    @
    INTERNAL_OPERATION
    void receiving() {
		
        await(cmd);
        //signal("tick_Bumper");
		if((currentBumper == 0)&&(currentState==1))
			signal("tick_Bumper_LEFT");
		if((currentBumper == 1)&&(currentState==1))
			signal("tick_Bumper_CENTER");
		if((currentBumper == 2)&&(currentState==1))
			signal("tick_Bumper_RIGHT");
		sleep(10);
    }

    /**
     * The ReadCmd implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method.
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
                ObsProperty prop = getObsProperty(propertyName);
                prop.updateValues(currentBumper);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Get current sensor values
     */
    @
    OPERATION byte getCurrentBumper() {
        logger1.info("ArtBumper >> getCurrentBumper()");
        ObsProperty prop = getObsProperty(propertyName);
        prop.updateValues(currentBumper);
        signal("tick_Bumper");
        return currentBumper;
    }
}