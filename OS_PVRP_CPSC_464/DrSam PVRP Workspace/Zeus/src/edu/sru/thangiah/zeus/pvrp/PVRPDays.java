package edu.sru.thangiah.zeus.pvrp;

import edu.sru.thangiah.zeus.core.Day;

/**
 * Created by jks1010 on 10/2/2014.
 */
public class PVRPDays
        extends Day
        implements java.io.Serializable, java.lang.Cloneable
{
	int dayType;
	int nodes;
	int numTrucks;
	int planningPeriod;
	int distance;
	int capacity;

public PVRPDays(int type, int n, int m, int t, int d, int q) {

	dayType = type;
	nodes = n;
	numTrucks = m;
	planningPeriod = t;
	distance = d;
	capacity = q;
	
}
}
