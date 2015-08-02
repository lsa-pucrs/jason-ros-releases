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
import kobuki_msgs.CliffEvent;
import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

/**
 * ROS artifact that handles <code>/mobile_base/events/cliff topic</code>.
 * 
 * @author Talles De Souza Perozzo <talles.perozzo@acad.pucrs.br>
 * @version 1.0
 * @since 2014-12-8
 */
public class ArtCliff extends JaCaRosArtifact {
    Subscriber < kobuki_msgs.CliffEvent > subscriberCliff;
    kobuki_msgs.CliffEvent m_Cliff;
    private static String rosNodeName = "Cliff";
    private String propertyName = "Cliff";
    private String topicName = "/mobile_base/events/cliff";
    private String topicType = "kobuki_msgs/CliffEvent";
    byte currentCliff = 0;
	byte currentState = 0;
	int distance = 0;
    ReadCmd cmd;

    /**
     * Creates ROS node
     */
    public ArtCliff() {
        super(rosNodeName);
        logger1.info("ArtCliff >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtCliff >> init(agentName)");
        
        //defineObsProperty(propertyName,currentCliff);
        defineObsProperty(propertyName, 0);
        cmd = new ReadCmd();

        subscriberCliff = (Subscriber < kobuki_msgs.CliffEvent > ) createSubscriber(topicName, topicType);
        subscriberCliff.addMessageListener(new MessageListener < CliffEvent > () {

            @
            Override
            public void onNewMessage(CliffEvent message) {
                //logger1.info("I heard CLIFF " + message);
                currentCliff = message.getSensor();
				currentState = message.getState();
				distance = message.getBottom();
				//logger1.info("CLIFF " + currentCliff);
				//logger1.info("STATE " + currentState);
                //logger1.info("DISTANCE TO THE FLOOR " + distance);
				execInternalOp("receiving");
            }
        });        
    }

    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtCliff >> init()");
        init(null);
        logger1.info("ArtCliff >> end of init()");
    }

    @
    INTERNAL_OPERATION
    void receiving() {
        
		await(cmd);
		if((currentCliff == 0)&&(currentState==1))
			signal("tick_Cliff_LEFT");
		if((currentCliff == 1)&&(currentState==1))
			signal("tick_Cliff_CENTER");
		if((currentCliff == 2)&&(currentState==1))
			signal("tick_Cliff_RIGHT");
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
                prop.updateValues(currentCliff);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Get current sensor values
     */
    @
    OPERATION byte getCurrentCliff() {
        logger1.info("ArtCliff >> getCurrentCliff()");
        ObsProperty prop = getObsProperty(propertyName);
        prop.updateValues(currentCliff);
        signal("tick_Cliff");
        return currentCliff;
    }
}
