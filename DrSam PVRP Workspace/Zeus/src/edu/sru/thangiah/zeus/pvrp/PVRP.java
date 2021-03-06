package edu.sru.thangiah.zeus.pvrp;

import java.io.*;
import java.util.*;

import edu.sru.thangiah.zeus.core.*;
import edu.sru.thangiah.zeus.pvrp.pvrpqualityassurance.*;
import edu.sru.thangiah.zeus.pvrp.pvrpnodeslinkedlist.*;
import edu.sru.thangiah.zeus.gui.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.sru.thangiah.zeus.pvrp.PVRPShipmentLinkedList.*;


/**
 *
 * <p>Title:</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Sam R. Thangiah
 * @version 2.0
 */

public class PVRP {

	int m = 0, //number of vehicles
			n = 0, //number of customers
			t = 0, //number of daysList
			D = 0, //maximum duration of route
			Q = 0; //maximum capacity of vehicle

	long startTime, endTime; //track the CPU processing time
	private Vector mainOpts = new Vector(); //contains the collections of optimizations
	private Vector optInformation = new Vector(); //contains information about routes
	private PVRPShipmentLinkedList mainShipments = new PVRPShipmentLinkedList(); //customers read in from a file or database that are available
	private PVRPDepotLinkedList mainDepots = new PVRPDepotLinkedList(); //depots linked list for the VRP problem
	private PVRPQualityAssurance vrpQA; //check the integrity and quality of the solution
	private List<PVRPDays> daysList = new ArrayList<PVRPDays>();

