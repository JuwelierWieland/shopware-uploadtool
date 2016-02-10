package land.sebastianwie.shopware_uploadtool.uiNew.tabs;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class Tab extends JPanel {
	private static final long serialVersionUID = 4653154438071896344L;

	@Deprecated
	public abstract boolean isCloseable();
	public abstract String getTitle();
	public abstract void lock();
	public abstract void unlock();
	public abstract JButton getCancelButton();
	
}
