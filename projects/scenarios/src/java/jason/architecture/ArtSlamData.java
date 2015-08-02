/**
 * ROS artifact code for project jacaros00
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
import java.util.Hashtable;
import java.util.Enumeration;
//import jason.architecture.*;

/**
 * Artifact that storage SLAM data. Based on CArtAgO artifact
 * 
 * @author Rodrigo Wesz <rodrigo.wesz@acad.pucrs.br>
 * @version 1.0
 * @since 2014-07-03
 */
public class ArtSlamData extends Artifact /*JaCaRosArtifact*/ {
	
	public Hashtable slam_data;
	
	/**
	 * Initialize Hashtable
	 */
	void init(String direcao){
		System.out.println("[JaCaRosArtifact - ArtSlamData] init()");
		
		System.out.println("[JaCaRosArtifact - ArtSlamData] Criando Hashtable");
		// Creating Hashtable for example
		slam_data = new Hashtable();
		
		//slam_data.put("Google", "United States");
		//slam_data.put("Nokia", "Finland");
		//slam_data.put("Sony", "Japan");
		//http://javarevisited.blogspot.com.br/2012/01/java-hashtable-example-tutorial-code.html
		//System.out.println("[JaCaRosArtifact - ArtSlamData] init() dormindo 10 seg");
		//sleep(10000);
		defineObsProperty("route", direcao);		
		defineObsProperty("degrees", 0);		
		defineObsProperty("teste",-50,-50,"string_teste");
		}
		
//	/**
//	 * Add new object into Hashtable
//	 */
//	 @OPERATION void updateSlamData(String key, String value){
//		 System.out.println("[JaCaRosArtifact - ArtSlamData] updateSlamData()");
//		 //String key = ""; String value = "";
//		 System.out.println(" key: " + key);
//		 System.out.println(" values: " + value);
//	 }
/**
     * very simple sleep implementation
     * @param wait time to wait
     */
    public void localSleep(int wait) {
    	System.out.printf("[Java: JaCaRosArtifact] Sleeping %d milliseconds", wait );
    	try {
    		Thread.sleep(wait);
    		}
    	catch (InterruptedException e) {}
    	}
	 
	 /**
	 * Add new object into Hashtable
	 */
	 @OPERATION void updateSlamData(Byte x, Byte y, String value){
		 System.out.println("[JaCaRosArtifact - ArtSlamData] updateSlamData()");
		 //String key = ""; String value = "";
		 System.out.println(" key: " + x.toString() + "_" + y.toString());
		 System.out.println(" values: " + value.toString());
		 slam_data.put(x.toString() + "_" + y.toString(), value);
	 }
		
	/**
	 * Prints Hashtable
	 */
	 @OPERATION void printSlamData(){
		 
		 System.out.println("[JaCaRosArtifact - ArtSlamData] print()");
		 Enumeration enum1 = slam_data.elements();
		 Enumeration enum2 = slam_data.keys();
		 
		 System.out.println("=================================");
		 while (enum1.hasMoreElements()) {			 
			 System.out.print("key: " + enum2.nextElement() + " ");
			 System.out.println(" values: " + enum1.nextElement());
			 }
		 System.out.println("=================================");
	 }
	 
	 /**
	 * Clean Hashtable to reuse it
	 */
	 @OPERATION void clearSlamData(){
		 slam_data.clear();
	 }
  
	/**
	 * Helper for Degrees
	 */
	short getDegreesFromRoute(String route){
		System.out.println("[JaCaRosArtifact - ArtSlamData] getDegrees()");
		
		/*final short NORTH_DEGREE = 0;
		final short EAST_DEGREE = 90;
		final short SOUTH_DEGREE = 180;
		final short WEST_DEGREE = 270;*/
		
		final short NORTH_DEGREE = 0;
		final short EAST_DEGREE = 270;
		final short SOUTH_DEGREE = 180;
		final short WEST_DEGREE = 90;
		
		short result = -1;
		if ( route == "north" || route.equals("north") )
		{
			result = NORTH_DEGREE;
		}else if ( route == "east" || route.equals("east") )
		{
			result = EAST_DEGREE;
		}else if ( route == "south" || route.equals("south") )
		{
			result = SOUTH_DEGREE;
		}else if ( route == "west" || route.equals("west") )
		{
			result = WEST_DEGREE;
		}
		return result;
	}
		 