	//constructor for the class
	public PVRP(String dataFile) throws IOException
	{

		//Truck types are placed into a vector
		ProblemInfo.truckTypes = new Vector();

		//Type of shipment insertion to be performed
		//ProblemInfo.insertShipType = new Object();

		boolean isDiagnostic = false;
		Shipment tempShip;
		Depot thisDepot;
		int type;
		int depotNo;
		int countAssignLoop;
		boolean status;
		String outputFileName;

		/** @todo  Need to put in a PVRP file and read in PVRP data. The readfile method will have to be changed to match the format of the
		 * pvrp file*/
		//read in the MDVRP data
		readDataFromFile(ProblemInfo.inputPath + dataFile);
		Settings.printDebug(Settings.COMMENT,
				"Read Data File: " + ProblemInfo.inputPath + dataFile);
		printDataToConsole();
//		writeDataFile(dataFile.substring(dataFile.lastIndexOf("/") + 1));
		writeDataFile(ProblemInfo.outputPath + "OUTPUT_" + dataFile);

		//Ensure that the shipment linked list has been loaded with the data
		if (mainShipments.getPVRPHead() == null) {
			Settings.printDebug(Settings.ERROR,
					"PVRP: Shipment linked list is empty");
		}

//		Set up the shipment selection type
		ProblemInfo.selectShipType = new PVRPNextNodeHeuristics();
		Settings.printDebug(Settings.COMMENT,PVRPNextNodeHeuristics.WhoAmI());
		//ProblemInfo.selectShipType = new SmallestPolarAngleToDepot();
//		Settings.printDebug(Settings.COMMENT, SmallestPolarAngleToDepot.WhoAmI());
		//ProblemInfo.selectShipType = new SmallestPolarAngleShortestDistToDepot();
//		Settings.printDebug(Settings.COMMENT,SmallestPolarAngleShortestDistToDepot.WhoAmI());
//
//		set up the shipment insertion type
		ProblemInfo.insertShipType = new PVRPInitialInsertShipment();
		Settings.printDebug(Settings.COMMENT, PVRPInitialInsertShipment.WhoAmI());

		//Capture the CPU time required for solving the problem
		startTime = System.currentTimeMillis();
		// captures the initial information on solving the problem
		// returns the total customer and total distance after the initial solution
		createInitialRoutes();
		System.out.println("Completed initial routes");

		//Get the initial solution
		//Depending on the Settings status, display information on the routes
		//Trucks used, total demand, dist, travel time and cost
		Settings.printDebug(Settings.COMMENT, "Created Initial Routes ");
		Settings.printDebug(Settings.COMMENT,
				"Initial Stats: " + mainDepots.getSolutionString());
		//At this point all shipments have been assigned
		writeLongSolution(dataFile.substring(dataFile.lastIndexOf("/") + 1));
		//writeShortSolution(dataFile.substring(dataFile.lastIndexOf("/") + 1));

		//Check for the quality and integrity of the solution
		System.out.println("Starting QA");
		vrpQA = new PVRPQualityAssurance(mainDepots, mainShipments);
		if (vrpQA.runQA() == false) {
			Settings.printDebug(Settings.ERROR, "QA FAILED!");
		}
		else {
			Settings.printDebug(Settings.COMMENT, "QA succeeded");


		}
/** @todo  GUI still needs to be implemented */
		//Call to the graphical user inter face
		//Vector emptyVector = new Vector(0);
		//VRPZeusGui gui = new VRPZeusGui(mainDepots, mainShipments, emptyVector);

//		ZeusGui guiPost = new ZeusGui(mainDepots, mainShipments);

	} //VRP ENDS HERE*******************<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//**********************>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	/**
	 * Creates the initial solution for the problem
	 */
	public void createInitialRoutes() {
		//OptInfo has old and new attributes
		PVRPDepot currDepot = null; //current depot
		PVRPShipment currShip = null; //current shipment
		//int countLoop=0;

		//check if selection and insertion type methods have been selected
		if (ProblemInfo.selectShipType == null) {
			Settings.printDebug(Settings.ERROR,
					"No selection shipment type has been assigned");

		}
		if (ProblemInfo.insertShipType == null) {
			Settings.printDebug(Settings.ERROR,
					"No insertion shipment type has been assigned");
		}


		//countLoop=1;
		while (!mainShipments.isAllShipsAssigned()) {
			double x, y;
			int i = 0;
			//Get the x an y coordinate of the depot
			//Then use those to get the customer, that has not been allocated,
			// that is closest to the depot
			currDepot = (PVRPDepot) mainDepots.getPVRPHead().getNext();
			x = mainDepots.getHead().getXCoord();
			y = mainDepots.getHead().getYCoord();
			//Send the entire mainDepots and mainShipments to get the next shipment
			//to be inserted including the current depot

			/** ROUTING DONE HERE */
			PVRPShipment theShipment = mainShipments.getNextInsertShipment(mainDepots,
					currDepot, mainShipments, currShip);

			if (theShipment == null) { //shipment is null, print error message
				Settings.printDebug(Settings.COMMENT, "No shipment was selected");
			}
			//The selected shipment will be inserted into the route
			if (!mainDepots.insertShipment(theShipment)) {
				Settings.printDebug(Settings.COMMENT, "The Shipment: <" + theShipment.getIndex() +
						"> cannot be routed");
			}
			else {
				Settings.printDebug(Settings.COMMENT,
						"The Shipment: <" + theShipment.getIndex() +// " " + theShipment +
								"> was routed");
				//tag the shipment as being routed
				theShipment.setIsAssigned(true);
			}
		}

		ProblemInfo.depotLLLevelCostF.calculateTotalsStats(mainDepots);
	}

