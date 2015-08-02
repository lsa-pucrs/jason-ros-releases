/**
 * JaCaROS main class
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

import org.apache.commons.logging.Log;
import org.ros.exception.RosRuntimeException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.node.NodeMainExecutor;
import org.ros.node.service.ServiceResponseBuilder;
import org.ros.node.topic.Subscriber;
import cartago.Artifact;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Artifact extension for the use of ROS features
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-06-02
 */
public class JaCaRosArtifact extends Artifact {
	
	/** ROS node to be used by Jason */
	RosNode m_rosnode;
	
	/** Name of our node Class */
	String[] nodeClassName = {"jason.architecture.RosNode"};
	
	/** Logger */
	public Log logger1;
	
	/** The current id to be used by the node  (avoid node name duplicity)*/
	private static long currentNodeId = 0;
	
	/** agent name used by Jason */    
    String m_agentName; //revisit (this name should come from Jason, right?)
    

    /***
     * main (not used)
     * @param args
     */
    public static void main(String[] args) {
    	System.out.println("JaCaRosArtifact >> main(args)");
    	System.err.println("Cannot run this directly");
    	System.exit(1);
    }
	
	/** Constructor */
	public JaCaRosArtifact()
	{
		logger1.info("JaCaRosArtifact >> Constructor() with no name for ROS node.");
		String name = "Noname"; //we should add some name to this ROS node
		connectToROS(name);

		if (logger1==null) {
			logger1 = m_rosnode.getConnectedNode().getLog();
		}
	}
	
	/***
	 * Constructor
	 * @param name Name of the node 
	 */
	public JaCaRosArtifact(String name)
	{
		//System.out.println("JaCaRosArtifact >> Constructor(String name)");
		//System.out.println("JaCaRosArtifact >> Constructor(String name) current name is: " + name);		
		connectToROS(name);

		if (logger1==null) {
			//System.out.println("JaCaRosArtifact >> Constructor(String name) creating new logger.");
			logger1 = m_rosnode.getConnectedNode().getLog();
		}
		//logger1.info("JaCaRosArtifact >> Constructor(String name): end of method.");
	}

	/***
	 * Method use to connect a new node to ROS
	 * based on the following ROS class: http://docs.ros.org/hydro/api/rosjava_core/html/RosRun_8java_source.html
	 * @param name Name of the node
	 */
	private void connectToROS(String name) {
		CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(nodeClassName));
		//System.out.println("JaCaRosArtifact >> Constructor(String name) current loader: " + loader.getNodeClassName());
		NodeConfiguration nodeConfiguration = loader.build();
		//System.out.println("JaCaRosArtifact >> Constructor(String name) current nodeConfiguration: " + nodeConfiguration.toString());
		name = name + "_" + getNextLongInt(); //avoid nodes with the same name
		nodeConfiguration.setNodeName(name);
		System.out.println("JaCaRosArtifact >> Constructor(String name) ROS node name: " + name);
		NodeMain nodeMain = null;
		try {
			//System.out.println("JaCaRosArtifact >> Constructor(String name) current nodeClassName: " + nodeClassName[0]);
			nodeMain = loader.loadClass(nodeClassName[0]);    		
			//System.out.println("JaCaRosArtifact >> Constructor(String name) current nodeMain: " + nodeMain.getDefaultNodeName());
		}
		catch (ClassNotFoundException e) {
			throw new RosRuntimeException("Unable to locate node: " + nodeClassName[0], e);
		}
		catch (InstantiationException e) {
			throw new RosRuntimeException("Unable to instantiate node: " + nodeClassName[0], e);
		}
		catch (IllegalAccessException e) {
			throw new RosRuntimeException("Illegal Access to node: " + nodeClassName[0], e);
		}
		catch (Exception e){
			throw new RosRuntimeException("Unable to create node: " + nodeClassName[0], e);
		}

		Preconditions.checkState(nodeMain != null);
		NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
		//System.out.println("JaCaRosArtifact >> Constructor(String name) current nodeMainExecutor: " + nodeMainExecutor.toString());
		nodeMainExecutor.execute(nodeMain, nodeConfiguration);
		m_rosnode = (RosNode)nodeMain;