	/**
	 * Get slam info
	 */
	 @OPERATION void getRoute(Byte current, Byte goal, String axis){
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute()");
		ObsProperty prop = getObsProperty("route");		
		//prop.updateValues(prop.getValue());
				
		String rota = "";
		String rotaAnterior = prop.getValue().toString();
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() PREVIOUS_VALUE=" + rotaAnterior);
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() HOME =" + current);
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() GOAL =" + goal);
		
		if (current == goal)
		{
			System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() ja chegou o disco voador");
			//rota = "reached";
			//prop.updateValues(prop.stringValue());
			prop.updateValues("reached");
		} 
		else if ( axis.toUpperCase() == "X" || axis.toUpperCase().equals("X") )
		{
			System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() eixo X");
			if (current > goal)
			{
				System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() west");
				prop.updateValues("west");
			}
			else
			{
				System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() east");
				prop.updateValues("east");
			}
		}
		else if ( axis.toUpperCase() == "Y" || axis.toUpperCase().equals("Y") )
		{
			System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() eixo Y");
			if (current > goal)
			{
				System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() south");
				prop.updateValues("south");
			}
			else
			{
				System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() north");
				prop.updateValues("north");
			}
		}
		else
		{
			System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() eixo Errado");
		} //axis = String.toUpperCase(axis);
		rota = prop.getValue().toString();
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() roooota "  + rotaAnterior);
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() roootaB " + rota);
		 
		
		ObsProperty propTwo = getObsProperty("degrees");
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() PREVIOUS_DEGREE=" + propTwo.getValue());
		
		short graus1 = getDegreesFromRoute(rotaAnterior);
		short graus2 = getDegreesFromRoute(rota);
		System.out.println("");
		System.out.println("");
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() G1 "  + graus1);
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() G2 " + graus2);
		System.out.println("++++++++++++++++++++++++++++++++++++++++");
		
		if (rota == "reached")
		{
			System.out.println("Não se mecher, pois rota = reached");
			graus2 = graus1;
		}
		
		short grausFinal = (short) (graus2 - graus1);
		/*
		if (grausFinal > 0)
		{
			//grausFinal = (short) (grausFinal + 60); // ((grausFinal/90) * 60)
			grausFinal = (short) (grausFinal + ((grausFinal/90) * 60));
		}
		else
		{
			grausFinal = (short) (grausFinal + ((grausFinal/90) * -60));
		}
		*/
		propTwo.updateValues(grausFinal);
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() FINAL_DEGREE=" + propTwo.getValue());		
		
		signal("tick_route");
		
		System.out.println("[JaCaRosArtifact - ArtSlamData] getRoute() ################ saindo");
	 }
	 
	 /**
	 * Update current position
	 */
	 @OPERATION void updatePos(String route, Byte x, Byte y){
		 System.out.println("[JaCaRosArtifact - ArtSlamData] updatePos() ");
		 }
		 
	/**
	 * Set a new value for route and degrees
	 */
	@OPERATION void updateProperties(String route, short degrees)
	{
		ObsProperty prop = getObsProperty("route");
		ObsProperty propTwo = getObsProperty("degrees");
		
		prop.updateValues(route);
		propTwo.updateValues(degrees);
	}
	
	/**
	*
	*/
	@OPERATION void getVisionRange(int x, int y, int range) {
		System.out.println("[JaCaRosArtifact - ArtSlamData] getVisionRange()");
		ObsProperty prop = getObsProperty("teste");
		Object position = null;
		position=slam_data.get("0_0");
		System.out.println("posicao dentro do artefato ANTES:" + position);
		slam_data.put("0_0", "oi_oi");
		position=slam_data.get("0_0");
		System.out.println("posicao dentro do artefato:" + position);
		prop.updateValues(position);
		//int a = x;
		//int b = y;
		//if (x==0) { x = 1; }
		//if (y==0) { y = 1; }
		for (int a=x-range; a<=x+range; a++) {
			for(int b=y-range; b<=y+range; b++) {
				
				if (slam_data.containsKey(a+"_"+b)) {
					position=slam_data.get(a+"_"+b);
					System.out.println("MA OI " + a+"_"+b+": "+position);
					
					Object[] currentSlamData = prop.getValues();
					prop.updateValues(a,b,position);
					localSleep(500);
					System.out.println("\nsinal =================================");
					signal("tick_oi");
				}
				else {
					System.out.println(a+"_"+b + " key not found !!!!!!!!!!!!!!!!");
					}
				}
			}
			System.out.println("\n sai do FOR");
			//System.out.println("\nsinal");
			//signal("tick_oi");
		}
	}