	//read in the data from the requested file in token format
	public int readDataFromFile(String PVRPFileName) throws IOException
	{
		// read in the MDVRP data from the listed file and load the information
		// into the availShipments linked list

		//type = 0 (MDVRP)
		//     = 1 (PTSP)
		//     = 2 (PVRP)
		char ch;
		String temp = "";
		int index = 0,
				j = 0,
				type = 0; //type
		//m        = 0,                           //number of vehicles
		//n        = 0,                           //number of customers
		//t        = 0,                           //number of days
		//D        = 0,                           //maximum duration of route
		//Q        = 0;                           //maximum load of vehicle
		int p = 3; //Np neighborhood size

		int depotIndex;
/**
		//Open the requested file
		FileInputStream fis;
		InputStreamReader isr;
		BufferedReader br;
		try {
			fis = new FileInputStream(VRPFileName);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
		}
		catch (Exception e) {
			System.out.println("File is not present");
			return 0;
		}
		*/

			int	typeNumeric = 0;
		int nodeNumber = 0, demandQ = 0, frequency = 0, numberCombinations = 0;
		double DUMMY = 0, xCoordinates = 0, yCoordinates = 0;
		int list[] = new int[ProblemInfo.MAX_COMBINATIONS];
		int currentComb[][] = new int[ProblemInfo.MAX_HORIZON][ProblemInfo.MAX_COMBINATIONS];

		FileInputStream file = new FileInputStream(new File(PVRPFileName));
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();        //an iterator for rows
/*

		//This section will get the initial information from the data file
		//Read in the first line from the file
		String readLn;
		StringTokenizer st;
*/
		//read in the first line

	//	while (rowIterator.hasNext())                        //while we have another row below the current
	//	{
			Row row = (Row) rowIterator.next();                    //get the next row
			Iterator<Cell> cellIterator = row.cellIterator();    //an iterator for columns
			Cell cell;

		int cellCount = 0;
		while (cellIterator.hasNext())    //while we have a next cell
		{

			cell = (Cell) cellIterator.next();                            //get the cell data
			cell.setCellType(typeNumeric);
			float currentCellValue = (float) cell.getNumericCellValue();    //extract cell data into int
			switch (cellCount)                                            //Finite State Machine
			{
				//first cell -- dunno what this is
				case 0:
					type = (int) currentCellValue;
					break;    //WE DON'T KNOW WHAT THIS VALUE IS -- POSSIBLY PROBLEM NUMBER

				//second cell -- the number vehs
				case 1:
					m = (int) currentCellValue;
					break;

				//third cell -- the number of nodes (customers)
				case 2:
					n = (int) currentCellValue;
					break;

				//fourth cell -- number of daysList
				case 3:
					t = (int) currentCellValue;
			}
			cellCount++;    //increment cell counter so we can move through FSM
		}

		System.out.println("TYPE\t" + type + "\tVehs\t" + m + "\tnodes\t" + n + "\tHorizon\t" + t);

		//Put the problem information into the ProblemInfo class
		//set the problem info for the problem
		ProblemInfo.numDepots = 1; //Set the number of depots to 1 for this problem
		ProblemInfo.fileName = PVRPFileName; //name of the file being read in
		ProblemInfo.probType = type; //problem type
		ProblemInfo.noOfVehs = m; //number of vehicles
		ProblemInfo.noOfShips = n; //number of shipments
		ProblemInfo.noOfDays = t; //number of days (horizon) or number of depots for MDVRP
		if (Q == 0) { //if there is no maximum capacity, set it to a very large number
			Q = 999999999;
		}
		if (D == 0) { //if there is no travel time, set it to a very large number
			D = 999999999; //if there is not maximum distance, set it to a very large number
			//ProblemInfo.maxCapacity = Q;  //maximum capacity of a vehicle
			//ProblemInfo.maxDistance = D;  //maximum distance of a vehicle
		}
		/** @todo  There three variables need to be defined at the beginning of
		 * the method */
		float maxCapacity = Q; //maximum capacity of a vehicle
		float maxDistance = D; //maximum distance of a vehicle

		String serviceType = "1"; //serviceType is the trucktype. Should match with
		//required truck type
		//In some problems, different truck types might be present to solve
		//the problem. For this problem, we assume that there is only one
		//truck type that is available.
		//loop through each truck type and store each one in the vector
		int numTruckTypes = 1;
		for (int i = 0; i < numTruckTypes; i++) {
			PVRPTruckType truckType = new PVRPTruckType(i, maxDistance, maxCapacity, serviceType);
			ProblemInfo.truckTypes.add(truckType);
		}

		/** @todo DO we really need customer types for the VRP problem? */
		//Some problems tend to have different customer types. In this problem
		//there is only one customter type. The integer value for the customer type
		//should match with the integer value for the truck type for the compatibiliy
		//check to work
		//read in the different customer types
		Vector custTypes = new Vector();
		//Obtain the different customer types
		for (int ct = 0; ct < 1; ct++) {
			custTypes.add(new Integer(1));
		}

		//place the number of depots and number of shipments in the linked list instance
		//These no longer seem to be needed for the shipment linked list. The total number of
		//shipments are tallied when they are inserted into the linked list
		//mainShipments.numShipments = n;
		//mainShipments.noDepots = t;
		//mainShipments.maxCapacity = Q;
		//mainShipments.maxDuration = D ;

		//display the information from the first line
		//System.out.println("typePvrp is       " + type);
		//System.out.println("numVeh is         " + m);
		//System.out.println("numCust is        " + n);
		//System.out.println("days is           " + t);
		//System.out.println("Depot duration is " + D);
		//System.out.println("capacity is       " + Q);

//		if (type != 1) { //then it is not an MDVRP problem
//			System.out.println("Problem is not an PVRP problem");
//			return 0;
//		}




		row = (Row) rowIterator.next();        //get the next row
		cellIterator = row.cellIterator();    //an iterator for columns


		while(row.getRowNum() > 0 && row.getRowNum() <= t){
			cellCount = 0;
			while (cellIterator.hasNext())    //while we have a next cell
			{
				cell = (Cell) cellIterator.next();                            //get the cell data
				cell.setCellType(typeNumeric);
				float currentCellValue = (float) cell.getNumericCellValue();    //extract cell data into int
				switch (cellCount)                                            //Finite State Machine
				{
					//MAX DIST
					case 0:
						D = (int) currentCellValue;
						break;    //WE DON'T KNOW WHAT THIS VALUE IS -- POSSIBLY PROBLEM NUMBER

					//MAX CAP
					case 1:
						Q = (int) currentCellValue;
						break;

				}
				cellCount++;    //increment cell counter so we can move through FSM

				daysList.add(new PVRPDays(type, n, m, t, D, Q));
			}
			row = (Row) rowIterator.next();        //get the next row
			cellIterator = row.cellIterator();    //an iterator for columns

		}

		System.out.println("MAX_DIS\t" + D + "\tMAX _CAP\t" + Q);



		int ROW_NOW = row.getRowNum();
		cellCount = 0;
		while (cellIterator.hasNext() && row.getRowNum() == t + 1)                //while we have another cell
		{

			cell = (Cell) cellIterator.next();
			cell.setCellType(typeNumeric);
			float currentCellContents = (float) cell.getNumericCellValue();

			switch (cellCount)
			{
				case 0:
					nodeNumber = (int) currentCellContents;
					break;
				case 1:
					xCoordinates = (double) currentCellContents;
					break;
				case 2:
					yCoordinates = (double) currentCellContents;
					break;
				default:
					break;

			}
			cellCount++;
		}

		System.out.println("DEPOT_NUM\t" + nodeNumber + "\tDEPOT_X\t" + xCoordinates + "\tDEPOT_Y\t" + yCoordinates);

		PVRPDepot depot = new PVRPDepot(nodeNumber, xCoordinates, yCoordinates); //n is the number of customers
		mainDepots.insertDepotLast(depot);

		//Each depot has a mainTrucks. The different truck types available are
		//inserted into the mainTrucks type. For the VRP, there is only one truck type
		depot = (PVRPDepot) mainDepots.getHead().getNext();
		for (int i = 0; i < ProblemInfo.truckTypes.size(); i++) {
			PVRPTruckType ttype = (PVRPTruckType) ProblemInfo.truckTypes.elementAt(i);
			depot.getMainTrucks().insertTruckLast(new PVRPTruck(ttype, depot.getXCoord(), depot.getYCoord()));
		}


		row = (Row) rowIterator.next();        //get the next row
		cellIterator = row.cellIterator();    //an iterator for columns


		while(row.getRowNum() > t + 1 && rowIterator.hasNext()){
			cellCount = 0;
			int listIndex = 0;

			while (cellIterator.hasNext())                //while we have another cell
			{
				cell = (Cell) cellIterator.next();
				cell.setCellType(typeNumeric);
				float currentCellContents = (float) cell.getNumericCellValue();

				switch (cellCount)
				{
					case 0:
						nodeNumber = (int) currentCellContents;
						break;
					case 1:
						xCoordinates = (double) currentCellContents;
						break;
					case 2:
						yCoordinates = (double) currentCellContents;
						break;
					case 3:
						DUMMY = (double) currentCellContents;     //USUALLY ALL ZEROS
						//DUNNO WHAT THIS ACTUALLY DOES
						break;
					case 4:
						demandQ = (int) currentCellContents;
						break;
					case 5:
						frequency = (int) currentCellContents;
						break;
					case 6:
						numberCombinations = (int) currentCellContents;
						break;
					default:
						list[listIndex] = (int) currentCellContents;
						listIndex++;
						break;

				}
				cellCount++;
			}

			for (int l = 0; l < numberCombinations; l++)
			{
				currentComb[l] = mainShipments.getCurrentComb(list, l, t); // current visit comb
				//insert the customer data into the linked list
			}

            Integer custType = (Integer) custTypes.elementAt(0);
            mainShipments.insertShipment(nodeNumber, xCoordinates, yCoordinates, DUMMY, demandQ, frequency, numberCombinations, list, currentComb);


			row = (Row) rowIterator.next();        //get the next row
			cellIterator = row.cellIterator();    //an iterator for columns

		}




		return 1;
		}



