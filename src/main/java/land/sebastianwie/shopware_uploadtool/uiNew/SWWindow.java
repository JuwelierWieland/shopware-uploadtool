package land.sebastianwie.shopware_uploadtool.uiNew;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SWWindow extends JFrame {
	private static final long serialVersionUID = -8761801624622699827L;
	// private Controller c;
	public Container displayedPanel;

	public SWWindow(JPanel startPanel, Controller c) {
		super("Shopware Upload-Tool");
		// this.c = c;
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.setSize(640, 480);
		// super.setResizable(false);

		this.setDisplayedPanel(startPanel);

		super.setVisible(true);
	}

	public SWWindow(Controller c) {
		this(new LoginPanel(c), c);
	}

	public void setDisplayedPanel(Container panel) {
		this.displayedPanel = panel;
		super.setContentPane(displayedPanel);
		super.revalidate();
		super.repaint();
	}

	public Container getDisplayedPanel() {
		return displayedPanel;
	}
}
