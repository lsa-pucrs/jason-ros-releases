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

import org.ros.message.MessageListener;
import org.ros.node.topic.Subscriber;

import perception_msgs.perception;

import java.util.List;
import java.util.ArrayList;

/**
 * ROS artifact that handles Marcio's ROS topic. Based on CArtAgO artifact
 *
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-06-07
 */
public class ArtPerception extends JaCaRosArtifact {	
    Subscriber <perception_msgs.perception> subscriberPerception;
    perception_msgs.perception m_perception;
    private static String rosNodeName = "ArtPerception";
    private String propertyName = "perception";
    private String topicName = "/jason/perception";
    private String topicType = perception_msgs.perception._TYPE;
    List <String> currentPerception = new ArrayList < String > ();
    ReadCmd cmd;

    /**
     * Creates ROS node
     */
    public ArtPerception() {
        super(rosNodeName);
        //super();
        logger1.info("ArtPerception >> construtor()");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init(String agentName) {
        logger1.info("ArtPerception >> init(agentName)");
        logger1.info("ArtPerception >> init(agentName): agent name=" + agentName);
        logger1.info("ArtPerception >> init(agentName): Defining artifact property " + propertyName);
        //defineObsProperty(propertyName,currentPerception);
        defineObsProperty(propertyName, "oi");

        if (agentName != null) {
            // Update topic name with agent name
            topicName = "/" + agentName + topicName;
        }

        cmd = new ReadCmd();

        subscriberPerception = (Subscriber < perception > ) createSubscriber(topicName, topicType);
        subscriberPerception.addMessageListener(new MessageListener < perception_msgs.perception > () {
        	@Override
            public void onNewMessage(perception_msgs.perception message) {
                logger1.info("I heard (from Artifact do Marcio)" + message);
                currentPerception = message.getPerception();
                execInternalOp("receiving");
            }
        });

        logger1.info("ArtPerception >> init(agentName): end of method");
    }

    /**
     * Initialize communication on ROS topic
     */
    void init() {
        logger1.info("ArtPerception >> init()");
        init(null);
        logger1.info("ArtPerception >> end of init()");
    }

    @
    INTERNAL_OPERATION
    void receiving() {
        logger1.info("ArtPerception >> receiving()");
        await(cmd);
        signal(propertyName);
    }

    /**
     * The ReadCmd implements a blocking command – implementing the IBlockingCmd interface – containing the command code in the exec method.
     *
     * @author Rodrigo Wesz
     *
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
                logger1.info("porcaria"+currentPerception.get(0));
                logger1.info("porcaria2"+prop);                
                prop.updateValues(currentPerception.get(0));
                logger1.info("porcaria3"+prop);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
    
    double getAxisLength(double seno_cosseno, double hipotenusa) {
        return seno_cosseno * hipotenusa;
    }

    double getSin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    double getCos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    double getTan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

    /**
     * Get current sensor values
     */
    @
    OPERATION void getPerception() {
        // In order to simulate a perception, run: rostopic pub -1 /jason/perception perception_msgs/perception "{perception:['box1,1.0,2.0,3.1']}"
        //		System.out.println("[JaCaRosArtifact - ArtPerception] at getCurrent9()");
        //		ObsProperty prop = getObsProperty("perception");
        //		prop.updateValues(prop.getValues());
        //		//prop.updateValue(prop.intValue());
        //		//prop.updateValues(prop.floatValues());
        //		System.out.println("[JaCaRosArtifact - ArtPerception] at getCurrent9(). Value:" + prop);
        //		for(String listItem : PerceptionAtual)
        //			{
        //			System.out.println("[JaCaRosArtifact - ArtPerception] at getCurrent9(). Item:"  + listItem);
        //			}
        //		signal("tick_perception");
        //		System.out.println("[JaCaRosArtifact - ArtPerception] FIM DO getCurrent9()");
    }
    /*
	http://www.guj.com.br/java/161225-como-retornar-seno--e-co-seno-e-tangente-de-um-angulo
	http://www.portugal-a-programar.pt/topic/30480-calcular-cosseno/
	http://www.guj.com.br/java/86996-seno-cosseno-e-tangente
	http://pt.wikipedia.org/wiki/Trigonometria#Lei_dos_senos
	radianos = graus * (3.14159265 / 180); ou radianos = Math.toRadians(graus); //Não uses essa aproximação para o numero pi. Usa antes Math.PI.
	---------
	Possuindo seno e hipotenusa, consigo o valor do cateto oposto (nosso caso, eixo y) - http://www.somatematica.com.br/fundam/raztrig/razoes.php
	Possuindo cosseno e hipotenusa, consigo o valor do cateto adjacente (nosso caso, eixo x) - http://www.somatematica.com.br/fundam/raztrig/razoes.php

	http://www.somatematica.com.br/fundam/raztrig/razoes.php
	http://www.somatematica.com.br/fundam/raztrig/razoes2.php

	http://pt.wikipedia.org/wiki/Abscissa
	http://pt.wikipedia.org/wiki/Ordenada

	http://aprovadonovestibular.com/seno-cosseno-e-tangente-como-calcular-e-exercicios.html
	http://www.brasilescola.com/matematica/seno-cosseno-tangente-angulos.htm (tabela)
 */
/*
	double getLengthX(double seno, double hipotenusa)
	{
		// seno = oposto / hipotenusa -> oposto = seno * hipotenusa
		return seno * hipotenusa;
	}

	double getLengthY(double cosseno, double hipotenusa)
	{
		// cosseno = adjacente / hipotenusa -> adjacente = seno * hipotenusa
		return cosseno * hipotenusa;
	}
 */
 /*
cd git/icra/projects/catkin_ws/
source /opt/ros/hydro/setup.bash
source devel/setup.bash 
rosrun perception visual_synth

cd git/icra/projects/catkin_ws/
source /opt/ros/hydro/setup.bash
source devel/setup.bash 
rostopic list
rostopic pub -1 /jason/perception perception_msgs/perception "{perception:['box1,0.0,0.0,0.0']}"
 */
}