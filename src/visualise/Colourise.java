package visualise;

import java.awt.Color;

import dof.Mark;
import dof.analysis.BasicAnalysis;

public class Colourise {
	
	public static Color getWeightedColor(Mark m, float[] baseColorHSB, int maxNodes) {
		
		double saturation = BasicAnalysis.getTotalNodes(m)/(double)maxNodes > 0.1 ?  BasicAnalysis.getTotalNodes(m)/(double)maxNodes : 0.1 ;
		
		int newColor = Color.HSBtoRGB(baseColorHSB[0], (float) (baseColorHSB[1]*saturation), baseColorHSB[2]);
		
		return new Color(newColor);
		
	}

}
