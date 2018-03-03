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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import dof.Mark;
import visualise.svg.SVGForTextInBoxTree;
import visualise.swing.TextInBoxTreePane;

public class Imagify {

	@SuppressWarnings("unused")
	private static void showInDialog(JComponent panel, SVGForTextInBoxTree generator) {
		JDialog dialog = new JDialog();
		Container contentPane = dialog.getContentPane();
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(
				10, 10, 10, 10));
		contentPane.add(panel);
		
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("File");
		JMenuItem save = new JMenuItem("Save");
		filemenu.add(save);
		menubar.add(filemenu);
		
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
            	PrintStream fileStream;
            	
            	JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    			jfc.setFileFilter(new FileNameExtensionFilter("Scalable Vector Graphics (.svg)", "svg", "*.svg"));

    			int returnValue = jfc.showSaveDialog(null);
    			 
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFile = jfc.getSelectedFile();
					try {
						String filePath = selectedFile.getAbsolutePath();
						if (!filePath.endsWith(".svg")) { filePath += ".svg";}
						fileStream = new PrintStream(filePath);
    					fileStream.println(generator.getSVG());
    					fileStream.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
            }
        });
        
        dialog.setJMenuBar(menubar);
		
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	
	@SuppressWarnings("unused")
	public static void visualise(Mark head) {
		TreeForTreeLayout<TextInBox> tree = TreeFactory.createTree(head, Color.orange, "solid");
		
		// setup the tree layout configuration
		double gapBetweenLevels = 50;
		double gapBetweenNodes = 10;
		DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		// create the NodeExtentProvider for TextInBox nodes
		TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		// create the layout
		TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		// Create a panel that draws the nodes and edges and show the panel
		TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
		SVGForTextInBoxTree generator = new SVGForTextInBoxTree(treeLayout);
	}

}
