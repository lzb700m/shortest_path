/**
 * Definition of global constant used throughout the project
 * 
 * @author Peng Li
 * @author Nan Zhang
 */
public class DEF {
	protected static final int UNIFORM_WEIGHT = 1;
	protected static final int DAG = 2;
	protected static final int NON_NEG_WEIGHT = 3;
	protected static final int OTHER = 4;

	protected static final int SIZE_CUT_OFF = 100;
	protected static final int SOURCE = 1;

	protected static final String ERROR_LEVEL1 = "Unable to solve problem. Graph has a negative cycle";
	protected static final String ERROR_LEVEL2 = "Non-positive cycle in graph. DAC is not applicable";
}
