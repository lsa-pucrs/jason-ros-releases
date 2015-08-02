/**
 * ROS artifact code for ROS Tutorials
 * http://wiki.ros.org/ROS/Tutorials
 * 
 * This class provides a listener for publisher/subscriber tutorial
 * and a client for service/client tutorial 
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

import org.ros.exception.RemoteException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.message.MessageListener;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;
import org.ros.node.topic.Subscriber;

import test_rosjava_jni.AddTwoInts;
import test_rosjava_jni.AddTwoIntsRequest;
import test_rosjava_jni.AddTwoIntsResponse;
import cartago.IBlockingCmd;
import cartago.INTERNAL_OPERATION;
import cartago.OPERATION;
import cartago.ObsProperty;

/**
 * ROS artifact that handles /chatter topic and /add_two_ints service.
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2015-03-01
 */
public class ArtTutorial extends JaCaRosArtifact {
	/** Jason Node name */
	private static String rosNodeName = "ArtTutorial";
	
	/* service properties */
	/** Property responsible to connects with AddTwoInts service */
	private ServiceClient<AddTwoIntsRequest, AddTwoIntsResponse> serviceClient;
	private long currentSum;
	/** Property to be observable within artifact */
	private String propertyNameSum = "twoIntsSum";
	private ReadCmdSum cmdSum;	
    
	/* chatter properties */
	/** Property responsible to connects with chatter topic */
	private Subscriber<std_msgs.String> subscriberChatter;	
	private String topicName = "/chatter";
    private String topicType = std_msgs.String._TYPE;
    private String currentMessage;
    /** Property to be observable within artifact */
	private String propertyNameChatter = "chatter";
	private ReadCmdMsg cmdMsg;
    
    /** Creates ROS node */
    public ArtTutorial() {
        super(rosNodeName);
        //logger1.info("ArtTutorial >> constructor()");
    }

    /**
     * Initialize communication with ROS
     * @param agentName string used for disambiguation at multi-agent environments
     */
    void init(String agentName) {
        //logger1.info("ArtTutorial >> init()");
        //logger1.info("ArtTutorial >> init(): agent name=" + agentName);

        if (agentName != null) {            
            topicName = "/" + agentName + topicName; // Update topic name with agent name            
            propertyNameChatter = agentName + "_" + propertyNameChatter;
            propertyNameSum =  agentName + "_" + propertyNameSum;
        }
        
        defineObsProperty(propertyNameChatter, "");
        cmdMsg = new ReadCmdMsg();
        defineObsProperty(propertyNameSum, 0);
        cmdSum = new ReadCmdSum();
        
        try {
			serviceClient = (ServiceClient<AddTwoIntsRequest, AddTwoIntsResponse>) createClient("add_two_ints", AddTwoInts._TYPE);
		} catch (ServiceNotFoundException e) {
			// TODO Auto-generated catch block
			//throw new RosRuntimeException(e);
			logger1.info("ArtTutorial >> init() Service not found!");
			e.printStackTrace();
		}
        
        subscriberChatter = (Subscriber <std_msgs.String>) createSubscriber(topicName, topicType);
        subscriberChatter.addMessageListener(new MessageListener <std_msgs.String> () {
        	@Override
        	public void onNewMessage(std_msgs.String message) {
        		//logger1.info("I heard (from Artifact)" + message);        		        		
        		currentMessage = message.getData();
        		execInternalOp("receivingMsg");
            }
        });
        //logger1.info("ArtTutorial >> init(): end of method");
    }

    /** Initialize communication on ROS topic */
    void init() {        
        init(null);        
    }
    
    /** Get current sensor values */
    @OPERATION void getCurrentMessage() {
    	//logger1.info("ArtTutorial >> getCurrentMessage()");
    	ObsProperty prop = getObsProperty(propertyNameChatter);
    	if(currentMessage!= null)
    		prop.updateValues(currentMessage);
    	else
    		prop.updateValues("Error: current message is null");
    	//logger1.info("ArtTutorial >>  getCurrentMessage() >> Observable Property Value: " + prop);
        signal(propertyNameChatter);
        //logger1.info("ArtTutorial >>  FIM DO getCurrentMessage()");
    }
	
    /**
     * Adds two integers using AddTwoInts ROS service
     * @param valueA First integer value
     * @param valueB Second integer value
     */
	@OPERATION void sum(int valueA, int valueB){
		//if (serviceClient==null)
		//	logger1.info("ArtTutorial >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  sum()");				
		//logger1.info("ArtTutorial >>  sum()");
		final AddTwoIntsRequest request = serviceClient.newMessage();
	    request.setA(valueA);
	    request.setB(valueB);
	    serviceClient.call(request, new ServiceResponseListener<AddTwoIntsResponse>() {
	      @Override
	      public void onSuccess(AddTwoIntsResponse response) {
	    	  currentSum = response.getSum();
	    	  //logger1.info(String.format("%d + %d = %d", request.getA(), request.getB(), currentSum));
	    	  execInternalOp("receivingSum");	    	  	    	  
	      }

	      @Override
	      public void onFailure(RemoteException e) {
	    	  currentSum = 0;
	        //throw new RosRuntimeException(e);
	      }
	    });
	    //logger1.info("ArtTutorial >>  FIM DO sum()");
	}
	
    @INTERNAL_OPERATION
    void receivingMsg() {
        //logger1.info("ArtOdometry >> receivingMsg()");
        await(cmdMsg);
        signal(propertyNameChatter);
        //sleepNoLog(500);
    }
    
    /** The ReadCmdMsg implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method. */
    class ReadCmdMsg implements IBlockingCmd {
        /** The command code */
        public void exec() {
            try {
                ObsProperty prop = getObsProperty(propertyNameChatter);
                prop.updateValues(currentMessage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
	
    @INTERNAL_OPERATION
    void receivingSum() {
        //logger1.info("ArtOdometry >> receivingSum()");
        await(cmdSum);
        signal(propertyNameSum);
        //sleepNoLog(500);
    }

    /** The ReadCmdSum implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method. */
    class ReadCmdSum implements IBlockingCmd {
        /** The command code */
        public void exec() {
            try {
                ObsProperty prop = getObsProperty(propertyNameSum);
                prop.updateValues(currentSum);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}