		System.out.println("JaCaRosArtifact >> Constructor(String name) trying to connect..." );
		while( m_rosnode.getConnectedNode() == null ) sleep(1000);
		System.out.println("JaCaRosArtifact >> Constructor(String name) connecting done!" );
		//System.out.println("JaCaRosArtifact >> Constructor(String name) current m_rosnode: " + m_rosnode.toString());
	}	

	/**
	 * Creates a new publisher node
	 * 
	 * @param topicName The name of ROS topic
	 * @param topicType The type of ROS topic
	 * @return returns the publisher object (it is used to publish data into ROS topic)
	 */
	public Object createPublisher(String topicName, String topicType){
		//logger1.info("JaCaRosArtifact >> createPublisher()");
		Object result = null;
		result = m_rosnode.getConnectedNode().newPublisher(topicName, topicType);
		logger1.info("JaCaRosArtifact >> createPublisher(): Topic= " + topicName + " | Type= " + topicType);
		sleep(5000);
		return result;    
	}

	/**
	 * Creates a new subscriber node
	 * 
	 * @param topicName The name of ROS topic
	 * @param topicType The type of ROS topic
	 * @return returns the subscriber object (it is used to receive data from ROS topic)
	 */
	public Object createSubscriber(String topicName, String topicType){
		//logger1.info("JaCaRosArtifact >> createSubscriber()");
		Object result = null;
		result = m_rosnode.getConnectedNode().newSubscriber(topicName, topicType);
		logger1.info("JaCaRosArtifact >> createSubscriber(): Topic= " + topicName + " | Type= " + topicType);
		sleep(5000);
		return result;
	}
	
	/**
	 * Creates a new Server
	 * 
	 * @param serviceName The name of ROS Service
	 * @param serviceType The type of ROS Service
	 * @param builder The Service builder
	 * @return returns the service server object (it is used to write data into ROS service)
	 */
	public Object createServer(String serviceName, String serviceType, ServiceResponseBuilder builder) {
		//logger1.info("JaCaRosArtifact >> createServer()");
		Object result = null;
		result = m_rosnode.getConnectedNode().newServiceServer(serviceName, serviceType, builder);
		logger1.info("JaCaRosArtifact >> createServer(): Service Name= " + serviceName + " | Type= " + serviceType);
		return result;
	}
	
	/**
	 * Creates a new Client
	 * 
	 * @param clientName The name of ROS Service
	 * @param topicType The type of ROS Service
	 * @return returns the client object (it is used to read data from ROS service)
	 * @throws ServiceNotFoundException
	 */
	public Object createClient(String clientName, String topicType) throws ServiceNotFoundException{
		logger1.info("JaCaRosArtifact >> createClient()");
		Object result = null;
		result = m_rosnode.getConnectedNode().newServiceClient(clientName, topicType);
		logger1.info("JaCaRosArtifact >> createClient(): Topic= " + clientName + " | Type= " + topicType);
		return result;
	}
	
	/**
	 * Implementation of a simple id to be used by nodes
	 * @return a unique long integer
	 */
	private long getNextLongInt(){		
		return currentNodeId++;
	}
	
	/***
	 * Returns a very high value when a NaN is found 
	 * @param value value to check if is NaN or not
	 * @return 50000f
	 */
	public Float checkNan(Float value){
		//logger1.info("JaCaRosArtifact >> checkNan(Float value)");
		if (value.isNaN()) {
			//logger1.info("JaCaRosArtifact >> checkNan(Float value): an NaN was found.");
			value = 50000f;
		}
		return value;
	}

	/***
	 * Returns a very high value when a NaN is found
	 * @param value value to check if is NaN or not
	 * @return 50000f
	 */
	public Float[] checkNan (Float[] value)
	{
		logger1.info("JaCaRosArtifact >> checkNan(Float[] value)");		
		for (int i=0; i< value.length; i++)
		{
			if (value[i].isNaN()) {
				logger1.debug("JaCaRosArtifact >> checkNan(Float[] value): an NaN was found.");
				value[i] = 5000f;
			}
		}
		return value;
	}

	/**
	 * Very simple sleep implementation
	 * @param wait time to wait
	 */
	public void sleep(int wait) {
		if (logger1!=null){
			logger1.info("JaCaRosArtifact >> sleep(): Sleeping " + wait + " milliseconds");			
		}else{
			System.out.printf("JaCaRosArtifact >> sleep(): Sleeping %d milliseconds", wait );			
		}		

		try {
			Thread.sleep(wait);
		}
		catch (InterruptedException e) {
			throw new RosRuntimeException("JaCaRosArtifact >> sleep(): Thread cannot wait ", e);
		}
	}

	/**
	 * Sleep implementation without logs
	 * @param wait time to wait
	 */
	public void sleepNoLog(int wait) {
		try {
			Thread.sleep(wait);
		}
		catch (InterruptedException e) {
		}
	}	
	
	/***
	 * Returns the name of the agent
	 * @return The Agent Name
	 */
	public String getAgName() {
		//logger1.info("JaCaRosArtifact >> getAgName()");
		return m_agentName;
	}
    
}