	/**
	 * Print  out the data to the console
	 */
	public void printDataToConsole() {
		try {
			mainShipments.printPVRPShipmentsToConsole();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write out the data file that was read in
	 * @param file name of file used for generating the data
	 */

	public void writeDataFile(String file) throws IOException {

		FileOutputStream out = new FileOutputStream(new File(file));
		System.out.print("TEST OUTPUT");
		mainShipments.writePVRPShipments(out);



//		try {
//			PrintStream ps = new PrintStream(new FileOutputStream(ProblemInfo.
//					outputPath +file +"_students.txt"));
//			mainShipments.writePVRPShipments(ps);
//		}
//		catch (IOException ioex) {
//			ioex.printStackTrace();
//		}
	}

	/**
	 * Will write a long detailed solution for the problem
	 * @param file name of the file to write to
	 */
	public void writeLongSolution(String file) {
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(ProblemInfo.
					outputPath + file + "_long.txt"));
			mainDepots.printDepotLinkedList(ps);
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	/**
	 * Will write a short solution for the problem
	 * @param file name of the file to write to
	 */
	public void writeShortSolution(String file) {
		try {
			//PrintStream ps = new PrintStream(new FileOutputStream(ProblemInfo.
			//outputPath + "/" + file + "_short.txt"));
			PrintStream ps = new PrintStream(new FileOutputStream(ProblemInfo.
					outputPath + file + "_short.txt"));

			ps.println("File: " + file + " Num Depots: " +
					ProblemInfo.numDepots + " Num Pick Up Points: " +
					ProblemInfo.numCustomers + " Num Trucks: " +
					ProblemInfo.numTrucks + " Processing Time: " +
					(endTime - startTime) / 1000 + " seconds");
			ps.println(mainDepots.getAttributes().toDetailedString());
			ps.println();

			Depot depotHead = mainDepots.getHead();
			Depot depotTail = mainDepots.getTail();

			while (depotHead != depotTail) {
				Truck truckHead = depotHead.getMainTrucks().getHead();
				Truck truckTail = depotHead.getMainTrucks().getTail();

				while (truckHead != truckTail) {
					ps.print("Truck #" + truckHead.getTruckNum() + " MaxCap: " +
							truckHead.getTruckType().getMaxCapacity() + " Demand: " +
							truckHead.getAttributes().getTotalDemand() + " ROUTE:");

					Nodes nodesHead = truckHead.getMainNodes().getHead();
					Nodes nodesTail = truckHead.getMainNodes().getTail();

					while (nodesHead != nodesTail) {
						ps.print(nodesHead.getIndex() + " ");
						nodesHead = nodesHead.getNext();
					}

					ps.println();
					truckHead = truckHead.getNext();
				}

				ps.println();
				ps.println();
				depotHead = depotHead.getNext();
			}
			for (int i = 0; i < optInformation.size(); i++) {
				ps.println(optInformation.elementAt(i));
			}
		}
		catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

} //End of PVRP file





















/**
//PVRP PROBLEM
//CPSC 464
//AARON ROCKBURN; JOSHUA SARVER

//***********	DECLARATION_S_OTHER	**********************************************************************************\\
// FUNCTION_START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


//PACKAGE TITLE
package edu.sru.thangiah.zeus.pvrp;

//IMPORT STATEMENTS
import edu.sru.thangiah.zeus.core.Depot;
import edu.sru.thangiah.zeus.core.ProblemInfo;
import edu.sru.thangiah.zeus.core.Settings;
import edu.sru.thangiah.zeus.core.Shipment;
import edu.sru.thangiah.zeus.pvrp.PVRPShipmentLinkedList.PVRPShipmentLinkedList;
import edu.sru.thangiah.zeus.pvrp.PVRPShipmentLinkedList.SmallestPolarAngleToDepot;
import edu.sru.thangiah.zeus.pvrp.pvrpnodeslinkedlist.LinearGreedyInsertShipment;
import edu.sru.thangiah.zeus.pvrp.pvrpqualityassurance.PVRPQualityAssurance;
import edu.sru.thangiah.zeus.vrp.vrpqualityassurance.VRPQualityAssurance;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;





//CLASS
public class PVRP
{

	//***********	CLASS_VARIABLES	**********************************************************************************\\
	//PROBLEM DATA VARIABLES
	double	depotXCoordinates 	= 0,
			depotYCoordinates 	= 0;
	int 	vehicleCapacity 	= 0;

	//NODES VARIABLES
	int 	customerNumber 	= 0;
	double 	xCoordinates 	= 0,
			yCoordinates 	= 0;
	int		demandQ 		= 0,
			frequency 		= 0;

	//OTHER VARIABLES
	long	startTime 	= 0,
			endTime 	= 0;    //tracks the CPU processing time


	//INSTANTIATE SOME OBJECTS
	//not sure what these are yet
	private Vector mainOpts = new Vector();         //collection of optmizations
	private Vector optInformation = new Vector();         //contains route information


	private PVRPShipmentLinkedList mainShipments = new PVRPShipmentLinkedList(); //customers read in from a file or database that are available
	private PVRPDepotLinkedList mainDepots = new PVRPDepotLinkedList();    //linked list for depots

	private PVRPQualityAssurance pvrpQA;        //checks our solution for good data
	private PVRPExcelReadWrite excel;        //handles reading our EXCEL file

	int list[] = new int[ProblemInfo.MAX_COMBINATIONS];
	int currentCombination[][] = new int[ProblemInfo.MAX_HORIZON][ProblemInfo.MAX_COMBINATIONS];



	//***********	DECLARATION_S	**********************************************************************************\\
	public PVRP(String excelDataInput) throws IOException    //not a curveball
	{
		//DECLARTION VARIABLES
		boolean 	isDiagnostic = false,
					status;
		Shipment	tempShip;
		Depot 		thisDepot;
		int 		type,
					depotNo,
					countAssignLoop;
		String 		outputFileName;

		ProblemInfo.truckTypes = new Vector();                                        //WE SHOULD ONLY HAVE ONE TRUCK TYPE
		int list[] = new int[ProblemInfo.MAX_COMBINATIONS];                //array of 0'1 and 1's for the combinations
		int currentComb[][] = new int[ProblemInfo.MAX_HORIZON][ProblemInfo.MAX_COMBINATIONS];

		excel = new PVRPExcelReadWrite(excelDataInput, mainShipments);    //instantiate the excel class

		//read some data -- this class adds the data to a linked list
		excel.excelReader(list, currentComb);

		Settings.printDebug(Settings.COMMENT, "Read Data File: " + ProblemInfo.inputPath + excelDataInput);
		printDataToConsole();
		System.out.println("E#RGTY#$%TYG#$%TYG#$%HY$%");
		excel.excelWriter(list);

		//MAKE SURE DATA IS IN OUR SHIPMENT LINKED LIST
		if (mainShipments.getPVRPHead() == null)
		{
			Settings.printDebug(Settings.ERROR, "VRP: Shipment linked list is empty");
		}

		ProblemInfo.selectShipType = new SmallestPolarAngleToDepot();
		Settings.printDebug(Settings.COMMENT, SmallestPolarAngleToDepot.WhoAmI());

		//set up the shipment insertion type
		ProblemInfo.insertShipType = new LinearGreedyInsertShipment();
		Settings.printDebug(Settings.COMMENT, LinearGreedyInsertShipment.WhoAmI());

		//Capture the CPU time required for solving the problem
		startTime = System.currentTimeMillis();

		createInitialRoutes();
		System.out.println("Completed initial routes");

		//Get the initial solution
		//Depending on the Settings status, display information on the routes
		//Trucks used, total demand, dist, travel time and cost
		Settings.printDebug(Settings.COMMENT, "Created Initial Routes ");
		Settings.printDebug(Settings.COMMENT, "Initial Stats: " + mainDepots.getSolutionString());


		//Check for the quality and integrity of the solution
		System.out.println("Starting QUALITY ASSURANCE");
		pvrpQA = new PVRPQualityAssurance(mainDepots, mainShipments);
		if (pvrpQA.runQA() == false)
		{
			Settings.printDebug(Settings.ERROR, "QA FAILED!");
		}
		else
		{
			Settings.printDebug(Settings.COMMENT, "QA succeeded");
		}


		//ZeusGui guiPost = new ZeusGui(mainDepots, mainShipments);		//CALL THIS SOME OTHER DAY...WHEN EVERYTHING WORKS

	}


	//***********	FUNCTIONS	**************************************************************************************\\
	// PRINT_DATA_TO_CONSOLE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public void printDataToConsole()
	{
		mainShipments.printPVRPShipmentsToConsole();
	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


/*

	// READ_DATA_FROM_FILE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public void readDataFromFile() throws IOException
	{

		//EXTRACT THE NEEDED DATA FROM EXCEL

		excel.excelReader(list, currentCombination); //LAST NODE IS DEPOT
		//WHAT ABOUT MULTIPLE DEPOTS?

		//SPLIT PROBLEM INFO INTO COLUMNS AS WE ONLY HAVE ONE ROW OF THIS
		String[] problemInfoByColumn = problemInfo.split(space);

		//GET DEPOT COORDINATES, VEHICLE CAPACITY, ETC.
		//*********THIS WILL NEED MORE WORK @#$%&*^%$#*(&%$&^^&&^&^^&^%
		depotXCoordinates = Integer.parseInt(problemInfoByColumn[0]);
		depotYCoordinates = Integer.parseInt(problemInfoByColumn[1]);
		vehicleCapacity = Integer.parseInt(problemInfoByColumn[2]);

		if (vehicleCapacity == 0)
		{              //IF CAPACITY DOESN'T MATTER
			vehicleCapacity = 99999999;    //JUST PICK A LARGE NUMBER
		}


		//SPLIT NODES STRING INTO ROWS AS WE JUST GET BACK ONE BIG CHUNK OF TEXT
		String[] nodesInfoByRow = problemInfo.split(newline);

		//GET NUMBER OF ROWS IN STRING
		int numberOfRows = nodesInfoByRow.length;

		//FOR EVERY ROW WE WILL SPLIT IT INTO COLUMNS
		for (int counter = 0; counter < numberOfRows; counter++)
		{
			String[] nodesInfoByColumn = nodesInfoByRow[counter].split(space);
			customerNumber = Integer.parseInt(nodesInfoByColumn[0]);
			xCoordinates = Integer.parseInt(nodesInfoByColumn[1]);
			yCoordinates = Integer.parseInt(nodesInfoByColumn[2]);
			demandQ = Integer.parseInt(nodesInfoByColumn[3]);
			frequency = Integer.parseInt(nodesInfoByColumn[4]);

			//AFTER WE COLLECT OUR DATA WE ADD IT AS A SHIPMENT
			//mainShipments.insertShipment(customerNumber, xCoordinates, yCoordinates, demandQ, frequency);


			//HANDLE MULTIPLE DEPOTS AS IN VRP
		}
	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
*/
/**

	// WRITE_DATA_TO_FILE >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public void writeDataToFile() throws FileNotFoundException
	{
		PrintStream ps = new PrintStream(new FileOutputStream(ProblemInfo.outputPath + "genericOutput.xlsx"));
		mainDepots.printDepotLinkedList(ps);

	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


	// CREATE_INITIAL_ROUTES >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	public void createInitialRoutes() {
		//OptInfo has old and new attributes
		PVRPDepot currDepot = null; //current depot
		PVRPShipment currShip = null; //current shipment
		//int countLoop=0;

		//check if selection and insertion type methods have been selected
		if (ProblemInfo.selectShipType == null) {
			Settings.printDebug(Settings.ERROR,
									   "No selection shipment type has been assigned");

		}
		if (ProblemInfo.insertShipType == null) {
			Settings.printDebug(Settings.ERROR,
									   "No insertion shipment type has been assigned");
		}


		//countLoop=1;
		while (!mainShipments.isAllShipsAssigned()) {
			double x, y;
			int i = 0;
			//Get the x an y coordinate of the depot
			//Then use those to get the customer, that has not been allocated,
			// that is closest to the depot
			currDepot = (PVRPDepot) mainDepots.getPVRPHead().getNext();
			x = mainDepots.getHead().getXCoord();
			y = mainDepots.getHead().getYCoord();
			//Send the entire mainDepots and mainShipments to get the next shipment
			//to be inserted including the current depot
			PVRPShipment theShipment = mainShipments.getNextInsertShipment(mainDepots,
																				 currDepot, mainShipments, currShip);

			if (theShipment == null) { //shipment is null, print error message
				Settings.printDebug(Settings.COMMENT, "No shipment was selected");
			}
			//The selected shipment will be inserted into the route
			if (!mainDepots.insertShipment(theShipment)) {
				Settings.printDebug(Settings.COMMENT, "The Shipment: <" + theShipment.getIndex() +
															  "> cannot be routed");
			}
			else {
				Settings.printDebug(Settings.COMMENT,
										   "The Shipment: <" + theShipment.getIndex() +// " " + theShipment +
												   "> was routed");
				//tag the shipment as being routed
				theShipment.setIsAssigned(true);
			}
		}

		ProblemInfo.depotLLLevelCostF.calculateTotalsStats(mainDepots);
	}
	//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


}
**/