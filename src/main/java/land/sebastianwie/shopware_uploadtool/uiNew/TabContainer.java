package land.sebastianwie.shopware_uploadtool.uiNew;

import java.awt.BorderLayout;

//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import land.sebastianwie.shopware_uploadtool.uiNew.tabs.DeleteTab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.DownloadTab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.InfoTab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.Tab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.UpdateStockTab;
import land.sebastianwie.shopware_uploadtool.uiNew.tabs.UploadTab;

public class TabContainer extends JPanel {
	private static final long serialVersionUID = 2345757427555219846L;
	private Controller c;

	private JTabbedPane tabs;

	// private MenuBar menu;

	public TabContainer(Controller c) {
		this.c = c;
		super.setLayout(new BorderLayout());
		super.setOpaque(true);

		// menu = new MenuBar(this);
		tabs = new JTabbedPane();
		tabs.setVisible(true);

		// super.add(menu, BorderLayout.PAGE_START);
		addTab(new InfoTab(this.c));
		addTab(new DownloadTab(this.c));
		addTab(new UploadTab(this.c));
		addTab(new UpdateStockTab(this.c));
		addTab(new DeleteTab(this.c));
		super.add(tabs, BorderLayout.CENTER);
	}

	public void addTab(Tab tab) {
		tabs.addTab(tab.getTitle(), tab);
		// tabs.setSelectedIndex(tabs.getTabCount() - 1);
	}

	public void closeTab() {
		if (tabs.getTabCount() > 0)
			// if (((Tab) tabs.getSelectedComponent()).isCloseable())
			tabs.remove(tabs.getSelectedIndex());
	}

	// private class MenuBar extends JPanel {
	// private static final long serialVersionUID = 5315809155460464591L;
	// private JButton addButton, closeButton;
	// private JComboBox<String> selection;
	// private TabContainer ref;

	// private final String[] selectionItems = { "Information", "Downloads",
	// "Uploads", "Artikel löschen",
	// "Bestände aktualisieren" };

	// public MenuBar(TabContainer ref) {
	// super.setLayout(new GridLayout(1, 3));
	// addButton = new JButton("Neue Registerkarte");
	// closeButton = new JButton("Registerkarte schließen");
	// selection = new JComboBox<>(selectionItems);
	// this.ref = ref;

	// super.add(selection);
	// super.add(addButton);
	// super.add(closeButton);

	// addButton.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// add((String) selection.getSelectedItem());
	// }
	// });

	// closeButton.addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// ref.closeTab();
	// }
	// });
	// }

	// private void add(String selectedItem) {
	// if ("Information".equals(selectedItem))
	// ref.addTab(new InfoTab(c));
	// if ("Uploads".equals(selectedItem))
	// ref.addTab(new UploadTab(c));
	// }
	// }
}
