/**
 * class from http://docs.rosjava.googlecode.com/hg/rosjava_core/html/getting_started.html
 *
 */
package jason.architecture;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
//import org.ros.node.Node;

/***
 * ROS node to be used by Jason
 * 
 * @author Google Code
 * @version 1.0
 * @since 2014-06-02
 *
 */
public class RosNode extends AbstractNodeMain {

    ConnectedNode m_connectedNode;

//    public RosNode() {
//        System.out.println("RosNode >> Construtor");
//    }

    public ConnectedNode getConnectedNode() {
//        System.out.println("RosNode >> getConnectedNode()");
//        System.out.println("RosNode >> getConnectedNode(): m_connectedNode=" + m_connectedNode);
//        if (m_connectedNode != null)
//            System.out.println("RosNode >> getConnectedNode(): m_connectedNode=" + m_connectedNode.getName());
        return m_connectedNode;
    }

    @
    Override
    public GraphName getDefaultNodeName() {
        GraphName graphName = GraphName.of("JaCaROS");
//        System.out.println("RosNode >> getDefaultNodeName()");
//        System.out.println("RosNode >> getDefaultNodeName(): GraphName=" + graphName);
        return graphName;
    }

    @
    Override
    public void onStart(ConnectedNode connectedNode) {
//        System.out.println("RosNode >> onStart");
        m_connectedNode = connectedNode;
    }

//    @
//    Override
//    public void onShutdown(Node node) {
//        System.out.println("RosNode >> onShutdown()");
//    }
//
//    @
//    Override
//    public void onShutdownComplete(Node node) {
//        System.out.println("RosNode >> onShutdownComplete()");
//    }
//
//    @
//    Override
//    public void onError(Node node, Throwable throwable) {
//        System.out.println("RosNode >> onError()");
//    }

};