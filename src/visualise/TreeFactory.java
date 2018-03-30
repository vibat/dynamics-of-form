/*
 * [The "BSD license"]
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the abego Software GmbH nor the names of its 
 *    contributors may be used to endorse or promote products derived from this 
 *    software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package visualise;

import java.awt.Color;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;

import dof.Mark;
import dof.analysis.BasicAnalysis;

public class TreeFactory {

	/**
	 * @param head The top node in the created DoF structure
	 * @param baseColor Base tree color 
	 * @param selectedStyle The style of tree coloring selected
	 * @return a "Sample" tree with {@link TextInBox} items as nodes.
	 */
	public static TreeForTreeLayout<TextInBox> createTree(Mark head, Color baseColor, String selectedStyle, String selectedMode) {
		
		TextInBox root = new TextInBox(head.hasTargets() ? "R" : "M", baseColor , 20, 20);
		
		DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(
				root);
		
		for (Mark child: head.children) {
			BuildChild(child, root, tree, baseColor, BasicAnalysis.getTotalNodes(head), selectedStyle, selectedMode);
		}
		
		return tree;
	}
	
	private static void BuildChild(Mark current, TextInBox parent, DefaultTreeForTreeLayout<TextInBox> tree, Color baseColor, int maxNodes, String selectedStyle, String selectedMode) {
		
		Color color;
		
		if (selectedMode.equals("Brownian value")) {
			color = new Color(
					255 - parent.color.getRed(),
					255 - parent.color.getGreen(),
					255 - parent.color.getBlue()
                    );
		}
		
		else if (selectedStyle.equals("Node weight")) {
			color = Colourise.getWeightedColor(
					current,
					Color.RGBtoHSB(
							baseColor.getRed(),
							baseColor.getGreen(),
							baseColor.getBlue(),
							null),
					maxNodes);
		}
		
		else {
			color = baseColor;
		}
		
		TextInBox node = new TextInBox(current.hasTargets() ? "R" : "M", color, 20, 20);
		
		tree.addChild(parent, node);
		
		for (Mark child: current.children) {
			BuildChild(child, node, tree, baseColor, maxNodes, selectedStyle, selectedMode);
		}
		
	}

}
